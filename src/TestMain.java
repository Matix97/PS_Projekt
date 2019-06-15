import java.util.Scanner;

public class TestMain {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Write a message or type \"End\" to exit");
        while(true){

            String msg=scanner.nextLine();
            if(msg.equalsIgnoreCase("End")){
                break;
            }else {
                System.out.println(msg);
            }
        }
    }
}
