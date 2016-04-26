package dfs_api;

import java.util.Date;

/**
 * Created by Anas on 4/24/2016.
 */
public class CommandHandler {

    //Determine which Node to Use (Root or Current) based on the path given (Relative path or absolute path)
    public static TreeNode searchNode (ClientRequestPacket req_packet, String path)
    {

        TreeNode tempNode = DFS_Globals.global_client_list.get(req_packet.client_uuid).root;
        /*
        if (path.charAt(0) == '/')
            tempNode =  clientWrapper.root;
        else
            tempNode = clientWrapper.curr;
         */

        return TreeAPI.findNode(tempNode,path);
    }

    //LS Command (Path is either a Full path OR ./directory)
    public static ClientResponsePacket commandLS (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        String path = req_packet.arguments[0];

        responsePacket.curNode = searchNode(req_packet,path);
        responsePacket.response_code = DFS_CONSTANTS.OK;

        return responsePacket;
    }

    //MKDIR Command
    public static ClientResponsePacket commandMKDIR (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        String arg = req_packet.arguments[0], dirPath, nodeName;

        if(arg.lastIndexOf('/') == -1)              //If Path is ABSOLUTE
        {
            dirPath = "";
            nodeName = req_packet.arguments[0];
        }
        else                                        //If Path is RELATIVE
        {
            dirPath = arg.substring(0,arg.lastIndexOf('/'));
            nodeName = arg.substring(arg.lastIndexOf('/')+1);
        }

        //Getting Correct Directory Node to insert a new directory
        TreeNode node = searchNode(req_packet,dirPath);

        boolean insert = TreeAPI.TreeInsert(node,
                                            new TreeNode(null,              //Storage Node List => Implement Function
                                                         nodeName,
                                                         true,              //isDir = true
                                                         new Date(),
                                                         0)                 //Size at the time of creation
                                            );
        if (insert)
            responsePacket.response_code = DFS_CONSTANTS.OK;
        else
            responsePacket.response_code = DFS_CONSTANTS.FAILURE;

        return responsePacket;
    }

    //CD command
    public static ClientResponsePacket commandCD (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        return responsePacket;
    }

    //PUT Command
    public static ClientResponsePacket commandPUT (ClientRequestPacket req_packet)
    {
        ClientResponsePacket responsePacket = new ClientResponsePacket();

        String filePath = req_packet.arguments[1];

        if (searchNode(req_packet,filePath) != null)
            responsePacket.response_code = DFS_CONSTANTS.OK;
        else
            responsePacket.response_code = DFS_CONSTANTS.FAILURE;

        //

        return responsePacket;
    }

}
