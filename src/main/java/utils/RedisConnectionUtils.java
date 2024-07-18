package utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RedisConnectionUtils {

    private Socket clientSocket;

    public RedisConnectionUtils(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handleConnectionRequest(){

        try(

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream())
                );

                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(
                        clientSocket.getOutputStream()
                ));
        )

        {
            String line;

            while((line = reader.readLine()) != null){
                System.out.println("::" + line);
                if("ping".equalsIgnoreCase(line)){
                    writer.write("+PONG\r\n");
                    writer.flush();
                }
                else if("echo".equalsIgnoreCase(line)){
                    String message = reader.readLine();
                    clientSocket.getOutputStream().write(
                            String.format("$%d\r\n", message.length(), message).getBytes()
                    );
                }
                else if("eof".equalsIgnoreCase(line)){
                    System.out.println("eof");
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }

    }
}
