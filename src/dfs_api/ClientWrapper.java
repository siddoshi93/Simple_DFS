package dfs_api;

import java.io.Serializable;

/**
 * Created by Anas on 4/24/2016.
 */
public class ClientWrapper implements Serializable{
    public String ID;
    public TreeNode root;
    public TreeNode curr;

    public ClientWrapper(String id)
    {
        this.ID = id;
        root = new TreeNode(); /* Root node for register file system */
        curr = root;
    }
}
