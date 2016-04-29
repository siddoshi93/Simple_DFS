package dfs_api;

import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by abhishek on 4/30/16.
 */
public class AliveServer implements Runnable
{
    ServerSocket ping_server = null;

    public AliveServer()
    {
        try
        {
            ping_server = new ServerSocket(DFS_CONSTANTS.ALIVE_LISTEN_PORT);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean isSetUp()
    {
        return ping_server != null;
    }

    @Override
    public void run()
    {
        try
        {
            while (DFS_Globals.is_alive_server_on)
            {
                ping_server.accept().close();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try {
                ping_server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
