package dfs_api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anas on 4/24/2016.
 */
public class TreeNode implements Serializable
{
    public StorageNode storageNode;
    public String NodeName;
    public boolean isDir;
    public Date timeAccess;
    public long size;

    //List of Directories and Files
    public ArrayList<TreeNode> children = null;

    public TreeNode(StorageNode storageNode, String NodeName, boolean isDir, Date timeAccess, long size)
    {
        this.storageNode = storageNode;
        this.NodeName = NodeName;
        this.isDir = isDir;
        this.timeAccess = timeAccess;
        this.size = size;
    }
}
