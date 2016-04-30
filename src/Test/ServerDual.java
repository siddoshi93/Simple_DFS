package Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by abhishek on 4/29/16.
 */
public class ServerDual extends Thread {

    private ServerSocket ss;

    public ServerDual(int port) {
        try {
            ss = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                Socket clientSock = ss.accept();
                saveFile(clientSock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile(Socket clientSock) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        byte[] buffer = new byte[4096];

        Socket s = new Socket("52.39.190.64",1988);
        System.out.println("Connecting for other");
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        int filesize = 2792591; // Send file size in separate msg
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            //System.out.println("read " + totalRead + " bytes.");
            //fos.write(buffer, 0, read);
            dos.write(buffer,0,read);
        }
        System.out.println("Done");
        dos.close();
        dis.close();
    }

    public static void main(String[] args) {
        ServerDual fs = new ServerDual(1988);
        fs.start();
    }

}