package dfs_api;


import java.util.ArrayList;

/**
 * Created by abhishek on 4/24/16.
 * This file is responsible for operating on the server
 */

//Bla
public class TreeAPI {

    public static boolean TreeInsert(TreeNode root, TreeNode newNode)
    {
        //To avoid duplicates
        for (TreeNode childNode : root.children)
        {
            if (childNode.NodeName.equals(newNode.NodeName))
                return false;
        }

        newNode.parent=root;
        root.children.add(newNode);

        return true;
    }

    //To find the node from a Given Path
    /*
    * Created by Raunaq on 04/26/2016
     */
    public static TreeNode findNode(TreeNode curNode,String path)
    {
        String nextPathPoint;
        TreeNode newCurNode=curNode;

        if(path.length()!=0 && path.charAt(0) == '/') {
            if (path.length() > 1)
                path = path.substring(1);
            else
                return newCurNode;
        }

        while(path.length()>0 && newCurNode!=null)
        {
            if(path.indexOf('/')!=-1)
                nextPathPoint = path.substring(0,path.indexOf('/'));

            else {
                nextPathPoint = path;
                path="";
            }

            switch(nextPathPoint)
            {
                case "..":
                          newCurNode=newCurNode.parent;
                          break;
                case ".":
                         continue;
                default:
                        newCurNode=searchNode(newCurNode,nextPathPoint);
                        if(newCurNode!=null)
                        {
                            if (!newCurNode.isDir)
                                newCurNode = null;
                        }
                        break;
            }
            path=path.substring(path.indexOf('/')+1);

        }
        return newCurNode;
    }

    //To find a Node in Children of a Node
    public static TreeNode searchNode(TreeNode curNode,String childNodeName)
    {
        if (curNode.children == null)
            return null;

        for(TreeNode node: curNode.children)
        {
            if(node.NodeName.equals(childNodeName))
                return node;
        }
        return null;
    }
}
