package dfs_api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anas on 4/24/2016.
 */
public class TreeNode implements Serializable
{
    public String NodeName;
    public boolean isDir;
    public Date timeAccess;
    public long size;
    public TreeNode parent;

    //List of Directories and Files
    public ArrayList<TreeNode> children = new ArrayList<TreeNode>();

    //List of Storage Nodes the files are stored on
    public ArrayList<StorageNode> storageNode = null;

    public TreeNode()
    {
        this.storageNode = null;
        this.NodeName = "/";
        this.isDir = true;
        this.parent=null;
    }

    public TreeNode(ArrayList<StorageNode> storageNode, String NodeName, boolean isDir, Date timeAccess, long size)
    {
        this.storageNode = storageNode;
        this.NodeName = NodeName;
        this.isDir = isDir;
        this.timeAccess = timeAccess;
        this.size = size;
        this.parent= null;
    }
}
