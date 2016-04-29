package dfs_CL;

import dfs_api.DFS_CONSTANTS;

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
                long startTime = System.currentTimeMillis();
                status = ClientCommand.Put(arg);
                long endTime = System.currentTimeMillis();
                System.out.println("Put Took : " + (endTime - startTime));
                break;
            case "HELP":
                display_help_option();
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
