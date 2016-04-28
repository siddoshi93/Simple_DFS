package dfs_api;

import java.util.Iterator;

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

        StorageNode st;
        Iterator<StorageNode> it = DFS_Globals.dn_q.iterator();
        while (it.hasNext()) {
            st = it.next();
            System.out.println("STORAGE DT : " + st.IPAddr + ":" + st.Size);
        }

        return targetNode;
    }
    public synchronized static void updateDNQ(StorageNode newDNData)
    {
        //Data Node values update
    }
}
