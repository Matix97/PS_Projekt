import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;

public class Listen extends Thread
{

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private HashSet<String> usersInRoom = new HashSet<>();

    public Listen(MulticastSocket socket, InetAddress group, int port)
    {
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run()
    {
        while (!Main.finish)
        {
            byte[] buffer = new byte[1000];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
            try {
                socket.setSoTimeout(10000);
                socket.receive(datagram);
                String message;
                message = new String(buffer, 0, datagram.getLength(), "UTF-8");
                if (message.startsWith("NICK"))
                    checkNicksUniqe(message);
                else if (message.startsWith("MSG"))
                    printMSG(message);
                else if (message.startsWith("JOIN") || message.startsWith(("LEFT")))
                    joinOrLeftRoomMessage(message);
                else if (message.startsWith("WHOISE"))
                    whoIsInMyRoom(message);
                else if (message.startsWith("ROOM"))
                    createCommunicatAboutRoom(message);
            } catch (IOException | InterruptedException e) {}
        }
    }

    private void createCommunicatAboutRoom(String message)
    {
        String splitString[] = message.split(" ");
        if (splitString[1].equalsIgnoreCase(Main.room) && Main.myWhois == true)
            usersInRoom.add(splitString[2]);

    }

    public void printUserInRoom()
    {
        System.out.println("Osoby w pokoju: ");
        for (String s : usersInRoom)
            System.out.println(" - " + s);
        usersInRoom.clear();
    }

    private void whoIsInMyRoom(String message) throws InterruptedException {
        String splitString[] = message.split(" ");
        if (splitString[1].equalsIgnoreCase(Main.room)) {
            Send send = new Send("ROOM " + Main.room + " " + Main.name, socket, group, port);
            send.start();
            send.join();
        }
    }

    private void joinOrLeftRoomMessage(String message)
    {
        String splitString[] = message.split(" ");
        if (splitString[1].equalsIgnoreCase(Main.room))
            System.out.println(message);
    }

    private void printMSG(String message)
    {
        String splitString[] = message.split(" ");
        if (splitString[2].equalsIgnoreCase(Main.room))
        {
            System.out.print(splitString[1] + ": ");
            for (int i = 3; i < splitString.length; i++)
                System.out.print(splitString[i] + " ");
            System.out.println();
        }
    }

    void checkNicksUniqe(String message) throws InterruptedException {
        String string[] = message.split(" ");
        if (string.length == 2)
        {
            if (string[1].equalsIgnoreCase(Main.name))
            {
                Send send = new Send("NICK " + string[1] + " BUSSY", socket, group, port);
                send.start();
                send.join();
            }
        } else if (string.length == 3 && Main.name.equalsIgnoreCase(" "))
            Main.nickUnunique = true;

    }
}
