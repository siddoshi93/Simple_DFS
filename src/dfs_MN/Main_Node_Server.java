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
		}

		switch (args[DFS_CONSTANTS.ZERO])
		{
			case DFS_CONSTANTS.PM:
				MN_Server_PM.start_pm_server(); /* Start Server in Primary mode */
				break;
			case DFS_CONSTANTS.SM:
				break;
			default:
				System.out.println("Please pass either PM or SM as mode");
		}
	}
}
