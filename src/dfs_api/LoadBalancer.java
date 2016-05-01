package dfs_api;

import java.util.Iterator;

/**
 * Created by Raunaq-PC on 27-04-2016.
 */
public class LoadBalancer {

    public synchronized static StorageNode getTargetNode(double size)
    {
        StorageNode targetNode=null;

        while(DFS_Globals.dn_q.size()>0)
        {
            targetNode = DFS_Globals.dn_q.poll();

            if(targetNode.Size>=0)    // To remove dead StorageNode
            {
                targetNode.Size += size;
                DFS_Globals.dn_q.add(targetNode);  // adds update StorageNode
                return targetNode;
            }
        }

        return null;
    }

    public synchronized static void updateDNQ(StorageNode newDNData)
    {
        //Data Node values update
    }
}
