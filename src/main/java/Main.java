/**
 * Created by neikila.
 */
import UDPServer.*;
public class Main {

    public static void main(String[] args) {
        int port = 8083;
//        Server server = new Server(port, new MessageCallback() {
//            public void apply(StringBuilder incomeMessage) {
//                System.out.println(incomeMessage.toString());
//            }
//        });
//        server.run();
        try {
            new UDPServer().start();
        } catch (Exception e) {

        }
    }
}
