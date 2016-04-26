package Test;

import dfs_api.ClientRequestPacket;
import dfs_api.TreeAPI;
import dfs_api.TreeNode;
import java.util.Date;
/**
 * Created by Raunaq-PC on 26-04-2016.
 */
public class TreeTests {

    public static void main(String[] args)
    {
        TreeNode root=new TreeNode();
        TreeNode dir1=new TreeNode(null,"dir1",true,new Date(),0);
        TreeNode dir2=new TreeNode(null,"dir2",true,new Date(),0);
        TreeNode dir3=new TreeNode(null,"dir3",true,new Date(),0);
        TreeNode dir4=new TreeNode(null,"dir4",true,new Date(),0);
        TreeNode dir5=new TreeNode(null,"dir5",true,new Date(),0);
        TreeAPI.TreeInsert(root,dir1);
        TreeAPI.TreeInsert(root,dir2);
        TreeAPI.TreeInsert(dir1,dir3);
        TreeAPI.TreeInsert(dir1,dir4);
        TreeAPI.TreeInsert(dir2,dir5);
        TreeNode val=TreeAPI.findNode(dir2,"../dir2/dir3");

        if(val!=null)
            System.out.println(val.NodeName);
        else
            System.out.println("NULL!");
    }
}
