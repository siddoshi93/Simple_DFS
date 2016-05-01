package Test;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Created by abhishek on 5/1/16.
 */
public class HostAddr
{
    public static void main(String ar[])throws UnknownHostException
    {
        System.out.println(Inet4Address.getLocalHost().getHostAddress());
    }
}
