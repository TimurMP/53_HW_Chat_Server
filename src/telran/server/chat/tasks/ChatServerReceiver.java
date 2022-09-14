package telran.server.chat.tasks;

import telran.chat.model.Message;
import telran.mediation.IBlkQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ChatServerReceiver implements Runnable {
    Socket socket;
    IBlkQueue<Message> messageBox;


    public ChatServerReceiver(Socket socket, IBlkQueue<Message> messageBox) {
        this.socket = socket;
        this.messageBox = messageBox;
    }

    @Override
    public void run() {
        try(Socket socket = this.socket) {
//            Message messageObj = null;
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (true){
                Message messageObj = (Message) ois.readObject();
//                String message = ois.readObject().toString();
//                messageBox.push(message);
                System.out.println(messageObj.getMessage());
                messageBox.push(messageObj);

            }

        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("Connection to Client: " + socket.getInetAddress() + ":" + socket.getPort() + " connection closed");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}

