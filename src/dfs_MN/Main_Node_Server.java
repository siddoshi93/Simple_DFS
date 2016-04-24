package dfs_MN;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/* Program specific imports */
import dfs_api.SynList;
import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

public class Main_Node_Server 
{
	private static ExecutorService workers;
	private static ConcurrentHashMap<String,ClientRequestHandle> active_client_list; /* List which maintains the current active client in the system */
	private static ServerSocket client_request; /* Socket which listens for client request */
	private static InetAddress hostAddress;
	private static ClientRequestHandle curr_req;
	private static String new_uuid;
	
	public static void main(String[] args)
	{
		try
		{
			/* Wait for a connection so that it can be served in a thread */
			setUpMN(); /* Set up the server */
			while(DFS_Globals.is_on)
			{
				/* Generate a random UUID for identification */
				new_uuid = UUID.randomUUID().toString();
				curr_req = new ClientRequestHandle(client_request.accept(),new_uuid); /* Listen to client request and assign the request to a worker thread */
				//System.out.println("Got connection : ");
				active_client_list.put(new_uuid,curr_req); /* Add the client to the end of the list */

				workers.submit(curr_req);
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
		try
		{
			client_request.close(); /* Close the server socket connection */
			workers.shutdown();
			workers.awaitTermination(1, TimeUnit.MINUTES);
			/* Close all the open connection */
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static boolean remove_active_request(String uuid)
	{
		return (active_client_list.remove(uuid) != null);

	}
	
	public static void setUpMN() throws IOException
	{		
		hostAddress = InetAddress.getLocalHost();  /* Get the host address */
		client_request = new ServerSocket(DFS_CONSTANTS.MN_LISTEN_PORT,DFS_CONSTANTS.REQUEST_BACK_LOG/*,hostAddress*/);
		active_client_list = new ConcurrentHashMap();
		workers = Executors.newFixedThreadPool(DFS_CONSTANTS.NUM_OF_WORKERS);
	}
}
