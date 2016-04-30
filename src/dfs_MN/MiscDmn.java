package dfs_MN;

import dfs_api.DFS_CONSTANTS;

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

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
