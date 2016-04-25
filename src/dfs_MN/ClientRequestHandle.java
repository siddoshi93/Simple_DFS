package dfs_MN;

import dfs_api.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* Program specific import */

/**
 * Created by Abhishek on 4/24/2016.
 */
//This is a Client Thread, which is created for EVERY client request handled
public class ClientRequestHandle implements Runnable{
	private Socket client_socket;
	private String uuid;
	
	public ClientRequestHandle(Socket soc,String uuid) {
		this.client_socket = soc;
		this.uuid = uuid;
	}
	@Override
	public void run()
	{
		ObjectInputStream ois;
		ClientRequestPacket req_packet;
		try
		{
			ois = new ObjectInputStream(client_socket.getInputStream());
			req_packet = (ClientRequestPacket)ois.readObject();
			if(req_packet == null)
				System.out.println("Handle NULL case");
			handle_command(req_packet);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Main_Node_Server.remove_active_request(uuid);
		}
	}

	public void handle_command(ClientRequestPacket req_packet)
	{
		switch (req_packet.command)
		{
			case DFS_CONSTANTS.REGISTER:
				ClientWrapper cw = new ClientWrapper(req_packet.client_uuid);
				DFS_Globals.global_client_list.put(req_packet.client_uuid,cw);
				break;
			case DFS_CONSTANTS.LOGIN:
				/* Validate the username requested */
				if(DFS_Globals.global_client_list.get(req_packet.client_uuid);
				break;
		}
	}

}
