package Test;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by abhishek on 4/29/16.
 */

public class Client {

    private Socket s;

    public Client(String host, int port, String file) {
        try {
            s = new Socket(host, port);
            sendFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String file) throws IOException {
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }

        fis.close();
        dos.close();
    }

    public static void main(String[] args) {
        long starttime = System.currentTimeMillis();
        Client fc = new Client("52.35.143.50", 1988, "/home/abhishek/Pictures/cct.zip");
        long endtime = System.currentTimeMillis();
        System.out.println("Time taken : " + (endtime -starttime));
    }

}
