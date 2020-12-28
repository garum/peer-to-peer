package app;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class PeerConnection {

    public  Socket clientSocket;

    public ObjectOutputStream out;
    public ObjectInputStream in ;

    public  PeerConnection (InetAddress host, int port) throws IOException {

            clientSocket = new Socket(host,port);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

    }

    public  PeerConnection (Socket ss)
    {
        this.clientSocket=ss;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg)
    {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String recvString ()
    {
        try {
           return in.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void sendPeerInfo(PeerInfo peerInfo)
    {
        try {
            out.writeObject(peerInfo);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PeerInfo recvPeerInfo()
    {
        try {
            return (PeerInfo) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void close()
    {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
