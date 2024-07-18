import utils.RedisConnectionUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main {
  public static void main(String[] args){
    System.out.println("Logs from your program will appear here!");
    ExecutorService threadPool = Executors.newCachedThreadPool();
    ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;


        try {

          serverSocket = new ServerSocket(port);
          serverSocket.setReuseAddress(true);
          while(true) {
              clientSocket = serverSocket.accept();
              threadPool.execute(new RedisConnectionUtils(clientSocket));

          }
        } catch (IOException e) {

          System.out.println("IOException: " + e.getMessage());

        } finally {
          try {
            if (clientSocket != null) {
              clientSocket.close();
            }
          } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
          }
        }
  }
}
