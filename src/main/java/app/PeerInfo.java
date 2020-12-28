package app;

import java.io.Serializable;
import java.net.InetAddress;

public class PeerInfo implements Serializable {
    private    int id ;
    private  int port;
    private InetAddress host;

    public PeerInfo(int id, int port, InetAddress host)
    {
        this.id= id;
        this.port =port;
        this. host =host ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getHost() {
        return host;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "PeerInfo{" +
                "id=" + id +
                ", port=" + port +
                ", host='" + host + '\'' +
                '}';
    }
}
