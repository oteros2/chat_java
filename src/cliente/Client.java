package cliente;

import cliente.interfaz.Chat;
import cliente.interfaz.Username;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private Chat chat;
    private Thread readMessageThread;

    public Client(String address, int port) throws IOException {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado al servidor en " + address + ":" + port);
    }

    public void setUsername(String username) {
        this.username = username;
        out.println(username);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void start() {
        readMessageThread = new Thread(new ReadMessage());
        readMessageThread.start();
    }

    public void stop() {
        try {
            readMessageThread.interrupt();
            in.close();
            out.close();
            socket.close();
            System.exit(0);
        } catch (IOException e) {
           JOptionPane.showMessageDialog(null, "Error al desconectar del servidor",
                   "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ReadMessage implements Runnable {
        @Override
        public void run() {
            try {
                while (Thread.currentThread().isAlive()) {
                    String message = in.readLine();
                    if (chat != null) {
                        chat.addMessage(message);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al leer mensajes del servidor",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 49000);
            client.start();
            new Username(client);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
