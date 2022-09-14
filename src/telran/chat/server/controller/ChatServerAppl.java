package telran.chat.server.controller;

import telran.mediation.BlkQueue;
import telran.mediation.IBlkQueue;
import telran.server.chat.tasks.ChatServerReceiver;
import telran.server.chat.tasks.ChatServerSender;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServerAppl {
    public static void main(String[] args) {
        int port = 9000;
        IBlkQueue<String> messageBox = new BlkQueue<>(10);

        ChatServerSender sender = new ChatServerSender(messageBox);
        Thread senderThread = new Thread(sender);
        senderThread.setDaemon(true);
        senderThread.start();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                while (true) {
                    System.out.println("Server waiting...");
                    Socket socket = serverSocket.accept();
                    System.out.println("Connection established");
                    System.out.println("Client address: " + socket.getInetAddress() + ":" + socket.getPort());
                    sender.addClient(socket);
                    ChatServerReceiver receiver = new ChatServerReceiver(socket, messageBox);
                    executorService.execute(receiver);
                }
            } finally {
                executorService.shutdown();
                executorService.awaitTermination(1, TimeUnit.MINUTES);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
