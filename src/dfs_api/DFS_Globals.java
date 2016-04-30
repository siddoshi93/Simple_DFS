package dfs_api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class DFS_Globals
{
	//Flag to tell server to keep running for requests
	public static boolean is_MN_on = true;
    public static boolean is_DN_on = true;
    public static boolean is_alive_server_on = true;

    //Global HashMap to maintain Client information
    public static HashMap<String, ClientWrapper> global_client_list;

    /* HashMap for maintaining the client data*/
    public static HashMap<String, ClientMetaData> client_data;

    /* List of data node to maintain */
    public static PriorityQueue<StorageNode> dn_q;

    /* Server address stored by DN */
    public static String server_addr;
}