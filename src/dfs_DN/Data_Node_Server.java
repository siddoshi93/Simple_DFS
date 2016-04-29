package dfs_DN;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by abhishek on 4/26/16.
 */

/* Program specific imports */
import dfs_MN.ClientRequestHandle;
import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

public class Data_Node_Server
{
    private static ExecutorService workers;

    /* List which maintains the current active client REQUESTS in the Server */
    private static ConcurrentHashMap<String,RequestProcessor> active_request_list;

    private static ServerSocket request; /* Server Socket which listens for client request */
    private static InetAddress hostAddress;
    private static RequestProcessor curr_req;
    private static String new_uuid;

    public static void main(String[] args)
    {
        try
        {
			/* Wait for a connection so that it can be served in a thread */
            setUpDN(); /* Set up the server */

            while(DFS_Globals.is_DN_on)
            {
                /* Generate a random UUID for Every new Client Request to be used as a Key in the HashMap */
                new_uuid = UUID.randomUUID().toString();

                curr_req = new RequestProcessor(request.accept(),new_uuid); /* Listen to request and assign the request to a worker thread */

                active_request_list.put(new_uuid,curr_req); /* Add the client to the end of the list */

                workers.submit(curr_req);
            }

        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
            cleanUpDN();
        }
    }

    public static void cleanUpDN()
    {
        try
        {
            request.close(); /* Close the server socket connection */
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
        return (active_request_list.remove(uuid) != null);

    }

    public static void setUpDN() throws IOException
    {
        hostAddress = InetAddress.getLocalHost();  /* Get the host address */
        request = new ServerSocket(DFS_CONSTANTS.DN_LISTEN_PORT,DFS_CONSTANTS.REQUEST_BACK_LOG/*,hostAddress*/);
        active_request_list = new ConcurrentHashMap<String, RequestProcessor>();
        workers = Executors.newFixedThreadPool(DFS_CONSTANTS.NUM_OF_WORKERS);
        DFS_Globals.client_data = new HashMap();

        if((DFS_Globals.server_addr = System.getenv(DFS_CONSTANTS.DFS_SERVER_ADDR)) == null)
        {
            System.out.println("Please set the environment variable for Server Address");
            System.exit(DFS_CONSTANTS.SUCCESS);
        }
    }
}
