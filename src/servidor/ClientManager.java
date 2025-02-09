package servidor;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientManager implements Runnable {
    private Socket socket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedWriter writer;
    private String clientName;

    public ClientManager(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.writer = new BufferedWriter(new FileWriter("src/servidor/log.txt", true));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            readUsername();
            openInOut();
            readMessage();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stop();
            server.broadcastUserList();
        }
    }

    // Metodo para enviar un mensaje al cliente
    public void sendMessage(String message) {
        out.println(message);
    }

    // Metodo para cerrar la conexion con el cliente
    public void stop() {
        try {
            in.close();
            out.close();
            socket.close();
            server.removeClient(this);
            server.broadcastUserList();
            System.out.println("Cliente " + clientName + " desconectado");
            server.broadcast("Usuario " + clientName + " desconectado");
            writer.write("Usuario " + clientName + " desconectado\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error al cerrar el cliente");
        }
    }

    // Metodo para obtener el nombre del cliente
    public String getClientName() {
        return clientName;
    }

    // Metodo para leer los mensajes del cliente
    public synchronized void readMessage() {
        try {
            // El servidor lee los mensajes del cliente y los reenvia a todos los clientes conectados
            String message;
            while ((message = in.readLine()) != null) {
                String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                String formattedMessage = String.format("[%s] %s: %s", timestamp, clientName, message);
                System.out.println(formattedMessage);
                server.broadcast(formattedMessage);
                writer.write(formattedMessage + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para abrir los flujos de entrada y salida
    public void openInOut() {
        try {
            // Se crea un flujo de entrada y salida para comunicarse con el cliente
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo para leer el nombre del cliente y enviar un mensaje de conexion
    public void readUsername() {
        try {
            // Se lee el nombre del cliente
            clientName = in.readLine();
            System.out.println("Cliente " + clientName + " conectado desde " + socket.getRemoteSocketAddress());
            server.broadcast("Usuario " + clientName + " conectado");
            writer.write("Usuario " + clientName + " conectado\n");
            writer.flush();
            server.broadcastUserList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}