package dfs_api;

import java.util.HashMap;

/**
 * Stores Client Meta Data => Folder Name, Suffix and HashMap of Files stored on each Data Node
 */
public class ClientMetaData
{
    public String folder_name;
    public int random_suffix;
    public HashMap<String,String> file_map;

    public ClientMetaData()
    {
        random_suffix = 11;
        file_map = new HashMap();
    }

    public ClientMetaData(String folder_name)
    {
        random_suffix = 11;
        this.folder_name = folder_name;
        file_map = new HashMap();
    }
}
