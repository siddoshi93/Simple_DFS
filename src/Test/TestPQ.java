package Test;


import dfs_MN.Main_Node_Server;
import dfs_api.DFS_CONSTANTS;
import dfs_api.DFS_Globals;
import dfs_api.LoadBalancer;
import dfs_api.StorageNode;
import log.Logger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by abhishek on 4/28/16.
 */
public class TestPQ {

    public static void main(String[] args)
    {
/*       Main_Node_Server.setUp_DN_List();

        LoadBalancer.getTargetNode(100);
        LoadBalancer.getTargetNode(200);
        LoadBalancer.getTargetNode(100);
        LoadBalancer.getTargetNode(10);
        StorageNode st;
        Iterator<StorageNode> it = DFS_Globals.dn_q.iterator();
        while (it.hasNext()) {
            st = it.next();
            System.out.println("STORAGE DT : " + st.IPAddr + ":" + st.Size);
        }
*/
        /*System.out.println((new Date().toString()));

        Logger newLog= Logger.getInstance();
        newLog.setLogName("clientTest");

        newLog.print("Check", DFS_CONSTANTS.WARN);
        newLog.print("Check", DFS_CONSTANTS.WARN);
        newLog.print("Check", DFS_CONSTANTS.WARN);

        newLog.stopLogging();*/

        LinkedList<String> tester= new LinkedList<String>();
        tester.add("asd");
        tester.add("dsd");
        tester.add("asd");
        tester.add("bsd");

        for(String a: tester)
        {
            if(a.equals("asd"))
                tester.remove(a);
        }
        for(String a: tester)
        {
           System.out.println(a);
        }



        try {
            System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
        }catch(Exception e)
        {

        }


    }

}
