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
                        new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
                );

                BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(
                        clientSocket.getOutputStream(), StandardCharsets.UTF_8
                ));
        )

        {
            String line;

            while((line = reader.readLine()) != null){
                System.out.println(line);
                if(line.equals("ping".equalsIgnoreCase(line))){
                    writer.write("+PONG\r\n");
                    writer.flush();
                }
                else if("eof".equalsIgnoreCase(line)){
                    System.out.println("Client issues EOF");
                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                clientSocket.close();
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
