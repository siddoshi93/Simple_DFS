package dfs_api;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anas on 4/30/2016.
 */
public class Packet implements Serializable{
    public String client_uuid;

    public int command;
    public int response_code;

    public TreeNode curNode;    //Returning Current Node for LS Command

    public String file_name;
    public String arguments[];
    public double file_size;
    public boolean replicate_ind;

    public String mn_addr;

    public ArrayList<StorageNode> dn_list;
}
