package dfs_MN;

import java.net.Socket;

/* Program specific import */

public class ClientRequestHandle implements Runnable{
	Socket client_socket;
	int list_index;
	
	public ClientRequestHandle(Socket soc,int index) {
		this.client_socket = soc;
		this.list_index = index;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Sleeping " + client_socket.getRemoteSocketAddress().toString() + ":" + list_index);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Main_Node_Server.getSyncList().remove(list_index);
		System.out.println("removed ");
	}

}
