package dfs_api;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abhishek on 4/24/16.
 * This structure will be used by the client to
 * communicate to the server about the request
 */
public class ClientResponsePacket implements Serializable
{
    public int response_code;
    public TreeNode curNode;    //Returning Current Node for LS Command

    public ArrayList<StorageNode> dn_list;

    public int command;

    public String file_name;
    public String arguments[];
    public double file_size;
    public boolean replicate_ind;
}