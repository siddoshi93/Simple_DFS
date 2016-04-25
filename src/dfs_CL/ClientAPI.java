package dfs_CL;

import dfs_api.ClientRequestPacket;
import dfs_api.DFS_CONSTANTS;

import java.io.*;
import java.net.Socket;

/**
 * Created by abhishek on 4/24/16.
 */
public class ClientAPI
{
    public static boolean create_session_file(String user_name)
    {
        try(PrintWriter out = new PrintWriter(new FileWriter(new File("/home/abhishek/sdfs_username"),false)))
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
}
