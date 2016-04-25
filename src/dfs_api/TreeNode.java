package dfs_api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Anas on 4/24/2016.
 */
public class TreeNode implements Serializable {

    public String NodeName;
    public boolean isDir;
    public Date timeAccess;
    public long size;

    public List<TreeNode> children;

    public TreeNode()
    {
        this.NodeName = "/";
        this.isDir = true;
    }
}
