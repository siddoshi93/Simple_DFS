package dfs_CL;

import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by abhishek on 4/24/16.
 */
public class ClientAPI
{
    public static boolean create_session_file(String user_name)
    {
        if(!check_and_create_dir())
        {
            System.out.println("Failed in CCD");
            return false;
        }
        try(PrintWriter out = new PrintWriter(new FileWriter(new File(DFS_CONSTANTS.user_name_file),false)))
        {
            out.print(user_name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean check_and_create_dir()
    {
        File dir = new File(DFS_CONSTANTS.sdfs_path);
        if(!(dir.exists() && dir.isDirectory()))
        {
            System.out.println("Creating Directory......");
            return dir.mkdir();
        }
        return true;
    }

    public static String getServerAddress()
    {
        String server_address;
        if((server_address = System.getenv(DFS_CONSTANTS.DFS_SERVER_ADDR)) != null)
            return server_address;
        else
            return null;
    }

    public static void send_request(Socket client_socket, ClientRequestPacket req_packet)
    {
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(client_socket.getOutputStream());
            oos.writeObject(req_packet);
            oos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ClientResponsePacket recv_response(Socket client_socket)
    {
        ObjectInputStream ois = null;
        ClientResponsePacket res_packet = null;
        try
        {
            /* Wait for the response packet from the server */
            ois = new ObjectInputStream(client_socket.getInputStream());
            res_packet = (ClientResponsePacket) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return res_packet;
        }
    }

    public static String getUserName()
    {
        /* Check if the file exists or not */
        Scanner cin = null;
        File username = new File(DFS_CONSTANTS.user_name_file);
        if(username.exists())
        {
            try {
                cin = new Scanner(username);
                return cin.next();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                cin.close();
            }
        }
        return null;
    }

    public static ClientRequestPacket createRequestPacket(int command)
    {
        ClientRequestPacket req_packet;
        String username = ClientAPI.getUserName();
        req_packet = new ClientRequestPacket();
        req_packet.client_uuid = username;
        req_packet.command = command;
        return req_packet;
    }
}
