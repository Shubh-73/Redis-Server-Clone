import utils.RedisConnectionUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args){
    System.out.println("Logs from your program will appear here!");

    ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {

          serverSocket = new ServerSocket(port);
          serverSocket.setReuseAddress(true);
          clientSocket = serverSocket.accept();
          //for single command
          //clientSocket.getOutputStream().write("+PONG\r\n".getBytes(StandardCharsets.UTF_8));
          RedisConnectionUtils connectionUtils = new RedisConnectionUtils(clientSocket);

          new Thread(() -> {
              try{
                  connectionUtils.handleConnectionRequest();
              }
              catch (Exception e){
                  System.out.println("exeption" + e.getMessage());
              }
          }).start();


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
