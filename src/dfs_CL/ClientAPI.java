package dfs_CL;

import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;
import dfs_api.FileTransfer;

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
        if(!FileTransfer.check_and_create_dir(DFS_CONSTANTS.sdfs_path))
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

    public static boolean getFiles(ClientResponsePacket res_packet,String path)
    {
        /* This program connects to the Data Node and transfer it to the local system */
        ClientRequestPacket req_packet = new ClientRequestPacket();
        ClientResponsePacket dn_res_packet = new ClientResponsePacket();
        String dn_ip = res_packet.dn_list.get(DFS_CONSTANTS.ONE).IPAddr;
        Socket connect = null;
        FileTransfer ftp = new FileTransfer();

        req_packet.command = DFS_CONSTANTS.GET;
        req_packet.client_uuid = ClientAPI.getUserName();
        req_packet.file_name = res_packet.file_name;
        req_packet.file_size = res_packet.file_size;

        try
        {
            connect = new Socket(dn_ip,DFS_CONSTANTS.DN_LISTEN_PORT);
            send_request(connect,req_packet);
            dn_res_packet = ClientAPI.recv_response(connect);

            if (dn_res_packet !=null && dn_res_packet.response_code == DFS_CONSTANTS.OK)
            {
                /* Start Sending the file */
                ftp.save_file(connect,(path + req_packet.file_name));
            }
            else
            {
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
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

    public static boolean sendFiles(ClientResponsePacket res_packet,String path)
    {
        /* This program connects to the Data Node and transfer it to the local system */
        ClientRequestPacket req_packet = new ClientRequestPacket();
        ClientResponsePacket dn_res_packet = new ClientResponsePacket();
        String dn_ip = res_packet.dn_list.get(DFS_CONSTANTS.ZERO).IPAddr;
        Socket connect = null;
        FileTransfer ftp = new FileTransfer();

        req_packet.command = DFS_CONSTANTS.PUT;
        req_packet.client_uuid = ClientAPI.getUserName();
        req_packet.file_name = res_packet.file_name;
        req_packet.file_size = res_packet.file_size;
        req_packet.arguments = res_packet.arguments;
        req_packet.dn_list = res_packet.dn_list;

        try
        {
            connect = new Socket(dn_ip,DFS_CONSTANTS.DN_LISTEN_PORT);
            send_request(connect,req_packet);
            System.out.println("In Send File wit dn ip :" + dn_ip);
            dn_res_packet = ClientAPI.recv_response(connect);
            if (dn_res_packet !=null && dn_res_packet.response_code == DFS_CONSTANTS.OK)
            {
                /* Start Sending the file */
                System.out.println("Starting sending file....");
                ftp.send_file(connect,path);
            }
            else
            {
                System.out.println("Recv null from DN response : " + dn_res_packet == null);
                return false;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
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

    public static boolean validate_file(String file)
    {
        File f = new File(file);
        System.out.println("validate file : " + file + " IsDir : " + f.exists());
        if(f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }

    public static double getFileSize(String file)
    {
        return new File(file).length();
    }
}
