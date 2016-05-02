package dfs_CL;

import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

/**
 * Created by abhishek on 4/24/16.
 */
public class SDFS
{
    public static void main(String[] arg)
    {
        if (arg.length == 0)
        {
            System.out.println("Please use in below manner");
            System.out.println("Usage : SDFS <COMMAND> <Arguement List>");
            System.exit(DFS_CONSTANTS.SUCCESS);
        }

        if((DFS_Globals.base_path = System.getenv(DFS_CONSTANTS.base_path)) == null)
        {
            System.out.print("Please set the Base path of SDFS:" + DFS_CONSTANTS.base_path);
            System.exit(DFS_CONSTANTS.SUCCESS);
        }

        DFS_Globals.user_name_file = DFS_Globals.base_path + DFS_Globals.user_name_file;
        DFS_Globals.sdfs_path = DFS_CONSTANTS.base_path + DFS_Globals.sdfs_path;

        String command = arg[0].toUpperCase();
        boolean status = false;
        switch (command)
        {
            case "REGISTER":
                status = ClientCommand.Register(arg);
                break;
            case "LOGIN":
                status = ClientCommand.Login(arg);
                break;
            case "MKDIR":
                status = ClientCommand.MkDir(arg);
                break;
            case "LS":
                status = ClientCommand.Ls(arg);
                break;
            case "GET":
                status = ClientCommand.Get(arg);
                break;
            case "PUT":
                status = ClientCommand.Put(arg);
                long endTime = System.currentTimeMillis();
                break;
            case "HELP":
                display_help_option();
                break;
            default:
                System.out.println("Unrecognizable Command.....");
        }
        if(status)
            System.out.println("Command Executed Successfully");
        else
            System.out.println("Command Executed Unsuccessfully");
    }

    public static boolean display_help_option()
    {
        System.out.println("Add display");
        return true;
    }
}
