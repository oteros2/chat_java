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
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Leer el nombre del cliente
            clientName = in.readLine();
            System.out.println("Cliente " + clientName + " conectado desde " + socket.getRemoteSocketAddress());
            server.broadcast("Usuario " + clientName + " conectado");
            writer.write("Usuario " + clientName + " conectado\n");
            writer.flush();

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
        } finally {
            stop();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void stop() {
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("Cliente " + clientName + " desconectado");
            server.broadcast("Usuario " + clientName + " desconectado");
            writer.write("Usuario " + clientName + " desconectado\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error al cerrar el cliente");
        }
    }

    public String getClientName() {
        return clientName;
    }
}