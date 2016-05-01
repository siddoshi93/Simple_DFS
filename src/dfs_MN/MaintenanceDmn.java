package dfs_MN;

import dfs_api.*;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by abhishek on 4/29/16.
 */
public class MaintenanceDmn implements Runnable
{
    private Socket dn_connect = null;
    private StorageNode sn = null;
    private Iterator<StorageNode> dn_list_iterator = null;
    private FileOutputStream fos = null;
    private ObjectOutputStream os = null;

    @Override
    public void run()
    {
        try
        {
            while (DFS_Globals.is_maintenance_on)
            {
                System.out.println("I am Up for Maintenance Boys....");
                check_dn_status(); /* Check for aliveness of DN in the list */
                //if(create_and_update_pers_md())/* create or update the meta data persistance copy */
                //{
                    /* Send this new metadata to the secondary Main Node */
                //}
                Thread.sleep(DFS_CONSTANTS.SLEEP_TIME);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public boolean create_and_update_pers_md()
    {
        try
        {
            fos = new FileOutputStream(DFS_CONSTANTS.sdfs_path + DFS_CONSTANTS.persistance_file);
            os = new ObjectOutputStream(fos);

            os.writeObject(DFS_Globals.global_client_list);
            os.flush();
            return true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                os.close();
                fos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
    }

    public void check_dn_status()
    {
        if(DFS_Globals.dn_q == null)
            return;

        dn_list_iterator = DFS_Globals.dn_q.iterator();

        try{
        while (dn_list_iterator.hasNext()) {
            sn = dn_list_iterator.next();

            if (ping_server(sn)) {
                System.out.println("STORAGE DT : " + sn.DataNodeID + ": is up");
            } else {
                System.out.println("STORAGE DT : " + sn.DataNodeID + ": is down ");
                /* Remove this listing from the PQ */
                sn.Size = DFS_CONSTANTS.INVALID_SIZE;
                sn.isAlive = false;
            }
        }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean ping_server(StorageNode sn)
    {
        try
        {
            dn_connect = new Socket();
            dn_connect.connect(new InetSocketAddress(sn.IPAddr,DFS_CONSTANTS.ALIVE_LISTEN_PORT),DFS_CONSTANTS.TIMEOUT);
            /* Connection succesfull */
            return true;
        }
        catch (IOException ex)
        {
            return false;
        }
        finally {
            try {
                dn_connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
