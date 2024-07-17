package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.OutputStream;

public class RedisConnectionUtils {

    private Socket clientSocket;

    public RedisConnectionUtils(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handleConnectionRequest(){

        try(
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream outputSteam = clientSocket.getOutputStream())
        {
            String line;

            while((line = reader.readLine()) != null){
                if("PING".equalsIgnoreCase(line.trim())){
                    outputSteam.write("+PONG\r\n".getBytes());
                }
                else{
                    outputSteam.write("unknown command\r\n".getBytes());
                    outputSteam.flush();
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
