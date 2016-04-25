package dfs_CL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* Program specific imports */
import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;

/**
 * Created by abhishek on 4/24/16.
 */
public class ClientCommand
{
    public static boolean RegLogActivity(String username,int command,ClientResponsePacket res_packet)
    {
        Socket connect = null;
        String server_ip;
        ClientRequestPacket req_packet;
        ObjectInputStream  ois = null;

        server_ip = ClientAPI.getServerAddress();
        //if(server_ip == null || username == null)
          //  System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
        try
        {
            connect = new Socket("127.0.0.1",DFS_CONSTANTS.MN_LISTEN_PORT);

            /* Set the request packet for Server with registration */
            req_packet = new ClientRequestPacket();
            req_packet.command = command;
            req_packet.client_uuid = username;

            /* Send the packet to the  server */
            ClientAPI.send_request(connect,req_packet);
            System.out.println("Request Send : ");

            /* Wait for the response packet from the server */
            ois = new ObjectInputStream(connect.getInputStream());
            res_packet = (ClientResponsePacket)ois.readObject();

            if(res_packet.response_code == DFS_CONSTANTS.OK)
            {
                if(ClientAPI.create_session_file(username))
                {
                    System.out.println("Successfully Performed the Activity.......");
                }
                else
                {
                    System.out.println("Error while Creating session.....");
                    return false;
                }
            }
            else
            {
                System.out.println("Error while Performing the Activity.....");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                if(connect != null)
                    connect.close();
                if(ois != null)
                    ois.close();
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean Register(String[] arg)
    {
        ClientResponsePacket res_packet = null;
        String username;

        if(arg.length != 2)
        {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS Register USER_NAME>");
            return false;
        }
        username = arg[1];

        if(!RegLogActivity(username,DFS_CONSTANTS.REGISTER,res_packet))
            return false;
        else
            return true;
    }

    public static boolean Login(String[] arg)
    {
        ClientResponsePacket res_packet = null;
        String username;

        if(arg.length != 2)
        {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS Login USER_NAME>");
            return false;
        }

        username = arg[1];

        if(!RegLogActivity(username,DFS_CONSTANTS.LOGIN,res_packet))
            return false;
        else
            return true;
    }
}