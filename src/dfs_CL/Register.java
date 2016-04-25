package dfs_CL;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* Program specific imports */
import dfs_api.ClientRequestPacket;
import dfs_api.DFS_CONSTANTS;

/**
 * Created by abhishek on 4/24/16.
 */
public class Register {
    public static void main(String arg[])
    {
        Socket connect = null;
        String username;
        ClientRequestPacket packet;
        ObjectOutputStream oos = null;
        ObjectInputStream  ois = null;

        if(arg.length != 2)
        {
            System.out.println("Please call with a argument as below");
            System.out.println("Usage <Register USER_NAME>");
            System.exit(DFS_CONSTANTS.SUCCESS);
        }

        username = arg[1];
        try
        {
            connect = new Socket("127.0.0.1",DFS_CONSTANTS.MN_LISTEN_PORT);
            /* Send the request to client for registration */
            packet = new ClientRequestPacket();
            packet.command = DFS_CONSTANTS.REGISTER;
            packet.client_uuid = username;

            /* Send the packet to the  server */
            oos = new ObjectOutputStream(connect.getOutputStream());
            oos.writeObject(packet);

            /* Wait for the response from the server */
            
        }
        catch (IOException e)
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