package telran.server.chat.tasks;

import telran.mediation.IBlkQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatServerReceiver implements Runnable {
    Socket socket;
    IBlkQueue<String> messageBox;


    public ChatServerReceiver(Socket socket, IBlkQueue<String> messageBox) {
        this.socket = socket;
        this.messageBox = messageBox;
    }

    @Override
    public void run() {
        try(Socket socket = this.socket) {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            while (true){
                String message = ois.readObject().toString();
                messageBox.push(message);

            }

        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connection to Client: " + socket.getInetAddress() + ":" + socket.getPort() + " connection closed");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}

