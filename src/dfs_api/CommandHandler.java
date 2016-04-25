package dfs_api;

import java.util.Date;

/**
 * Created by Anas on 4/24/2016.
 */
public class CommandHandler {

    //LS Command
    public ClientResponsePacket commandLS (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        responsePacket.curNode = DFS_Globals.global_client_list.get(req_packet.client_uuid).curr;
        responsePacket.response_code = DFS_CONSTANTS.OK;

        return responsePacket;
    }

    //arguments[0] in MKDIR is the name of the Directory
    public ClientResponsePacket commandMKDIR (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        TreeNode currNode = DFS_Globals.global_client_list.get(req_packet.client_uuid).curr;

        if (TreeAPI.TreeInsert(currNode,new TreeNode(null,req_packet.arguments[0],true,new Date(),0)))
            responsePacket.response_code = DFS_CONSTANTS.OK;
        else
            responsePacket.response_code = DFS_CONSTANTS.FAILURE;

        return responsePacket;
    }
}
