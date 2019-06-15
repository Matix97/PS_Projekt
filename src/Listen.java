import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Listen extends Thread {

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private static final int MAX_LEN = 1000;

    public Listen(MulticastSocket socket, InetAddress group, int port) {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {

        while(!Main.koniec) {
            byte[] buffer = new byte[Listen.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            String message;
            try {
                socket.setSoTimeout(10000);//w sumie nie wiem po co to,no ale było w instrukcji
                socket.receive(datagram);

                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if(message.startsWith("NICK")){

                    checkNicksUniqe(message);
                }
                else if (!message.startsWith(Main.name))
                    System.out.println(message);
            } catch (IOException e) {
                System.out.println("Socket closed!");
            }
        }
    }

    void checkNicksUniqe(String message){
        String string[]=message.split(" ");
        if(string.length==2)
        {
            if(string[1].equalsIgnoreCase(Main.name))
            {
                Send send = new Send("NICK "+string[1]+" BUSSY", socket, group, port);
                send.start();
                try {
                    send.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        else if(string.length==3 && Main.name.equalsIgnoreCase(" "))
        {
            Main.nickUnunique=true;
        }

    }
}
