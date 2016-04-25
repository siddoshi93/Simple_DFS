package dfs_api;

import java.io.Serializable;

/**
 * Created by abhishek on 4/24/16.
 * This structure will be used by the client to
 * communicate to the server about the request
 */
public class ClientResponsePacket implements Serializable
{
    public int response_code;
    public String secondary_server_ip;

    public TreeNode curNode;    //Returning Current Node for LS Command
}