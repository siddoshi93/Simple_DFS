package dfs_api;

import java.io.Serializable;

/**
 * Created by Anas on 4/24/2016.
 */
public class StorageNode implements Serializable {
    public String IPAddr;
    public String DataNodeID;
    public long Size;
    public long Capacity;

    public StorageNode(String IPAddr, String DataNodeID, long Capacity)
    {
        this.IPAddr = IPAddr;
        this.DataNodeID = DataNodeID;
        Size = 0;
        this.Capacity = Capacity;
    }
}
