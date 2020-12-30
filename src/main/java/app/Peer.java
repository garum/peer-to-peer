package app;

import exceptions.SendFileFailedException;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Peer extends Thread {
    public PeerInfo info;
    public ServerSocket serverSocket;


    private Map<PeerInfo, PeerConnection> outMap;




    public Peer (PeerInfo info)
    {
        this.info =info;
        this.outMap = new HashMap<>();
        try {
            serverSocket = new ServerSocket(info.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run()
    {

        try {
            while (true) {
                System.out.println("peer " + info.toString() + "is listening");
                PeerConnection peerConnection = new PeerConnection(serverSocket.accept());
                Thread th = new PeerHandler(this.info,peerConnection);
                th.start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public  void connectToPeer(PeerInfo peerInfo)
    {
        PeerConnection outConnection= null;
        try {
            outConnection = new PeerConnection(peerInfo.getHost(),peerInfo.getPort());
            outMap.put(peerInfo, outConnection);

            // send the info to the connected peer
            outConnection.sendPeerInfo(info);

            // receive the confimations message
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeToPeer(PeerInfo peerInfo)
    {
        PeerConnection outConnection= outMap.get(peerInfo);
        if (outConnection!=null) {
            outConnection.send("exit");
            outConnection.close();
            outMap.remove(peerInfo);
        }
    }
    public void sendFileToPeer(PeerInfo peerInfo,String root, String filename) throws SendFileFailedException
    {
        PeerConnection outConnection= outMap.get(peerInfo);
        if(outConnection!=null)
        {
            outConnection.send("file");
            outConnection.sendFile(new File(root+filename));
            String confirmations=outConnection.recvString();
            if (!confirmations.equals("done"))
            {
               throw  new SendFileFailedException("send file failed");
            }
        }
    }

    public void sendToAllPeers(String msg)
    {

        for (Map.Entry<PeerInfo, PeerConnection> entry: outMap.entrySet())
        {
            PeerConnection outConnection= entry.getValue();
            outConnection.send(msg);
        }
    }



}
