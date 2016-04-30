package dfs_MN;

import dfs_api.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by abhishek on 4/30/16.
 */
public class MN_MiscDmn implements Runnable
{
    private ServerSocket listen = null;
    private Socket request = null;
    private Packet req_packet;
    private Packet res_packet;

    public MN_MiscDmn() throws IOException
    {
        listen = new ServerSocket(DFS_CONSTANTS.MN_MISC_LISTEN_PORT);
    }

    @Override
    public void run()
    {
        try
        {
            request = listen.accept();
            handle_request();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public int add_dn(StorageNode sn)
    {
        if(!DFS_Globals.dn_q.add(sn)) {
            System.out.println("Unsucessfull addition of Storage");
            return DFS_CONSTANTS.FAILURE;
        }
        else {
            System.out.println("Successfull addition of Storage");
            return DFS_CONSTANTS.OK;
        }
    }

    public void handle_request()
    {
        PacketTransfer pt = new PacketTransfer(request);
        req_packet = pt.receivePacket();
        res_packet = new Packet();

        switch (req_packet.command)
        {
            case DFS_CONSTANTS.ADD_DN:
                if(req_packet.dn_list == null)
                {
                    System.out.println("Improper DataRecieved from DN");
                    return;
                }
                res_packet.response_code = add_dn(req_packet.dn_list.get(DFS_CONSTANTS.ZERO));
                pt.sendPacket(res_packet);
                break;
            default:
                System.out.println("Unknown Command Recieved");
        }

    }
}
