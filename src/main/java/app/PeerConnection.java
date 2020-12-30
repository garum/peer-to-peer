package app;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

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
        if (clientSocket.isConnected()) {
            if (clientSocket.isConnected()) {
                try {
                    out.flush();
                    out.writeUTF(msg);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public  void sendBytes(byte[] bytes)
    {
        if (clientSocket.isConnected()) {
            try {
                out.writeInt(bytes.length);
                out.flush();
                out.write(bytes);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public  byte[] recvBytes()
    {
        try {
            int length = in.readInt();
            if(length>0)
            {
                byte [] message = new byte[length];
                in.readFully(message,0,message.length);
                return  message;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public void sendFile(File file)
    {
        if (clientSocket.isConnected()) {
            InputStream fin = null;

            try {
                long lenght = file.length();
                out.writeLong(lenght);
                fin = new FileInputStream(file);
                int count;
                byte[] bytes = new byte[2048];
                while ((count = fin.read(bytes)) > 0) {
                    System.out.println("writing " + Arrays.toString(bytes));
                    out.write(bytes, 0, count);
                    out.flush();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void recvFile(File file)
    {
        if (clientSocket.isConnected()) {
            if (!file.exists()) {
                System.out.println("file does not exist" + file.toString());
                try {
                    if (file.createNewFile())
                        System.out.println("File created");
                    else
                        System.out.println("File already exists or failed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                OutputStream fout = new FileOutputStream(file);
                long fileSize = in.readLong();
                byte[] bytes = new byte[2048];
                int count = 1;
                long bytesRead = 0;
                while (count > 0 && bytesRead < fileSize) {
                    count = in.read(bytes);
                    bytesRead += count;
                    System.out.println("reading " + Arrays.toString(bytes));

                    fout.write(bytes, 0, count);
                    fout.flush();
                    System.out.println(" done reading " + count + " " + fileSize + " " + bytesRead + " " + (count > 0 && bytesRead < fileSize));

                }
                this.send("done");
                System.out.println(" finished reading ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String recvString ()
    {
        if (clientSocket.isConnected()) {
            try {
                return in.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    public void sendPeerInfo(PeerInfo peerInfo)
    {
        if (clientSocket.isConnected()) {
            try {
                out.writeObject(peerInfo);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public PeerInfo recvPeerInfo()
    {
        if (clientSocket.isConnected()) {
            try {
                return (PeerInfo) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
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

    public  boolean isConnected()
    {
        return clientSocket.isConnected();
    }


}
