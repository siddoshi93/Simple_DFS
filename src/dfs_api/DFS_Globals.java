package dfs_api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Global Variables for the Application
 * @author Abhishek
 */
public class DFS_Globals
{
	//Flag to tell server to keep running for requests
	public static boolean is_MN_on = true;
    public static boolean is_DN_on = true;
    public static boolean is_alive_server_on = true;
    public static boolean is_maintenance_on = true;
    public static boolean is_misc_dmn_up = true;
    public static boolean synhronization_start = false;
    public static boolean is_secondary_on = true;
    public static String mn_mode_ind;

    /* Global HashMap to maintain Client information <MN> */
    public static HashMap<String, ClientWrapper> global_client_list;

    /* HashMap for maintaining the client data<DN>*/
    public static HashMap<String, ClientMetaData> client_data;

    /* List of data node to maintain <MN> */
    public static PriorityBlockingQueue<StorageNode> dn_q;

    /* Server address stored by DN */
    public static String server_addr = null;

    /* Server address of the Secondary Main Node */
    public static String sec_mn_ip_addr = null;

    /* Base Path */
    public static String base_path = null;
    public static String sdfs_path = ".sdfs/";
    public static String user_name_file = sdfs_path + "sdfs_username";
    public static String storage_path = null;
}