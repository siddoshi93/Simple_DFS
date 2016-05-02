package dfs_api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Wrapper used to Send/Receive objects of class "Packet"
 * @author Anas
 */
public class PacketTransfer {

    public Socket connectSocket;     //Socket Object for 1 Send-Receive Operation

    public PacketTransfer(String IPAddr, int Port)
    {
        try {
            connectSocket = new Socket(IPAddr, Port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public PacketTransfer(Socket connect)
    {
        this.connectSocket = connect;
    }

    @Override
    protected void finalize() throws Throwable {

        try {
            connectSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        super.finalize();
    }

    /**
     * Sends the Packet
     * @param req_packet
     */
    public void sendPacket(Packet req_packet)
    {
        ObjectOutputStream oos = null;

        try
        {
            oos = new ObjectOutputStream(connectSocket.getOutputStream());
            oos.writeObject(req_packet);
            oos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Returns a "Packet" instance
     * @return
     */
    public Packet receivePacket()
    {
        ObjectInputStream ois = null;
        Packet res_packet = null;
        try
        {
            /* Wait for the response packet from the server */
            ois = new ObjectInputStream(connectSocket.getInputStream());
            res_packet = (Packet) ois.readObject();
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
