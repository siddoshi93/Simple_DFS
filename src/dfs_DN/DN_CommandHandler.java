package dfs_DN;

import dfs_api.*;

import java.io.File;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by abhishek on 4/26/16.
 */
public class DN_CommandHandler
{
    ClientMetaData client_data;
    String client_id;
    boolean status;
    ClientResponsePacket res_packet;

    public DN_CommandHandler()
    {
        status = false;
        res_packet = null;
        client_id = null;
    }

    public DN_CommandHandler(String client_id)
    {
        status = false;
        this.client_id = client_id;
    }

    /* Get Request to Transfer file to client*/
    public ClientResponsePacket Get(Socket connect,ClientRequestPacket req_packet)
    {
        FileTransfer ft = new FileTransfer();
        String cname = null;
        res_packet = new ClientResponsePacket();
        res_packet.response_code = DFS_CONSTANTS.FAILURE;

        if (client_id == null)
        {
            return res_packet;
        }

        client_data = DFS_Globals.client_data.get(client_id);
        if(client_data == null) /* Since DN is not aware of this client */
        {
            return res_packet;
        }
        /* Get the file name which needs to be send  */
        cname = client_data.file_map.get(req_packet.file_name);
        if(cname == null)
            return res_packet;

        try
        {
            ft.send_file(connect, (DFS_CONSTANTS.storage_path + client_data.folder_name), cname, req_packet.file_size);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return res_packet;
        }
        /* Set the response packet and pass it back */
        res_packet.response_code = DFS_CONSTANTS.OK;
        return res_packet;
    }

    /* Store the file requested by client */
    public ClientResponsePacket Put(Socket connect,ClientRequestPacket req_packet)
    {
        FileTransfer ft = new FileTransfer();
        String cname = null;
        res_packet = new ClientResponsePacket();
        res_packet.response_code = DFS_CONSTANTS.FAILURE;

        if (client_id == null)
        {
            return res_packet;
        }

        client_data = DFS_Globals.client_data.get(client_id);
        if(client_data == null) /* Since DN is not aware of this client */
        {
            /* Data Node does not contain the client so add one */
            client_data = new ClientMetaData(UUID.randomUUID().toString());
            /* Create a folder */
            if(!create_client_folder(client_data.folder_name))
            {
               return res_packet;
            }
            DFS_Globals.client_data.put(req_packet.client_uuid,client_data);
        }
        /* Get the file name  */
        cname = getCNAME(req_packet.file_name);
        try
        {
            ft.send_file(connect, (DFS_CONSTANTS.storage_path + client_data.folder_name), cname, req_packet.file_size);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return res_packet;
        }
        /* Add the mapping to the File Map so that data node knows about the mapping */
        client_data.file_map.put(req_packet.file_name,cname);

        /* Set the response packet and pass it back */
        res_packet.response_code = DFS_CONSTANTS.OK;
        return res_packet;
    }

    public boolean create_client_folder(String folder_name)
    {
        File dir = new File(DFS_CONSTANTS.storage_path + folder_name);
        if(!(dir.exists() && dir.isDirectory()))
        {
            System.out.println("Creating Directory......");
            return dir.mkdir();
        }
        return true;
    }

    public String getCNAME(String filename)
    {
        return (filename + client_data.random_suffix);
    }
}
