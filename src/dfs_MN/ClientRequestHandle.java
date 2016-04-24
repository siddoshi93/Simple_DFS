package dfs_MN;

import java.net.Socket;

public class ClientRequestHandle implements Runnable{
	Socket client_socket;
	
	public ClientRequestHandle(Socket soc) {
		this.client_socket = soc;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(client_socket.getRemoteSocketAddress().toString());
	}

}
