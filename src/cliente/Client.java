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

    // Se envia el nombre de usuario al servidor
    public void setUsername(String username) {
        this.username = username;
        out.println(username);
    }

    // Se asigna la interfaz grafica del chat al cliente
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    // Metodo para enviar un mensaje al servidor
    public void sendMessage(String message) {
        out.println(message);
    }

    // Se inicia un hilo para leer los mensajes del servidor
    public void start() {
        readMessageThread = new Thread(new ReadMessage());
        readMessageThread.start();
    }

    // Cierra el cliente
    public void stop() {
        System.exit(0);
    }

    // Clase interna para leer los mensajes del servidor
    private class ReadMessage implements Runnable {
        @Override
        public void run() {
            try {
                // El cliente lee los mensajes del servidor y los muestra en la interfaz grafica
                while (Thread.currentThread().isAlive()) {
                    String message = in.readLine();
                    if (chat != null) {
                        // Si el mensaje contiene el nombre de usuario, se reemplaza por "Yo"
                        if (message.contains(username + ":")) {
                            message = message.replace(username + ":", "Yo:");
                        }
                        // Se añade el mensaje a la interfaz grafica
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
