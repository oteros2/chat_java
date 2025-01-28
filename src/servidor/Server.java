package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private List<ClientManager> clients;

    public Server(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        clients = new ArrayList<>();
    }

    // Se inicia el servidor y se queda constantemente esperando conexiones
    public void start() {
        System.out.println("Servidor iniciado. Esperando conexiones...");
        while (true) {
            try {
                // El servidor cuando recibe una conexion, crea un nuevo socket para atender al cliente
                Socket socket = serverSocket.accept();
                // Se crea un nuevo hilo para atender al cliente, se le asigna el socket creado y el propio servidor
                ClientManager clientManager = new ClientManager(socket, this);
                addClient(clientManager);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Se añade un cliente a la lista de clientes y se inicia un hilo para atenderlo
    public synchronized void addClient(ClientManager clientManager) {
        clients.add(clientManager);
        new Thread(clientManager).start();
    }

    // Metodo para cerrar el servidor y desconectar a todos los clientes
    public void stop() {
        try {
            serverSocket.close();
            for (ClientManager client : clients) {
                client.stop();
            }
            System.out.println("Servidor cerrado");
        } catch (IOException e) {
            System.out.println("Error al cerrar el servidor");
        }
    }

    // Metodo para enviar un mensaje a todos los clientes conectados
    public synchronized void broadcast(String message) {
        for (ClientManager client : clients) {
                client.sendMessage(message);
            }
    }

    // Metodo para enviar la lista de usuarios conectados a todos los clientes
    public synchronized void broadcastUserList() {
        List<String> users = new ArrayList<>();
        for (ClientManager client : clients) {
            users.add(client.getClientName());
        }
        String userList = "USERLIST:" + String.join(",", users);
        broadcast(userList);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(49000);
            server.start();
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor");
        }
    }
}