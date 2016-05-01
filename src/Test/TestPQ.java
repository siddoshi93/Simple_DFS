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


    class Tp
    {
        String name;
        int value;
        public Tp(String name,int value)
        {
            this.name=name;
            this.value=value;
        }

    }

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
        TestPQ obj=new TestPQ();


        ArrayList<Tp> tester= new ArrayList<Tp>();

        ArrayList<Tp> otherlist= new ArrayList<Tp>();

        tester.add(obj.new Tp("asd",10));
        tester.add(obj.new Tp("bsd",10));
        tester.add(obj.new Tp("bsd",10));
        tester.add(obj.new Tp("asd",10));

        otherlist.add(tester.get(0));
        otherlist.add(tester.get(1));

        for(Tp a: tester)
        {
            a.value=-1;
        }

        for(Tp a: otherlist)
        {
            System.out.println(a.name+" "+a.value);

        }
    }

}
