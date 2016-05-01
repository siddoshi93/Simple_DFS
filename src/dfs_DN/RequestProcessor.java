package dfs_DN;

/* Program specific import */
import dfs_api.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by abhishek on 4/26/16.
 */
public class RequestProcessor implements Runnable {
    ReplicatorDmn replication_req;
    private Socket client_socket;
    private String uuid;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private ClientRequestPacket req_packet;
    private ClientMetaData client_data;

    public RequestProcessor(Socket soc, String uuid) {
        this.client_socket = soc;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        try {
            System.out.println("DN");
            ois = new ObjectInputStream(client_socket.getInputStream());
            req_packet = (ClientRequestPacket) ois.readObject();
            if (req_packet == null)
                System.out.println("Handle NULL case");
            System.out.println("Request for ID : " + req_packet.client_uuid + ":" + req_packet.dn_list.size());
            handle_command();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Data_Node_Server.remove_active_request(uuid);
            try {
                client_socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handle_command() throws IOException
    {
        ClientResponsePacket res_packet = null;
        DN_CommandHandler handler = new DN_CommandHandler(req_packet.client_uuid);
        System.out.println("DN List Size : " + req_packet.dn_list.size());
        /* Handle the request */
        switch (req_packet.command)
        {
            case DFS_CONSTANTS.GET:
                res_packet = handler.Get(req_packet);
                send_response(res_packet);
                if(res_packet != null) /* Wrong request */ {
                    System.out.println("Sending file..commented");
                    handler.send_file(client_socket, res_packet.file_name);
                }
                break;
            case DFS_CONSTANTS.PUT:
                res_packet = handler.Put(req_packet);
                send_response(res_packet);
                if(res_packet != null) {
                    System.out.println("Saving file... commented : " + req_packet.file_name + ":" + req_packet.file_size);
                    handler.recv_file(client_socket, req_packet.file_name,req_packet.file_size);
                }
                /* We need to notify the main node about this updation */
                NotifyMN();
                /* Check if its a replication request */
                check_and_replicate(req_packet);
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
        String arg[] = new String[DFS_CONSTANTS.TWO];
        Socket mn_connect = null;
        System.out.println("DN List : " + req_packet.dn_list.size());
        //arg[0] = this.req_packet.arguments[1];
        mn_req_packet.client_uuid = req_packet.client_uuid;
        mn_req_packet.arguments = req_packet.arguments;
        mn_req_packet.dn_list = req_packet.dn_list;
        mn_req_packet.file_name = req_packet.file_name;
        System.out.println("Argument is : " + mn_req_packet.file_name);

        try {
            mn_connect = new Socket(DFS_Globals.server_addr,DFS_CONSTANTS.MN_LISTEN_PORT);
            send_request(mn_connect,mn_req_packet);
            mn_res_packet = recv_response(mn_connect);
            if(mn_res_packet == null || mn_res_packet.response_code != DFS_CONSTANTS.OK)
            {
                System.out.println("Issue in updating in MN");
            }
            else
            {
                System.out.println("Notificaton Successfull...");
                System.out.println("Recv DN List : " + req_packet.dn_list.size());
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

    /* Routine which will start a DN 2 DN transfer in case of a repliation */
    public void check_and_replicate(ClientRequestPacket req_packet) throws IOException
    {
        Socket repl_dn = null;
        if(!req_packet.replicate_ind)
        {
            System.out.println("No Replication");
            return;
        }
        System.out.println("Doing Replication : " + req_packet.dn_list.size());
        client_data = DFS_Globals.client_data.get(req_packet.client_uuid);
        String file_path = (DFS_CONSTANTS.storage_path +
                            client_data.folder_name +
                            "/" +
                            client_data.file_map.get(req_packet.file_name));
        System.out.println("File Path for DN Replication : " + file_path);
        req_packet.dn_list.remove(DFS_CONSTANTS.ZERO);

        repl_dn = new Socket(req_packet.dn_list.get(DFS_CONSTANTS.ZERO).IPAddr,DFS_CONSTANTS.DN_LISTEN_PORT);
        req_packet.replicate_ind = false; /* No More Replication */

        replication_req = new ReplicatorDmn(repl_dn,req_packet,file_path);
        new Thread(replication_req).start();
    }

    public void send_response(ClientResponsePacket res_packet)
    {
        try
        {
            System.out.println("Sending reponse : " + res_packet.response_code);
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
