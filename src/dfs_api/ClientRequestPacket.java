package dfs_api;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abhishek on 4/24/16.
 * This structure will be used by the client to
 * communicate to the server about the request
 */
public class ClientRequestPacket implements Serializable
{
    public int command;
    public String client_uuid;

    public String file_name;
    public String arguments[];
    public double file_size;
    public boolean replicate_ind;

    public ArrayList<StorageNode> dn_list;

    public int requestEntity;           //0-Client, 1-datanode
}