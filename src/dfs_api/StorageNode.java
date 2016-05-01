package dfs_api;

import java.io.Serializable;

/**
 * Created by Anas on 4/24/2016.
 */
public class StorageNode implements Serializable {
    public String IPAddr;
    public String DataNodeID;
    public double Size;
    public long Capacity;
    public boolean isAlive;

    public StorageNode(String IPAddr, String DataNodeID, long Capacity) {
        this.IPAddr = IPAddr;
        this.DataNodeID = DataNodeID;
        Size = 0;
        this.Capacity = Capacity;
        isAlive = true;
    }

    public StorageNode(StorageNode storageNode)
    {
        this.IPAddr = storageNode.IPAddr;
        this.DataNodeID = storageNode.DataNodeID;
        Size = storageNode.Size;
        this.Capacity = storageNode.Capacity;
        isAlive = storageNode.isAlive;
    }
}
