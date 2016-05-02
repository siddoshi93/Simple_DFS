package dfs_MN;


import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;


/* Program specific imports */
import dfs_api.*;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class MN_Server_PM
{
    private static ExecutorService workers; /* Workers threads */
    private static ExecutorService daemon_exec;
    private static AliveServer alive_server; /* HeartBeat Server */
    private static MaintenanceDmn maintenance_dmn; /* Maintenance Daemon */

    /* List which maintains the current active client REQUESTS in the Server */
    private static ConcurrentHashMap<String,ClientRequestHandle> active_client_list;

    private static ServerSocket client_request; /* Server Socket which listens for client request */
    private static InetAddress hostAddress;
    private static ClientRequestHandle curr_req;
    private static String new_uuid;
    private static MN_MiscDmn mn_misc_daemon;

    public static class Storagesort implements Comparator<StorageNode>,Serializable {

        public int compare(StorageNode s1, StorageNode s2) {
            if(s2.Size < s1.Size)
                return 1;
            else
                return -1;
        }
    }

    public static void start_pm_server()
    {
        try
        {
			/* Wait for a connection so that it can be served in a thread */
            setUpMN(); /* Set up the server */

            while(DFS_Globals.is_MN_on)
            {
				/* Generate a random UUID for Every new Client Request to be used as a Key in the HashMap */
                new_uuid = UUID.randomUUID().toString();

                curr_req = new ClientRequestHandle(client_request.accept(),new_uuid); /* Listen to client request and assign the request to a worker thread */

                active_client_list.put(new_uuid,curr_req); /* Add the client to the end of the list */

                workers.execute(curr_req);
            }

        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            cleanUpMN();
        }
    }

    public static void cleanUpMN()
    {
        try
        {
            client_request.close(); /* Close the server socket connection */
            workers.shutdown();
            workers.awaitTermination(DFS_CONSTANTS.ONE, TimeUnit.MINUTES);
			/* Close all the open connection */
            daemon_exec.shutdown();
            daemon_exec.awaitTermination(DFS_CONSTANTS.ONE,TimeUnit.DAYS.SECONDS);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static MaintenanceDmn getMaintenance_dmn()
    {
        return maintenance_dmn;
    }

    public static boolean remove_active_request(String uuid)
    {
        return (active_client_list.remove(uuid) != null);

    }

    public static void setUpMN() throws IOException,ClassNotFoundException
    {
        hostAddress = InetAddress.getLocalHost();  /* Get the host address */
        client_request = new ServerSocket(DFS_CONSTANTS.MN_LISTEN_PORT,DFS_CONSTANTS.REQUEST_BACK_LOG/*,hostAddress*/);
        active_client_list = new ConcurrentHashMap();
        workers = Executors.newFixedThreadPool(DFS_CONSTANTS.NUM_OF_WORKERS);
        daemon_exec = Executors.newFixedThreadPool(DFS_CONSTANTS.THREE);

        /* Load/Init data for the Server */
        load_or_init_data();

		/*if(!setUp_DN_List())
		{
			System.out.println("Please define a proper config file for DN!!!!");
			System.exit(DFS_CONSTANTS.SUCCESS);
		}*/

		/* Bring up the MISC Daemon */
        mn_misc_daemon = new MN_MiscDmn();
        daemon_exec.submit(mn_misc_daemon);

		/* Maintenance Daemon */
        maintenance_dmn = new MaintenanceDmn();
        daemon_exec.submit(maintenance_dmn);

		/* Service Check Daemon */
        if(!bringUpAliveServer())
        {
            System.out.println("Unnable to bring up the alive Server!!!!");
            System.exit(DFS_CONSTANTS.SUCCESS);
        }

        /* If this was a take over then it needs to notify the DN */
        check_and_notify_dn();
    }

    public static void check_and_notify_dn()throws UnknownHostException,IOException
    {
        if(DFS_Globals.mn_mode_ind == DFS_CONSTANTS.PM) /* Boot up in Primary Mode */
            return;

        /* Iterate over the PQ */
        Iterator<StorageNode> itr = DFS_Globals.dn_q.iterator();
        StorageNode sn = null;
        PacketTransfer pt = null;
        Packet req_packet = null;
        Packet res_packet = null;
        Socket connect = null;

        while (itr.hasNext())
        {
            sn = itr.next();
            if(check_service(sn))
            {
                /* Alive, So notify the DN */
                connect = new Socket(sn.IPAddr,DFS_CONSTANTS.DN_MISC_LISTEN_PORT); /* Connect to communicate with the DN */
                pt = new PacketTransfer(connect);

                /* Set the request packet */
                req_packet = new Packet();
                req_packet.command = DFS_CONSTANTS.CHANGE_MN;
                req_packet.mn_addr = Inet4Address.getLocalHost().getHostAddress();

                /* Send the request */
                pt.sendPacket(req_packet);

                /* Recieve the reponse */
                res_packet = pt.receivePacket();
                if(res_packet.response_code != DFS_CONSTANTS.OK)
                {
                    System.out.println("DN with IP : " + sn.IPAddr + " did not accept");
                    sn.isAlive = false;
                    sn.Size = DFS_CONSTANTS.INVALID_SIZE;
                    connect.close();
                    continue;
                }
                connect.close();
            }
            else
            {
                /* Dead so remove it from PQ */
                sn.isAlive = false;
                sn.Size = DFS_CONSTANTS.INVALID_SIZE;
            }
        }
    }

    public static boolean check_service(StorageNode sn)
    {
        Socket dn_connect = null;
        try
        {
            //Pinging Data Nodes to Check if They are ALIVE for service.
            dn_connect = new Socket();
            dn_connect.connect(new InetSocketAddress(sn.IPAddr,DFS_CONSTANTS.ALIVE_LISTEN_PORT),DFS_CONSTANTS.TIMEOUT);
            /* Connection succesfull */
            return true;
        }
        catch (IOException ex)
        {
            return false;
        }
        finally {
            try {
                dn_connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Load or Init the data */
    public static void load_or_init_data() throws IOException,ClassNotFoundException
    {
        File check_file = new File(DFS_Globals.sdfs_path + DFS_CONSTANTS.persistance_file);

        if(DFS_Globals.mn_mode_ind == DFS_CONSTANTS.SM && !check_file.exists())
        {
            System.out.println("MN Server was a SM mode but did not find any syncing file ");
        }

        if(DFS_Globals.mn_mode_ind == DFS_CONSTANTS.PM && !check_file.exists())
        {
            /* Initialize Storage pool */
            Storagesort storagesort = new Storagesort();
            DFS_Globals.dn_q = new PriorityBlockingQueue(DFS_CONSTANTS.PQ_SIZE, storagesort);
            DFS_Globals.global_client_list = new HashMap();
        }
        else
        {
            /* Load Storage pool */
            FileInputStream fis = new FileInputStream(DFS_Globals.sdfs_path + DFS_CONSTANTS.persistance_file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            DFS_Globals.global_client_list = (HashMap<String, ClientWrapper>) ois.readObject();
            DFS_Globals.dn_q = (PriorityBlockingQueue<StorageNode>) ois.readObject();
            ois.close();
            fis.close();
        }

    }

    public static boolean setUp_DN_List()
    {
        Storagesort storagesort = new Storagesort();
        //DFS_Globals.dn_q = new PriorityQueue(DFS_CONSTANTS.PQ_SIZE,storagesort);
        String line = null;
        StorageNode temp;
        String[] storage_params;
        FileReader fr = null;
        BufferedReader bufferedReader = null;

        try
        {
            fr = new FileReader(DFS_Globals.sdfs_path + DFS_CONSTANTS.dn_list);
            bufferedReader = new BufferedReader(fr);

            while((line = bufferedReader.readLine()) != null)
            {
                storage_params = line.split(",");
                temp = new StorageNode(storage_params[0],storage_params[1],Long.parseLong(storage_params[2]));
                DFS_Globals.dn_q.offer(temp);
            }

            // Always close files.
            bufferedReader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                bufferedReader.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean bringUpAliveServer()
    {
        alive_server = new AliveServer();
        daemon_exec.submit(alive_server);
        return alive_server.isSetUp();
    }
}
