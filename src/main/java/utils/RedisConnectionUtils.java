package utils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RedisConnectionUtils implements Runnable{

    private Socket clientSocket;

    public RedisConnectionUtils(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        handleConnectionRequest();
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

                if(line.startsWith("*")){


                    int numberOfElements = Integer.parseInt(line.substring(1));

                    if(numberOfElements == 1){
                        line = reader.readLine();
                        line = reader.readLine();

                        if("ping".equalsIgnoreCase(line)){
                            writer.write("+PONG\r\n");
                            writer.flush();
                        }

                    }
                    else if(numberOfElements == 2){
                        line = reader.readLine();
                        line = reader.readLine();
                        if("echo".equalsIgnoreCase(line)){
                            line = reader.readLine();
                            line = reader.readLine();
                            String message = line;
                            writer.write(String.format("$%d\r\n%s\r\n",message.length(), message));
                            writer.flush();
                        }
                    } else if ("eof".equalsIgnoreCase(line)) {
                        System.out.println("eof");
                        break;

                    }
                }
            }
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }

    }
}
