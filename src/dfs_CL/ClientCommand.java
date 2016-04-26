package dfs_CL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/* Program specific imports */
import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;
import dfs_api.TreeNode;

import javax.xml.ws.handler.MessageContext;

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

    public static boolean MkDir(String[] arg) {
        String[] arg_list;
        Socket connect = null;
        ClientRequestPacket req_packet;
        ClientResponsePacket res_packet;
        ObjectInputStream ois = null;

        if (arg.length != 2) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS MkDir DIRNAME>");
            return false;
        }

        req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.MKDIR);
        arg_list = new String[DFS_CONSTANTS.ONE];
        arg_list[0] = arg[1];
        req_packet.arguments = arg_list;
        try
        {
            connect = new Socket("127.0.0.1", DFS_CONSTANTS.MN_LISTEN_PORT);

            ClientAPI.send_request(connect, req_packet);
            res_packet = ClientAPI.recv_response(connect);
            if(res_packet != null && res_packet.response_code == DFS_CONSTANTS.OK)
            {
                System.out.println("MKDIR implemented successfully..");
            }
            else
            {
                System.out.println("MKDIR implemented unsuccessfully..");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean Ls(String[] arg)
    {
        ClientRequestPacket req_packet;
        ClientResponsePacket res_packet;
        Socket connect = null;
        if (arg.length != 1) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS LS>");
            return false;
        }
        try
        {
            req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.LS);
            connect = new Socket("127.0.0.1",DFS_CONSTANTS.MN_LISTEN_PORT);
            ClientAPI.send_request(connect,req_packet);
            res_packet = ClientAPI.recv_response(connect);

            if(res_packet != null && res_packet.response_code == DFS_CONSTANTS.OK)
            {
                for(TreeNode i:res_packet.curNode.children)
                {
                    System.out.println(i.NodeName);
                }
                System.out.println("LS Worked as expected");
            }
            else
            {
                System.out.println("LS not Worked as expected");
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
}