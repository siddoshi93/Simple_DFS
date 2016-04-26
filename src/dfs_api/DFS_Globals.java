package dfs_api;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class DFS_Globals
{
	//Flag to tell server to keep running for requests
	public static boolean is_MN_on = true;
    public static boolean is_DN_on = true;

    //Global HashMap to maintain Client information
    public static HashMap<String, ClientWrapper> global_client_list;

    /* HashMap for maintaining the client data*/
    public static HashMap<String, ClientMetaData> client_data;

    /* Maintain free port list for DataNode */
    public static LinkedList<Integer> free_port_list;
}