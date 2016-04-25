package dfs_api;

import java.io.Serializable;

/**
 * Created by abhishek on 4/24/16.
 * This structure will be used by the client to
 * communicate to the server about the request
 */
public class ClientRequestPacket implements Serializable
{
    public int command;
    public String client_uuid;
    public String arguments[];

    public int response_code;
}