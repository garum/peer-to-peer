package app;

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

    public void closeToPeer(PeerInfo peer)
    {
        PeerConnection outConnection= outMap.get(peer);
        if (outConnection!=null) {
            outConnection.send("exit");
            outConnection.close();
            outMap.remove(peer);
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


    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            System.out.println(host.toString());

            Scanner scanner = new Scanner(System. in);
            String inputString = scanner. nextLine();
            int port = Integer.parseInt(inputString);

            PeerInfo pi = new PeerInfo(1,port,host);


            Peer peer1= new Peer (pi);
            peer1.start();

            inputString = scanner. nextLine();
            port = Integer.parseInt(inputString);
            inputString = scanner. nextLine();
            InetAddress inetAddress = InetAddress.getByName(inputString);


            PeerInfo pi2  = new PeerInfo(1,port,inetAddress);
            peer1.connectToPeer(pi2);

            peer1.sendToAllPeers(null);
            peer1.closeToPeer(pi2);

            peer1.join();

        } catch (UnknownHostException | InterruptedException e) {
            e.printStackTrace();
        }



    }
}
