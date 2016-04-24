package dfs_api;

import java.util.Date;
import java.util.List;

/**
 * Created by Anas on 4/24/2016.
 */
public class TreeNode {

    public String NodeName;
    public boolean isDir;
    public Date timeAccess;
    public long size;

    public List<TreeNode> children;
}
