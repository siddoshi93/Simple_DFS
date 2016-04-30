package dfs_api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Anas on 4/30/2016.
 */
public class PacketTransfer {

    Socket connect;     //Socket Object for 1 Send-Receive Operation

    public PacketTransfer(String IPAddr, int Port)
    {
        try {
            connect = new Socket(IPAddr, Port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {

        try {
            connect.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        super.finalize();
    }

    //Sending A packet
    public void sendPacket(String IPAddr, int Port, ClientRequestPacket req_packet)
    {
        ObjectOutputStream oos = null;

        try
        {
            oos = new ObjectOutputStream(connect.getOutputStream());
            oos.writeObject(req_packet);
            oos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ClientResponsePacket receivePacket()
    {
        ObjectInputStream ois = null;
        ClientResponsePacket res_packet = null;
        try
        {
            /* Wait for the response packet from the server */
            ois = new ObjectInputStream(connect.getInputStream());
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


}
