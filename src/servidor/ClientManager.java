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
    private String clientName;

    public ClientManager(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Abre los flujos de entrada y salida
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Pide el nombre del cliente
            out.println("Introduce tu nombre: ");
            clientName = in.readLine();
            server.broadcast("Usuario " + clientName + " conectado", this);

            String message;
            // Lee los mensajes del cliente y los reenvía a todos los clientes con un formato de fecha, hora y nombre del cliente
            while ((message = in.readLine()) != null) {
                String timestamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                String formattedMessage = String.format("[%s] %s: %s", timestamp, clientName, message);
                System.out.println(formattedMessage);
                server.broadcast(formattedMessage, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    // Este metodo se encarga de cerrar el cliente
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
}