package dfs_DN;

/* Program specific import */
import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by abhishek on 4/26/16.
 */
public class RequestProcessor implements Runnable {
    private Socket client_socket;
    private String uuid;
    boolean status;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientRequestPacket req_packet;

    public RequestProcessor(Socket soc, String uuid) {
        this.client_socket = soc;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(client_socket.getInputStream());
            req_packet = (ClientRequestPacket) ois.readObject();
            if (req_packet == null)
                System.out.println("Handle NULL case");
            System.out.println("ID : " + req_packet.client_uuid);
            handle_command();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Data_Node_Server.remove_active_request(uuid);
        }
    }

    public void handle_command()
    {
        ClientResponsePacket res_packet = null;
        DN_CommandHandler handler = new DN_CommandHandler(req_packet.client_uuid);

        /* Handle the request */
        switch (req_packet.command)
        {
            case DFS_CONSTANTS.GET:
                res_packet = handler.Get(req_packet);
                send_response(res_packet);
                if(res_packet != null) /* Wrong request */
                    handler.send_file(client_socket,res_packet.file_name);
                break;
            case DFS_CONSTANTS.PUT:
                res_packet = handler.Put(req_packet);
                send_response(res_packet);
                if(res_packet != null)
                    handler.recv_file(client_socket,req_packet.file_name);
                /* We need to notify the main node about this updation */
                NotifyMN();
                break;
            default:
                System.out.println("Invalid request");
                res_packet = new ClientResponsePacket();
                res_packet.response_code = DFS_CONSTANTS.INVALID_CMD;
                send_response(res_packet);
        }
        System.out.println("Returning back to client : " + res_packet == null);
    }

    public void NotifyMN()
    {
        ClientRequestPacket mn_req_packet = new ClientRequestPacket();
        ClientResponsePacket mn_res_packet = new ClientResponsePacket();

        mn_req_packet.command = DFS_CONSTANTS.UPDATE;
        String arg[] = new String[DFS_CONSTANTS.ONE];
        Socket mn_connect = null;

        arg[0] = this.req_packet.arguments[1];
        mn_req_packet.arguments = arg;
        try {
            mn_connect = new Socket(DFS_Globals.server_addr,DFS_CONSTANTS.MN_LISTEN_PORT);
            send_request(mn_connect,mn_req_packet);
            mn_res_packet = recv_response(mn_connect);
            if(mn_res_packet == null || mn_res_packet.response_code != DFS_CONSTANTS.OK)
            {
                System.out.println("Issue in updating in MN");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                mn_connect.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientResponsePacket recv_response(Socket mn_socket)
    {
        ClientResponsePacket res_packet = null;
        try
        {
            /* Wait for the response packet from the server */
            ois = new ObjectInputStream(mn_socket.getInputStream());
            res_packet = (ClientResponsePacket) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            return res_packet;
        }
    }

    public void send_request(Socket conn,ClientRequestPacket req_packet)
    {
        try
        {
            oos = new ObjectOutputStream(conn.getOutputStream());
            oos.writeObject(req_packet);
            oos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send_response(ClientResponsePacket res_packet)
    {
        try
        {
            System.out.println(res_packet.response_code);
            oos = new ObjectOutputStream(client_socket.getOutputStream());
            oos.writeObject(res_packet);
            oos.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
