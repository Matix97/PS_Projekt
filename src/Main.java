import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Main {

    private static String ip = "239.0.0.0";
    private static int port = 3301;
    public static String name = " ";
    private static Scanner scanner = new Scanner(System.in);
    public static boolean koniec = false;
    public static boolean nickUnunique = true;
    public static String room;

    public static void main(String[] args) {

        try {
            InetAddress group = InetAddress.getByName(ip);
            MulticastSocket multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);
            Listen listen = new Listen(multicastSocket, group, port);
            listen.start();
            getUniqueName(multicastSocket, group);
            chooseRoom();
            System.out.println("Write a message or type \"End\" to exit");
            while (true) {

                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("End")) {
                    koniec = true;
                    break;
                } else {
                    Send send = new Send("MSG "+name + " "+room+" " + msg, multicastSocket, group, port);
                    send.start();
                    send.join();
                }
            }


            listen.join();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void chooseRoom() {
        System.out.println("Podaj nazwę pokoju: ");
        room = scanner.nextLine();
    }

    private static void getUniqueName(MulticastSocket multicastSocket, InetAddress group) throws InterruptedException {
        String tempName = "";
        while (nickUnunique) {
            nickUnunique = false;
            System.out.println("Podaj nick: ");
            tempName = scanner.nextLine();
            if (!tempName.equalsIgnoreCase("NICK") && !tempName.equalsIgnoreCase(" ")) {
                Send send = new Send("NICK " + tempName, multicastSocket, group, port);
                send.start();
                sleep(3000);
                send.join();
            } else {
                System.out.println("Nick cannot be a key word \"NICK\" or empty word");
                nickUnunique = true;
            }
        }
        name = tempName;

    }
}
