package dfs_api;

import java.util.Date;

/**
 * Created by Anas on 4/24/2016.
 */
public class CommandHandler {

    public static TreeNode nodeToSend (ClientWrapper clientWrapper, String path)
    {
        TreeNode tempNode;
        if (path.charAt(0) == '/')
            tempNode =  clientWrapper.root;
        else
            tempNode = clientWrapper.curr;

        return TreeAPI.findNode(tempNode,path);
    }
    //LS Command
    public static ClientResponsePacket commandLS (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        String path = req_packet.arguments[0];

        responsePacket.curNode = nodeToSend(DFS_Globals.global_client_list.get(req_packet.client_uuid),path);
        responsePacket.response_code = DFS_CONSTANTS.OK;

        return responsePacket;
    }

    //arguments[0] in MKDIR is the name of the Directory
    public static ClientResponsePacket commandMKDIR (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        String arg = req_packet.arguments[0], dirPath, nodeName;

        if(arg.lastIndexOf('/') == -1)
        {
            dirPath = "";
            nodeName = req_packet.arguments[0];
        }
        else
        {
            dirPath = arg.substring(0,arg.lastIndexOf('/'));
            nodeName = arg.substring(arg.lastIndexOf('/')+1);
        }

        TreeNode node = nodeToSend(DFS_Globals.global_client_list.get(req_packet.client_uuid),dirPath);

        boolean insert = TreeAPI.TreeInsert(node,
                                            new TreeNode(null,
                                                         nodeName,
                                                         true,
                                                         new Date(),
                                                         0)
                                            );
        if (insert)
            responsePacket.response_code = DFS_CONSTANTS.OK;
        else
            responsePacket.response_code = DFS_CONSTANTS.FAILURE;

        return responsePacket;
    }
}
