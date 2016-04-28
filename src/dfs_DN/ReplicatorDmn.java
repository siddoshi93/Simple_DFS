package dfs_DN;

import dfs_CL.ClientAPI;
import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;
import dfs_api.FileTransfer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by abhishek on 4/28/16.
 */
public class ReplicatorDmn implements Runnable
{
    private Socket dn_socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientRequestPacket req_packet;
    private ClientResponsePacket dn_res_packet;
    private FileTransfer ftp;
    private String file_path;

    public ReplicatorDmn(Socket soc,ClientRequestPacket req_packet,String file_path) {
        this.dn_socket = soc;
        this.req_packet = req_packet;
        this.file_path = file_path;
        ftp = new FileTransfer();
    }

    @Override
    public void run()
    {
        try
        {
            send_request();
            dn_res_packet = ClientAPI.recv_response(dn_socket);
            System.out.println("Got response from Replicator daemon rc : " + dn_res_packet.response_code);
            if (dn_res_packet !=null && dn_res_packet.response_code == DFS_CONSTANTS.OK)
            {
                /* Start Sending the file */
                System.out.println("Starting sending file for replication....");
                ftp.send_file(dn_socket,file_path);
            }
            else
            {
                System.out.println("Recv null from Replicator DN response : ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dn_socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send_request()
    {
        try {
            oos = new ObjectOutputStream(dn_socket.getOutputStream());
            oos.writeObject(req_packet);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
