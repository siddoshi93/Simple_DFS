package dfs_api;


import java.util.ArrayList;

/**
 * Created by abhishek on 4/24/16.
 * This file is responsible for operating on the server
 */
public class TreeAPI {

    public static boolean TreeInsert(TreeNode root, TreeNode newNode)
    {
        //To avoid duplicates
        for (TreeNode childNode : root.children)
        {
            if (childNode.NodeName.equals(newNode.NodeName))
                return false;
        }

        root.children.add(newNode);

        return true;
    }
}
