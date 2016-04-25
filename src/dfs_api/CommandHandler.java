package dfs_api;

/**
 * Created by Anas on 4/24/2016.
 */
public class CommandHandler {

    //Handling LS Command
    public ClientResponsePacket commandLS (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        responsePacket.curNode = DFS_Globals.global_client_list.get(req_packet.client_uuid).curr;
        responsePacket.response_code = DFS_CONSTANTS.OK;

        return responsePacket;
    }
}
