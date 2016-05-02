package dfs_MN;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.*;


/* Program specific imports */
import dfs_api.*;

/**
 * Created by Abhishek on 4/24/2016.
 */
public class Main_Node_Server 
{
	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("Please bring up the Server as below.");
			System.out.println("MN_SERVER <MODE>");
			System.exit(DFS_CONSTANTS.SUCCESS);
		}

		if((DFS_Globals.base_path = System.getenv(DFS_CONSTANTS.base_path)) == null)
		{
			System.out.print("Please set the Base path of SDFS:" + DFS_CONSTANTS.base_path);
			System.exit(DFS_CONSTANTS.SUCCESS);
		}

		DFS_Globals.sdfs_path = DFS_Globals.base_path + DFS_Globals.sdfs_path;
		/* Create the directory if not present */
		File dir = new File(DFS_Globals.sdfs_path);
		if(!(dir.exists() && dir.isDirectory()))
		{
			System.out.println("Creating Directory......");
			dir.mkdir();
		}

		switch (args[DFS_CONSTANTS.ZERO])
		{
			case DFS_CONSTANTS.PM:
				DFS_Globals.mn_mode_ind = DFS_CONSTANTS.PM;
				break;
			case DFS_CONSTANTS.SM:
				DFS_Globals.mn_mode_ind = DFS_CONSTANTS.SM;
				MN_Server_SM.start_sm_server(); /* Start Backup server */
				System.out.println("Changing mode to primary.....");
				break;
			default:
				System.out.println("Please pass either PM or SM as mode");
		}
		MN_Server_PM.start_pm_server(); /* Start Server in Primary mode */
	}
}
