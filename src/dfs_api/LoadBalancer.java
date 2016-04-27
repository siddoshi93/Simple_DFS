package dfs_api;

/**
 * Created by Raunaq-PC on 27-04-2016.
 */
public class LoadBalancer {

    public synchronized static StorageNode getTargetNode(double size)
    {
        StorageNode targetNode;
        targetNode= DFS_Globals.dn_q.poll();
        targetNode.Size+=size;
        DFS_Globals.dn_q.add(targetNode);  // adds update StorageNode
        return targetNode;
    }
    public synchronized static void updateDNQ(StorageNode newDNData)
    {
        //Data Node values update
    }
}
