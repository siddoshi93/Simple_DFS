package dfs_MN;

import dfs_api.*;

import java.io.IOException;
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
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private ClientRequestPacket req_packet;

	public ClientRequestHandle(Socket soc,String uuid) {
		this.client_socket = soc;
		this.uuid = uuid;
	}
	@Override
	public void run()
	{
		try
		{
			ois = new ObjectInputStream(client_socket.getInputStream());
			req_packet = (ClientRequestPacket)ois.readObject();
			if(req_packet == null)
				System.out.println("Handle NULL case");
			handle_command(this.client_socket,req_packet);
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

	public void handle_command(Socket client_socket,ClientRequestPacket req_packet)
	{
		ClientResponsePacket res_packet;
		switch (req_packet.command)
		{
			case DFS_CONSTANTS.REGISTER:
				ClientWrapper cw = new ClientWrapper(req_packet.client_uuid);
				DFS_Globals.global_client_list.put(req_packet.client_uuid,cw);
				res_packet = new ClientResponsePacket();
				res_packet.response_code = DFS_CONSTANTS.OK;
				send_response(res_packet);
				break;
			case DFS_CONSTANTS.LOGIN:
				/* Validate the username requested */
				if(validate_login(req_packet))
				{
					/* Send Positive Feedback */
					res_packet = new ClientResponsePacket();
					res_packet.response_code = DFS_CONSTANTS.OK;
				}
				else
				{
					/* Send Negative Feedback */
					res_packet = new ClientResponsePacket();
					res_packet.response_code = DFS_CONSTANTS.AUTH_FAILED;
				}
				send_response(res_packet);
				break;
			default:
				System.out.println("Invalid Command");
		}
	}

	public boolean validate_login(ClientRequestPacket req_packet)
	{
		ClientWrapper temp_cw;
		if((temp_cw = DFS_Globals.global_client_list.get(req_packet.client_uuid)) != null)
		{
			if(temp_cw.ID.equals(req_packet.client_uuid))
			{
				/* Send the client positive response */
				return true;
			}
			else
			{
				/* Send the client negative reponse */
				return false;
			}
		}
		else
		{
			/* Send the client negative response */
			return false;
		}
	}

	public void send_response(ClientResponsePacket res_packet)
	{
		try
		{
			oos = new ObjectOutputStream(client_socket.getOutputStream());
			oos.writeObject(res_packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
