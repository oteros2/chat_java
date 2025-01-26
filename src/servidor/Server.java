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

    public void start() {
        System.out.println("Servidor iniciado. Esperando conexiones...");
        while (true) {
            try {
                // El servidor cuando recibe una conexion, crea un nuevo socket para atender al cliente
                Socket socket = serverSocket.accept();
                // Se crea un nuevo hilo para atender al cliente, se le asigna el socket creado y el propio servidor
                ClientManager clientManager = new ClientManager(socket, this);
                // Se añade el cliente a la lista de clientes
                clients.add(clientManager);
                // Se inicia el hilo que gestiona la entrada y la salida de mensajes del cliente
                new Thread(clientManager).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Este metodo se encarga de cerrar el servidor y de cerrar todos los clientes
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

    // Este metodo se encarga de enviar un mensaje a todos los clientes excepto al
    // propio cliente que se pasa como parametro.
    public synchronized void broadcast(String message) {
        for (ClientManager client : clients) {
                client.sendMessage(message);
            }
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