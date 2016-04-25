package dfs_api;


import java.util.ArrayList;

/**
 * Created by abhishek on 4/24/16.
 * This file is responsible for operating on the server
 */
public class TreeAPI {
    public void TreeInsert(TreeNode root, TreeNode newNode)
    {
        if (root.children == null)
        {
            root.children = new ArrayList<TreeNode>();
        }
        root.children.add(newNode);
    }
}
