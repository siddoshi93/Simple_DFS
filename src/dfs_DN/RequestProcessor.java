package dfs_DN;

/* Program specific import */
import dfs_api.ClientRequestPacket;
import dfs_api.ClientResponsePacket;
import dfs_api.DFS_CONSTANTS;

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
        DN_CommandHandler handler = new DN_CommandHandler();

        /* Handle the request */
        switch (req_packet.command)
        {
            case DFS_CONSTANTS.GET:
                res_packet = handler.Get(client_socket,req_packet);
                break;
            case DFS_CONSTANTS.PUT:
                res_packet = handler.Put(client_socket,req_packet);
                break;
            default:
                System.out.println("Invalid request");
                res_packet = new ClientResponsePacket();
                res_packet.response_code = DFS_CONSTANTS.INVALID_CMD;
        }
        send_response(res_packet);
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
