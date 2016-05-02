package dfs_api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Stores information about a Single Tree Node in MasterNode Metadata
 */
public class TreeNode implements Serializable
{
    public String NodeName;
    public boolean isDir;
    public Date timeAccess;
    public double size;
    public TreeNode parent;

    /**
     * List of Children of a Node => Files/Directories in a Directory
     */
    public ArrayList<TreeNode> children = new ArrayList<TreeNode>();

    /**
     * List of Data Nodes a file is stored on
     */
    public ArrayList<StorageNode> storageNode = null;

    public TreeNode()
    {
        this.storageNode = null;
        this.NodeName = "/";
        this.isDir = true;
        this.parent=null;
    }

    public TreeNode(ArrayList<StorageNode> storageNode, String NodeName, boolean isDir, Date timeAccess, double size)
    {
        this.storageNode = storageNode;
        this.NodeName = NodeName;
        this.isDir = isDir;
        this.timeAccess = timeAccess;
        this.size = size;
        this.parent= null;
    }
}
