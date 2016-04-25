package dfs_CL;

import dfs_api.DFS_CONSTANTS;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Created by abhishek on 4/24/16.
 */
public class ClientAPI
{
    public static boolean create_session_file(String user_name)
    {
        try(PrintWriter out = new PrintWriter("~/sdfs_username"))
        {
            out.print(user_name);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String getServerAddress()
    {
        String server_address;
        if((server_address = System.getenv(DFS_CONSTANTS.DFS_SERVER_ADDR)) != null)
            return server_address;
        else
            return null;
    }
}
