package dfs_MN;

import dfs_api.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Anas on 5/1/2016.
 */
public class MN_Server_SM {

    public static Socket persistentSocket = null;
    public static FileTransfer ftp;

    //Starting point for Secondary Server
    public static void start_sm_server()
    {
        if (!RegisterWithMaster())
        {
            System.out.println("Secondary Node CANNOT reach Master Node.");
            System.exit(0);
        }

        ListenForPersistance();
    }

    //Register Secondary Master Node with Main Master Server
    public static boolean RegisterWithMaster()
    {
        //Creating Socket with Master Node Address stored
        PacketTransfer packetTransfer = new PacketTransfer(DFS_CONSTANTS.DFS_SERVER_ADDR, DFS_CONSTANTS.MN_MISC_LISTEN_PORT);

        String[] tempArgs = new String[1];

        Packet responsePacket;
        Packet requestPacket = new Packet();

        try {
            //Getting the Local Address of Secondary Master Node
            tempArgs[0] = InetAddress.getLocalHost().getHostAddress();

            requestPacket.command = DFS_CONSTANTS.ADD_SEC_MN;
            requestPacket.arguments = tempArgs;

            //REGISTER WITH MASTER NODE
            packetTransfer.sendPacket(requestPacket);

            //Getting Response from MASTER NODE
            responsePacket = packetTransfer.receivePacket();

            //MASTER NODE cannot be reached
            if (responsePacket == null || responsePacket.response_code != DFS_CONSTANTS.OK)
                return false;

            persistentSocket = packetTransfer.connectSocket;
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //Function to Start Listening for Persistent Metadata
    public static void ListenForPersistance() {

        ftp = new FileTransfer();

        try {
            //Expect
            while (DFS_Globals.is_secondary_on)
            {
                persistentSocket.setSoTimeout(2 * DFS_CONSTANTS.SLEEP_TIME);
                ftp.save_file(persistentSocket, DFS_CONSTANTS.sdfs_path + DFS_CONSTANTS.persistance_file);
            }
        }
        catch (SocketException se)
        {
            //Take CONTROL AS MASTER NODE
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
