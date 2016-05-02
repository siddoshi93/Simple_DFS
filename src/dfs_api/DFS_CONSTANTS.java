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
	public final static int SLEEP_TIME = 3000;
	public final static int TIMEOUT = 5000;
	public final static int MASTER_PING_TIMEOUT = 3000;

	/* PORT CONSTANTS */
	public final static int MN_LISTEN_PORT = 7091;
	public final static int ALIVE_LISTEN_PORT = 8090;
	public final static int DN_LISTEN_PORT = 9090;
	public final static int DN_MISC_LISTEN_PORT = 9098;
	public final static int MN_MISC_LISTEN_PORT = 9099;

	/* Data Node Constants */
	public final static String storage_path = "/home/ubuntu/";
	public final static short DATA_PACKET_SIZE = 4096;

	/* MISC CONSTANTS */
	public final static int REQUEST_BACK_LOG = 25; /* MN Queue length */
	public final static int NUM_OF_WORKERS = 20; /* NUMBER OF EXECUTOR THREADS */
	public final static int NUM_OF_MN = 2; /* Number of back up node configured */

	/* Entity Type */
	public final static int MN = 55;
	public final static int DN = 56;
	public final static int CL = 57;

	/* RETURN TYPE CONSTANTS */
	public final static int SUCCESS = 0; /* SUCCESS RETURN FOR FUNCTION */
	public final static int FAILURE = -1; /* FAILURE RETURN FOR FUNCTION */
	public final static int OK = 0;
	public final static int AUTH_FAILED = 3;
	public final static int INVALID_CMD = 4;
	public final static int INVALID_SIZE = -1;

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
	public final static int ADD_DN = 20;
	public final static int ADD_SEC_MN = 21;
	public final static int CHANGE_MN = 22;

	/* Environment variable for server address */
	public final static String DFS_SERVER_ADDR = "DFS_SERVER_ADDR";
	public final static String DFS_SECONDARY_SERVER_ADDR = "DFS_SECONDARY_SERVER_ADDR";
	public final static String PM = "PM";
	public final static String SM = "SM";

	/* File Path */
	public final static String base_path = "SDFS_BASE_PATH";
	public final static String sec_nm_data_file = "sec_nm_data";
	public final static String dn_list = "dn_config.csv";
	public final static String persistance_file = "persistance_file";

	/*Log Path and Constants*/
	public final static String logPath="";
	public final static String INFO="INFORMATION";
	public final static String FATAL="FATAL";
	public final static String WARN="WARNING";
	public final static String ERR="ERROR";
}
