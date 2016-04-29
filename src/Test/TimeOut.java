package Test;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by abhishek on 4/29/16.
 */
public class TimeOut
{
    public static void main(String arg[])throws IOException
    {
        boolean is_alive = InetAddress.getByName("85.63.3.9").isReachable(2000);
        if(is_alive)
            System.out.println("Alive");
        else
            System.out.println("Dead");
    }
}
