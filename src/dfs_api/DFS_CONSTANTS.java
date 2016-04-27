package dfs_api;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class DFS_CONSTANTS 
{
	/* NUMBERS */
	public final static int ZERO = 0;
	public final static int ONE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int PQ_SIZE = 10;

	/* PORT CONSTANTS */
	public final static int MN_LISTEN_PORT = 7091;
	public final static int CL_LISTEN_PORT = 8090;
	public final static int DN_LISTEN_PORT = 9090;

	/* Data Node Constants */
	public final static String storage_path = "/home/abhishek/";
	public final static short DATA_PACKET_SIZE = 4096;

	/* MISC CONSTANTS */
	public final static int REQUEST_BACK_LOG = 25; /* MN Queue length */
	public final static int NUM_OF_WORKERS = 20;

	/* RETURN TYPE CONSTANTS */
	public final static int SUCCESS = 0; /* SUCCESS RETURN FOR FUNCTION */
	public final static int FAILURE = -1; /* FAILURE RETURN FOR FUNCTION */
	public final static int OK = 0;
	public final static int AUTH_FAILED = 3;
	public final static int INVALID_CMD = 4;

	/* CONSTANTS FOR TYPE OF CLIENT REQUESTS i.e. commands*/
	public final static int REGISTER = 11;
	public final static int LOGIN = 12;
	public final static int LS = 13;
	public final static int MKDIR = 14;
	public final static int RM = 15;
	public final static int GET = 16;
	public final static int PUT = 17;
	public final static int CD = 18;
	public final static int UPDATE = 19;
	public final static String CURRENT_DIRECTORY = ".";
	public final static String repl = "REPL";

	/* Environment variable for server address */
	public final static String DFS_SERVER_ADDR = "DFS_SERVER_ADDR";

	/* File Path */
	public final static String user_name_file = "/home/abhishek/.sdfs/sdfs_username";
	public final static String sdfs_path = "/home/abhishek/.sdfs/";
	public final static String dn_list = "dn_config.csv";
}
