package dfs_DN;

import dfs_api.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by abhishek on 4/30/16.
 */
public class DN_MiscDmn implements Runnable
{
    private ServerSocket listen = null;
    private Socket request = null;
    private Packet req_packet;
    private Packet res_packet;

    public DN_MiscDmn() throws IOException
    {
        listen = new ServerSocket(DFS_CONSTANTS.DN_MISC_LISTEN_PORT);
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

    public int change_mn(String mn_addr)
    {
        DFS_Globals.server_addr = mn_addr;
        return DFS_CONSTANTS.OK;
    }

    public void handle_request()
    {
        PacketTransfer pt = new PacketTransfer(request);
        req_packet = pt.receivePacket();
        res_packet = new Packet();

        switch (req_packet.command)
        {
            case DFS_CONSTANTS.CHANGE_MN:
                if(req_packet.mn_addr == null)
                {
                    System.out.println("Improper MainNODE Recieved..Shutting down DN");
                    DFS_Globals.is_DN_on = false;
                    res_packet.response_code = DFS_CONSTANTS.FAILURE;
                    pt.sendPacket(res_packet);
                    return;
                }
                res_packet.response_code = change_mn(req_packet.mn_addr);
                pt.sendPacket(res_packet);
                break;
            default:
                System.out.println("Unknown Command Recieved");
        }

    }
}
