package dfs_api;

import java.util.HashMap;

public class DFS_Globals
{
	//Flag to tell server to keep running for requests
	public static boolean is_on = true;

    //Global HashMap to maintain Client information
    public static HashMap<String, ClientWrapper> global_client_list;
}
