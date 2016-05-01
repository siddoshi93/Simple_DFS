package dfs_api;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by abhishek on 4/26/16.
 */
public class FileTransfer
{
    DataInputStream dis;
    FileOutputStream fos;
    byte[] buffer;

    public void save_file(Socket sock,String path,int file_size) throws IOException
    {
        dis = new DataInputStream(sock.getInputStream());
        fos = new FileOutputStream(path);
        buffer = new byte[DFS_CONSTANTS.DATA_PACKET_SIZE];
        System.out.println("Path : " + path);
        int read = 0;
        int totalRead = 0;
        int remaining = file_size;
        System.out.println("Calling save file ");
        while((read = dis.read(buffer,0,Math.min(buffer.length,remaining))) > 0)
        {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }
        fos.close();
        //dis.close();
    }

    public void send_file(Socket sock,String path) throws IOException
    {
        DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
        FileInputStream fis = new FileInputStream(path);
        byte[] buffer = new byte[DFS_CONSTANTS.DATA_PACKET_SIZE];
        while (fis.read(buffer) > 0) {
            System.out.println("Sending file");
            dos.write(buffer);
        }

        fis.close();
        //dos.close();
    }

    public static boolean check_and_create_dir(String path)
    {
        File dir = new File(path);
        if(!(dir.exists() && dir.isDirectory()))
        {
            System.out.println("Creating Directory......");
            return dir.mkdir();
        }
        return true;
    }
}
