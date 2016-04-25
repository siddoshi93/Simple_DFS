package dfs_api;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class DFS_CONSTANTS 
{
	/* PORT CONSTANTS */
	public final static int MN_LISTEN_PORT = 7091;
	public final static int CL_LISTEN_PORT = 8090;
	public final static int DN_LISTEN_PORT = 9090;

	/* MISC CONSTANTS */
	public final static int REQUEST_BACK_LOG = 25; /* MN Queue length */
	public final static int NUM_OF_WORKERS = 20;

	/* RETURN TYPE CONSTANTS */
	public final static int SUCCESS = 0; /* SUCCESS RETURN FOR FUNCTION */
	public final static int FAILURE = -1; /* FAILURE RETURN FOR FUNCTION */
	public final static int OK = 0;

	/* CONSTANTS FOR TYPE OF CLIENT REQUESTS i.e. commands*/
	public final static int REGISTER = 11;
	public final static int LOGIN = 12;
	public final static int LS = 13;
	public final static int MKDIR = 14;
	public final static int RM = 15;

	/* Environment variable for server address */
	public final static String DFS_SERVER_ADDR = "DFS_SERVER_ADDR";
}
