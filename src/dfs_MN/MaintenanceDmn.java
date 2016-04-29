package dfs_MN;

import dfs_api.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
    boolean is_alive = false;

    @Override
    public void run()
    {
        System.out.println("I am Up for Maintenance Boys....");
        try
        {
            dn_list_iterator = DFS_Globals.dn_q.iterator();
            while (dn_list_iterator.hasNext())
            {
                sn = dn_list_iterator.next();
                System.out.println("STORAGE DT : " + sn.IPAddr + ":" + sn.Size);
                //ping_server(sn);
                is_alive = InetAddress.getByName(sn.IPAddr).isReachable(DFS_CONSTANTS.TIMEOUT);

                if (is_alive)
                {
                    System.out.println("STORAGE DT : " + sn.DataNodeID + ":" + is_alive);
                }
                else
                {
                    System.out.println("STORAGE DT : " + sn.DataNodeID + ":" + is_alive);
                    /* Remove this listing from the PQ */
                    DFS_Globals.dn_q.remove(sn);
                }
            }
            Thread.sleep(DFS_CONSTANTS.SLEEP_TIME);
        }
        catch (Exception e)
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
