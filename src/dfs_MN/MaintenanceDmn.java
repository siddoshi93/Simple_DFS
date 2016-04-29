package dfs_MN;

import dfs_api.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;

/**
 * Created by abhishek on 4/29/16.
 */
public class MaintenanceDmn implements Runnable
{
    private Socket connect = null;
    private StorageNode sn = null;
    private Iterator<StorageNode> dn_list_iterator = null;
    private ClientRequestPacket req_packet = null;
    private ClientResponsePacket res_packet = null;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    @Override
    public void run()
    {
        System.out.println("I am Up for Maintenance Boys....");
        dn_list_iterator = DFS_Globals.dn_q.iterator();
        while (dn_list_iterator.hasNext())
        {
            sn = dn_list_iterator.next();
            System.out.println("STORAGE DT : " + sn.IPAddr + ":" + sn.Size);
            ping_server(sn);
        }
        try
        {
            Thread.sleep(DFS_CONSTANTS.SLEEP_TIME);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void ping_server(StorageNode sn)
    {
        try
        {
            /* Forming the request packet */
            req_packet.command = DFS_CONSTANTS.IS_DN_ALIVE;
            req_packet.requestEntity = DFS_CONSTANTS.MN;

            connect = new Socket();
            connect.connect(new InetSocketAddress(sn.IPAddr,DFS_CONSTANTS.DN_LISTEN_PORT),DFS_CONSTANTS.TIMEOUT);

            /* Send the request packet */
            send_request();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            try {
                connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send_request()
    {
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
}
