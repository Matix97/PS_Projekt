import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Send extends Thread {

    private String msg;
    private MulticastSocket socket;
    private InetAddress group;
    private int port;


    public Send(String msg, MulticastSocket socket, InetAddress group, int port) {
        this.msg = msg;
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {
        byte[] buffer = msg.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,group,port);
        try {
            socket.send(datagram);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
