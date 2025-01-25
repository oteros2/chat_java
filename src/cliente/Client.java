package cliente;

import cliente.interfaz.Chat;
import cliente.interfaz.Username;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private Chat chat;

    public Client(String address, int port) throws IOException {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado al servidor en " + address + ":" + port);
        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor");
        }
    }

    public void setUsername(String username) {
        this.username = username;
        out.println(username);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void start() {
        new Thread(new ReadMessage()).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            out.println(message);
        }
    }

    public void stop() {
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("Cliente desconectado");
        } catch (IOException e) {
            System.out.println("Error al cerrar el cliente");
        }
    }

    private class ReadMessage implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                    if (chat != null){
                        chat.addMessage(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al leer mensajes del servidor");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 49000);
            new Username(client);
            client.start();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor");
        }
    }
}
