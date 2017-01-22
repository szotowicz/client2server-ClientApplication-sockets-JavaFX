
package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javafx.concurrent.Task;

/**
 *
 * @author Mikołaj
 */
public class Sending extends Task<Void> {

    File file = null;
    String serverName = null;
    int portNumber = 0;

    public Sending(File file, String serverName, int portName) {
        this.file = file;
        this.serverName = serverName;
        this.portNumber = portName;
    }

    @Override
    protected Void call() throws Exception {
        try{
            updateMessage("Connecting to server...");
            Thread.sleep(200);
            
            Socket socket = new Socket(serverName, portNumber);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(file.getName());
            
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) 500];
            
            int dataSize;
            int fileSize = 0;
            
            while ((dataSize = fileInputStream.read(buffer)) > -1)
            {
                fileSize += dataSize;
                objectOutputStream.write(buffer, 0, dataSize);

                updateProgress(fileSize, file.length());
                updateMessage("Sending: " + fileSize + " bits");
                Thread.sleep(100);
            }
            objectOutputStream.close();
            updateMessage("Upload finished. You have sent " + fileSize + " bits");
            updateProgress(1, 1);
        }
        catch(Exception e){
            System.err.println("Connection to the server failed. Error: " + e);
        }
        return null;
    }
}