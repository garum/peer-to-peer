package app;

import app.PeerConnection;

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
            Boolean flag = true;
            while (flag)
            {
                String msg = peerConnection.recvString();
                if (msg.equals("ping"))
                {
                    System.out.println(mainPeer.toString() + ": pong");
                }

                if(msg.equals("exit"))
                {
                    System.out.println(mainPeer.toString() +":is closing connesctions with  " + peerInfo.toString() );
                  peerConnection.close();
                  flag= false;

                }
            }
    }
}
