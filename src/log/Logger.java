package log;

import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
/**
 * Created by Raunaq-PC on 29-04-2016.
 */
public class Logger {

    FileWriter logFile=null;

    static Logger log = null;

    private Logger()
    {
    }

    public static Logger getInstance()
    {
        if(log==null)
            log=new Logger();
        return log;
    }

    public void setLogName(String name)
    {
        try {
            logFile = new FileWriter(DFS_CONSTANTS.logPath+name);
            logFile.write("-----------LOG START------------");
        }catch(IOException ie)
        {
            System.out.println("Could not create file: "+ie.getMessage());
        }
    }


    public void print(String message,String infoType)
    {
        if(logFile!=null)
        {
            try {

                logFile.write("\n"+(new Date().toString())+": "+infoType+" "+" "+message);

            }catch(IOException ie)
            {
                System.out.println("Could not write to file:"+ ie.getMessage());
            }

        }
    }
    public void stopLogging()
    {
        try
        {
            logFile.write("\n"+"-----------END OF LOG------------");
            logFile.close();
        }catch(IOException ie)
        {
            System.out.println("Error in closing file: "+ie.getMessage());
        }
    }

}
