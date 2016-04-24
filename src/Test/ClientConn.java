package Test;

import java.net.Socket;

import dfs_api.DFS_CONSTANTS;
// trial
public class ClientConn {
	private static Socket socket;
	public static void main(String arg[])
	{
		try
		{
			socket = new Socket("127.0.0.1",DFS_CONSTANTS.MN_LISTEN_PORT);			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}
