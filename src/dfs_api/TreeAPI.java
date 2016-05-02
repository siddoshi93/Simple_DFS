package dfs_api;


import java.util.ArrayList;

/**
 * Defines Functions to Operate on Client Metadata => Insert, Search
 */
public class TreeAPI {

    /**
     * Inserting newNode into a node.
     * @param root
     * @param newNode
     * @return
     */
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

    /**
    * Find the node from a Given Path. Root of the Client Metadata is passed ALWAYS
    * @author Raunaq
    */
    public static TreeNode findNode(TreeNode curNode,String path)
    {
        String nextPathPoint;
        TreeNode newCurNode=curNode;

        //Handling Absolute Path and Edge cases
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

    /**
     * To find the reference of childNodeName (child) in curNode (parent)
     * @param curNode
     * @param childNodeName
     * @return
     */
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
