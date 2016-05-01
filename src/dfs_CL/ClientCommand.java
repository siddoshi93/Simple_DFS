package dfs_CL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/* Program specific imports */
import dfs_api.*;

import javax.xml.ws.handler.MessageContext;

/**
 * Created by abhishek on 4/24/16.
 */
public class ClientCommand
{
    static Socket connect = null;
    static String server_ip;
    static ClientRequestPacket req_packet;
    static ClientResponsePacket res_packet;
    static ObjectInputStream  ois = null;

    public static boolean RegLogActivity(String username,int command,ClientResponsePacket res_packet)
    {
        server_ip = ClientAPI.getServerAddress();
        if(server_ip == null || username == null)
        {
            System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
            return false;
        }
        try
        {
            System.out.println("Server Ip : " + server_ip);
            connect = new Socket(server_ip,DFS_CONSTANTS.MN_LISTEN_PORT);

            /* Set the request packet for Server with registration */
            req_packet = new ClientRequestPacket();
            req_packet.command = command;
            req_packet.client_uuid = username;

            /* Send the packet to the  server */
            ClientAPI.send_request(connect,req_packet);

            System.out.println("Sending the request");

            /* Wait for the response packet from the server */
            ois = new ObjectInputStream(connect.getInputStream());
            res_packet = (ClientResponsePacket)ois.readObject();

            if(res_packet.response_code == DFS_CONSTANTS.OK)
            {
                //Setting server_addr => IP Of Master Node
                DFS_Globals.server_addr = System.getenv(DFS_CONSTANTS.DFS_SERVER_ADDR);

                //Setting sec_mn_ip_addr => IP of Secondary Master Node IF Provided by Master Node
                if (res_packet.arguments.length > 0)
                {
                    DFS_Globals.sec_mn_ip_addr = res_packet.arguments[0];
                }

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
        String[] arg_list; /* Argument list passed as String */

        if (arg.length != 2) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS MkDir DIRNAME>");
            return false;
        }

        server_ip = ClientAPI.getServerAddress();
        if(server_ip == null)
        {
            System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
            return false;
        }
        req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.MKDIR);
        arg_list = new String[DFS_CONSTANTS.ONE];
        arg_list[0] = arg[1];
        req_packet.arguments = arg_list;
        try
        {
            connect = new Socket(server_ip, DFS_CONSTANTS.MN_LISTEN_PORT);

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
        finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean Ls(String[] arg)
    {
        String arg_list[] = new String[DFS_CONSTANTS.ONE];
        if (arg.length  < 1) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS LS <PATH>");
            return false;
        }
        try
        {
            server_ip = ClientAPI.getServerAddress();
            if(server_ip == null)
            {
                System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
                return false;
            }
            req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.LS);
            connect = new Socket(server_ip,DFS_CONSTANTS.MN_LISTEN_PORT);
            if(arg.length == 1) /* Mo Parameter to LS */
            {
                arg_list[0] = DFS_CONSTANTS.CURRENT_DIRECTORY;
            }
            else
            {
                arg_list[0] = arg[1];
            }
            req_packet.arguments = arg_list;
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
        finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean Cd(String[] arg)
    {
        String[] arg_list; /* Argument list passed as String */
        if (arg.length != 2) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS GET FILE_NAME>");
            return false;
        }

        server_ip = ClientAPI.getServerAddress();
        if(server_ip == null)
        {
            System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
            return false;
        }
        req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.CD);
        arg_list = new String[DFS_CONSTANTS.ONE];
        arg_list[0] = arg[1];
        req_packet.arguments = arg_list;

        try
        {
            connect = new Socket(server_ip, DFS_CONSTANTS.MN_LISTEN_PORT);

            ClientAPI.send_request(connect, req_packet);
            res_packet = ClientAPI.recv_response(connect);
            if(res_packet != null && res_packet.response_code == DFS_CONSTANTS.OK)
            {
                System.out.println("Successfully performed the operation.....");
            }
            else
            {
                System.out.println("Something went wrong in performing the address!!!!");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean Get(String[] arg)
    {
        String[] arg_list; /* Argument list passed as String */
        if (arg.length != 2) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS GET SDFS_SOURCE>");
            return false;
        }

        server_ip = ClientAPI.getServerAddress();
        if(server_ip == null)
        {
            System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
            return false;
        }
        req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.GET);
        arg_list = new String[DFS_CONSTANTS.ONE];
        arg_list[0] = arg[1];
        //arg_list[1] = arg[2];
        req_packet.file_name = arg[1].split("/")[arg[1].split("/").length - 1];
        req_packet.arguments = arg_list;
        try
        {
            connect = new Socket(server_ip, DFS_CONSTANTS.MN_LISTEN_PORT);

            ClientAPI.send_request(connect, req_packet);
            res_packet = ClientAPI.recv_response(connect);
            if(res_packet != null && res_packet.response_code == DFS_CONSTANTS.OK)
            {
                System.out.println("Got the IPs of DN.Connecting for getting data..... : " + res_packet.dn_list.size());
                System.out.println("IP1 : " + res_packet.dn_list.get(DFS_CONSTANTS.ZERO));
                connect.close(); /* Close the connection with the server */
                res_packet.file_name = req_packet.file_name;
                ClientAPI.getFiles(res_packet,arg[1]);
            }
            else
            {
                System.out.println("Something went wrong in getting DN address or with file!!!!");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean Put(String[] arg)
    {
        String[] arg_list; /* Argument list passed as String */
        String path = null;

        if (! ( arg.length ==3  || (arg.length == 4 && arg[1].equalsIgnoreCase(DFS_CONSTANTS.repl))) ) {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <SDFS PUT SOURCE DEST>");
            return false;
        }

        req_packet = ClientAPI.createRequestPacket(DFS_CONSTANTS.PUT);
        if(arg.length == 3) /* No Replication passed */
        {
            if (!ClientAPI.validate_file(arg[1])) {
                System.out.println("Invalid File...");
                return false;
            }
            arg_list = new String[DFS_CONSTANTS.TWO];
            arg_list[0] = arg[1];
            arg_list[1] = arg[2];
            req_packet.replicate_ind = false;
            req_packet.file_name = arg[1].split("/")[arg[1].split("/").length - 1];
            req_packet.file_size = ClientAPI.getFileSize(arg[1]);
            path = arg[1];
        }
        else
        {
            if (!ClientAPI.validate_file(arg[2])) {
                System.out.println("Invalid File...");
                return false;
            }
            arg_list = new String[DFS_CONSTANTS.THREE];
            req_packet.replicate_ind = true;
            arg_list[0] = arg[2];
            arg_list[1] = arg[3];
            req_packet.file_name = arg[2].split("/")[arg[2].split("/").length - 1];
            req_packet.file_size = ClientAPI.getFileSize(arg[2]);
            path = arg[2];
        }

        server_ip = ClientAPI.getServerAddress();
        if(server_ip == null)
        {
            System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
            return false;
        }

        req_packet.arguments = arg_list;
        try
        {
            connect = new Socket(server_ip, DFS_CONSTANTS.MN_LISTEN_PORT);
            ClientAPI.send_request(connect, req_packet);
            res_packet = ClientAPI.recv_response(connect);
            if(res_packet != null && res_packet.response_code == DFS_CONSTANTS.OK)
            {
                connect.close(); /* Close the connection with the server */
                res_packet.arguments = req_packet.arguments; /* Data need to send to DN */
                res_packet.file_name = req_packet.file_name;
                System.out.println("Replication ind : " + res_packet.replicate_ind);
                if(res_packet.replicate_ind)
                {
                    System.out.println("DN IP1 : " + res_packet.dn_list.get(DFS_CONSTANTS.ZERO).IPAddr);
                    System.out.println("DN IP2 : " + res_packet.dn_list.get(DFS_CONSTANTS.ONE).IPAddr);
                }
                ClientAPI.sendFiles(res_packet,path);
            }
            else
            {
                System.out.println("Something went wrong in getting DN address or with file!!!!");
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}