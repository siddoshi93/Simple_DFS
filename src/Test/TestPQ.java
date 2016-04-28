package Test;

import build.tools.javazic.Main;
import dfs_MN.Main_Node_Server;
import dfs_api.DFS_Globals;
import dfs_api.LoadBalancer;
import dfs_api.StorageNode;

import java.util.Iterator;

/**
 * Created by abhishek on 4/28/16.
 */
public class TestPQ {

    public static void main(String[] args)
    {
        Main_Node_Server.setUp_DN_List();

        LoadBalancer.getTargetNode(100);
        LoadBalancer.getTargetNode(200);
        LoadBalancer.getTargetNode(100);
        LoadBalancer.getTargetNode(10);
        StorageNode st;
        Iterator<StorageNode> it = DFS_Globals.dn_q.iterator();
        while (it.hasNext()) {
            st = it.next();
            System.out.println("STORAGE DT : " + st.IPAddr + ":" + st.Size);
        }

    }

}
