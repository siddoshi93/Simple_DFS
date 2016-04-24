package dfs_MN;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;


/* Program specific imports */
import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

public class Main_Node_Server 
{
	private static ServerSocket client_request; /* Socket which listens for client request */
	private static InetAddress hostAddress;

	public static void main(String[] args)
	{
		try
		{
			/* Wait for a connection so that it can be served in a thread */
			setUpMN();
			while(DFS_Globals.is_on)
			{
				client_request.accept(); /* Listen to client request and assign the request to a worker thread */
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			cleanUpMN();
		}
	}
	
	public static void cleanUpMN()
	{
		
	}
	
	public static void setUpMN() throws IOException
	{
		client_request = new ServerSocket(DFS_CONSTANTS.MN_LISTEN_PORT);
		hostAddress = InetAddress.getLocalHost();
	}
}
