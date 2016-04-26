package dfs_api;

/**
 * Created by Anas on 4/24/2016.
 */
public class ClientWrapper {
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
