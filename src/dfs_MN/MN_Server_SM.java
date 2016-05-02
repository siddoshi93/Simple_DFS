package dfs_MN;

import dfs_api.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Anas on 5/1/2016.
 */
public class MN_Server_SM {

    public static Socket persistentSocket = null;
    public static FileTransfer ftp;
    public static String file_path;
    public static PacketTransfer pt;
    public static ObjectInputStream ois;
    public static FileOutputStream fos = null;
    public static ObjectOutputStream os = null;
    public static Packet pack;

    //Starting point for Secondary Server
    public static void start_sm_server()
    {
        file_path = (DFS_Globals.sdfs_path + DFS_CONSTANTS.persistance_file);
        System.out.println(DFS_Globals.sdfs_path);
        FileTransfer.check_and_create_dir(DFS_Globals.sdfs_path);

        ftp = new FileTransfer();
        if (((DFS_Globals.server_addr = System.getenv(DFS_CONSTANTS.DFS_SERVER_ADDR)) == null) || !RegisterWithMaster())
        {
            System.out.println("Secondary Node CANNOT reach Master Node.");
            System.exit(DFS_CONSTANTS.SUCCESS);
        }
        ListenForPersistance();
    }

    //Register Secondary Master Node with Main Master Server
    public static boolean RegisterWithMaster()
    {
        System.out.println("Server IP : " + DFS_Globals.server_addr);
        //Creating Socket with Master Node Address stored
        pt = new PacketTransfer(DFS_Globals.server_addr, DFS_CONSTANTS.MN_MISC_LISTEN_PORT);

        String[] tempArgs = new String[DFS_CONSTANTS.ONE];

        Packet responsePacket;
        Packet requestPacket = new Packet();

        try {
            //Getting the Local Address of Secondary Master Node
            tempArgs[DFS_CONSTANTS.ZERO] = InetAddress.getLocalHost().getHostAddress();
            System.out.println("SEC  " + tempArgs[0]);

            requestPacket.command = DFS_CONSTANTS.ADD_SEC_MN;
            requestPacket.arguments = tempArgs;

            //REGISTER WITH MASTER NODE
            pt.sendPacket(requestPacket);

            //Getting Response from MASTER NODE
            responsePacket = pt.receivePacket();

            //MASTER NODE cannot be reached
            if (responsePacket == null || responsePacket.response_code != DFS_CONSTANTS.OK)
                return false;

            persistentSocket = pt.connectSocket;
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //Function to Start Listening for Persistent Metadata
    public static void ListenForPersistance()
    {
        try
        {
            persistentSocket.setSoTimeout(2 * DFS_CONSTANTS.SLEEP_TIME);
            //Expect
            while (DFS_Globals.is_secondary_on)
            {
                ois = new ObjectInputStream(persistentSocket.getInputStream());
                DFS_Globals.global_client_list = (HashMap<String, ClientWrapper>) ois.readObject();
                DFS_Globals.dn_q = (PriorityBlockingQueue<StorageNode>)ois.readObject();

                //ftp.save_file(persistentSocket, file_path);

                /* create the request and response packet packet */
                //pack = pt.receivePacket();
                pack = new Packet();
                pack.response_code = DFS_CONSTANTS.OK;

                if(!create_and_update_pers_md())
                    pack.response_code = DFS_CONSTANTS.FAILURE;
                System.out.println("Successfully backed up file......");
                pt.sendPacket(pack);
            }
        }
        catch (EOFException ex)
        {
            ex.printStackTrace();
            System.out.println("Detected a Main Node Failure.Taking over....");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        finally {
            try {
                persistentSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean create_and_update_pers_md()
    {
        try
        {
            fos = new FileOutputStream(file_path);
            os = new ObjectOutputStream(fos);

            os.writeObject(DFS_Globals.global_client_list);
            os.writeObject(DFS_Globals.dn_q);
            os.flush();
            return true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
