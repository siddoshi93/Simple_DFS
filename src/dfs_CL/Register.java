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
public class Register {
    public static void main(String arg[])
    {
        Socket connect = null;
        String username,server_ip;
        ClientRequestPacket req_packet;
        ClientResponsePacket res_packet;
        ObjectOutputStream oos = null;
        ObjectInputStream  ois = null;

        if(arg.length != 2)
        {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <Register USER_NAME>");
            System.exit(DFS_CONSTANTS.SUCCESS);
        }

        username = arg[1];
        server_ip = ClientAPI.getServerAddress();
        if(server_ip == null || username == null)
            System.out.println("Please define DFS_SERVER_ADDR env variable or pass proper username");
        try
        {
            connect = new Socket(server_ip,DFS_CONSTANTS.MN_LISTEN_PORT);
            oos = new ObjectOutputStream(connect.getOutputStream());
            ois = new ObjectInputStream(connect.getInputStream());
            /* Send the request to client for registration */
            req_packet = new ClientRequestPacket();
            req_packet.command = DFS_CONSTANTS.REGISTER;
            req_packet.client_uuid = username;

            /* Send the packet to the  server */
            oos.writeObject(req_packet);

            /* Wait for the response packet from the server */
            res_packet = (ClientResponsePacket)ois.readObject();
            if(res_packet.response_code == DFS_CONSTANTS.OK)
            {
                if(ClientAPI.create_session_file(username))
                    System.out.println("Successfully Registered.......");
                else
                    System.out.println("Error while Creating session.....");
            }
            else
                System.out.println("Error while registering client with the server.....");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                if(connect != null)
                    connect.close();
                if(oos != null)
                    oos.close();
                if(ois != null)
                    ois.close();
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }
}