package app;

import app.PeerConnection;

import java.io.File;

public class PeerHandler extends Thread {

    public PeerConnection peerConnection;
    PeerInfo mainPeer;

    public PeerHandler (PeerInfo mainPeer, PeerConnection ss)
    {
        this.peerConnection =ss;
        this.mainPeer=mainPeer;
    }

    @Override
    public void run() {

            PeerInfo peerInfo = peerConnection.recvPeerInfo();
            System.out.println("peer " + peerInfo.toString() + " is connected to " + mainPeer.toString());
            boolean flag = true;
            while (peerConnection.isConnected())
            {
                String msg = peerConnection.recvString();
                System.out.println(mainPeer.toString() + " echo : " + msg);
                if(msg == null)
                {
                    break;
                }
                if (msg.equals("ping"))
                {
                    System.out.println(mainPeer.toString() + ": pong");
                }
                if(msg.equals("file"))
                {
                    peerConnection.recvFile(new File("recieved.txt"));
                }

                if(msg.equals("exit") )
                {
                    System.out.println(mainPeer.toString() +":is closing connesctions with  " + peerInfo.toString() );
                    peerConnection.close();
                    flag =false;
                }
            }
            System.out.println(mainPeer.toString() +":is closed connesctions with  " + peerInfo.toString() );

    }
}
