package dfs_MN;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/* Program specific imports */
import dfs_api.*;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class Main_Node_Server 
{
	private static ExecutorService workers; /* Workers threads */
	private static AliveServer alive_server; /* HeartBeat Server */
	private static MaintenanceDmn maintenance_dmn; /* Maintenance Daemon */

	/* List which maintains the current active client REQUESTS in the Server */
	private static ConcurrentHashMap<String,ClientRequestHandle> active_client_list;

	private static ServerSocket client_request; /* Server Socket which listens for client request */
	private static InetAddress hostAddress;
	private static ClientRequestHandle curr_req;
	private static String new_uuid;

   public static class Storagesort implements Comparator<StorageNode> {

		public int compare(StorageNode s1, StorageNode s2) {
			if(s2.Size < s1.Size)
				return 1;
			else
				return -1;
		}
	}
	
	public static void main(String[] args)
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

				//System.out.println("Got connection : ");
				active_client_list.put(new_uuid,curr_req); /* Add the client to the end of the list */

				workers.submit(curr_req);
			}
						
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
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
			workers.awaitTermination(1, TimeUnit.MINUTES);
			/* Close all the open connection */
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static boolean remove_active_request(String uuid)
	{
		return (active_client_list.remove(uuid) != null);

	}
	
	public static void setUpMN() throws IOException
	{		
		hostAddress = InetAddress.getLocalHost();  /* Get the host address */
		client_request = new ServerSocket(DFS_CONSTANTS.MN_LISTEN_PORT,DFS_CONSTANTS.REQUEST_BACK_LOG/*,hostAddress*/);
		active_client_list = new ConcurrentHashMap();
		workers = Executors.newFixedThreadPool(DFS_CONSTANTS.NUM_OF_WORKERS);

		DFS_Globals.global_client_list = new HashMap();
		if(!setUp_DN_List())
		{
			System.out.println("Please define a proper config file for DN!!!!");
			System.exit(DFS_CONSTANTS.SUCCESS);
		}

		/* Service Check Daemon */
		if(bringUpAliveServer())
		{
			System.out.println("Please define a proper config file for DN!!!!");
			System.exit(DFS_CONSTANTS.SUCCESS);
		}

		/* Maintenance Daemon */

	}

	public static boolean setUp_DN_List()
	{
		Storagesort storagesort = new Storagesort();
		DFS_Globals.dn_q = new PriorityQueue(DFS_CONSTANTS.PQ_SIZE,storagesort);
		String line = null;
		StorageNode temp;
		String[] storage_params;
		FileReader fr = null;
		BufferedReader bufferedReader = null;

		try
		{
			fr = new FileReader(DFS_CONSTANTS.sdfs_path + DFS_CONSTANTS.dn_list);
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
		new Thread(alive_server).start();
		return alive_server.isSetUp();
	}
}
