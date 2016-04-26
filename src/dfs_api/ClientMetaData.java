package dfs_api;

import java.util.HashMap;

/**
 * Created by abhishek on 4/26/16.
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
