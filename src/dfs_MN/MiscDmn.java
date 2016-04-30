package dfs_MN;

import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;
import dfs_api.PacketTransfer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by abhishek on 4/30/16.
 */
public class MiscDmn implements Runnable
{
    private ServerSocket listen = null;
    private Socket request = null;
    private ClientResponsePacket res_packet;

    public MiscDmn() throws IOException
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

    public void handle_request()
    {
        PacketTransfer pt = new PacketTransfer(request);
        res_packet = pt.receivePacket();

        switch (res_packet.command)
        {
            case DFS_CONSTANTS.ADD_DN:

                break;

        }

    }
}
