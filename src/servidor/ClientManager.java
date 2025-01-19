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
            out.print("Introduce tu nombre: ");
            clientName = in.readLine();

            String message;
            // Lee los mensajes del cliente y los reenvía a todos los clientes con un formato de fecha, hora y nombre del cliente
            while ((message = in.readLine()) != null) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String formattedMessage = String.format("[%s] %s: %s", timestamp, clientName, message);
                System.out.println("Recibido: " + formattedMessage);
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
}