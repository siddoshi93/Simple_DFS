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
    public ClientResponsePacket Get(ClientRequestPacket req_packet)
    {
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

        /* Set the response packet and pass it back */
        res_packet.file_name = req_packet.file_name;

        res_packet.response_code = DFS_CONSTANTS.OK;
        return res_packet;
    }

    /* Store the file requested by client */
    public ClientResponsePacket Put(ClientRequestPacket req_packet)
    {
        //String cname = null;
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
                System.out.println("Some error in creating the folder");
               return res_packet;
            }
            DFS_Globals.client_data.put(req_packet.client_uuid,client_data);
        }

        /* Set the response packet and pass it back */
        //res_packet.file_name = cname;
        res_packet.response_code = DFS_CONSTANTS.OK;
        return res_packet;
    }

    public void send_file(Socket connect,String file_name)
    {
        FileTransfer ft = new FileTransfer();
        String cname = client_data.file_map.get(file_name);
        try
        {
            ft.send_file(connect, (DFS_Globals.storage_path + client_data.folder_name + "/" + cname));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void recv_file(Socket connect,String file_name,int file_size)
    {
        FileTransfer ft = new FileTransfer();
        boolean new_file = false;

        /* Get the file name  */
        String cname = client_data.file_map.get(file_name);
        if(cname == null)/* New file */
        {
            new_file = true;
            cname = getCNAME(file_name);
            updateSuffix();
        }

        try
        {
            ft.save_file(connect, (DFS_Globals.storage_path + client_data.folder_name + "/" + cname),file_size);

            /* Add the mapping to the File Map so that data node knows about the mapping */
            if (new_file)
                client_data.file_map.put(file_name,cname);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public boolean create_client_folder(String folder_name)
    {
        File dir = new File(DFS_Globals.storage_path + folder_name);
        System.out.println("Creating Client Folder : " + DFS_Globals.storage_path + folder_name);
        if(!(dir.exists() && dir.isDirectory()))
        {
            System.out.println("Creating Directory......");
            return dir.mkdir();
        }
        return true;
    }

    public String getCNAME(String filename)
    {
        return (client_data.random_suffix + filename);
    }

    public void updateSuffix() { client_data.random_suffix++;}
}
