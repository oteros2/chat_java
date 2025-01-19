package cliente;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String address, int port) throws IOException {
        try {
            // Se crea un socket para conectarse al servidor
            socket = new Socket(address, port);
            // Se crean los flujos de entrada y salida
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Conectado al servidor en " + address + ":" + port);
        } catch (IOException e) {
            System.out.println("No se pudo conectar al servidor");
        }
    }

    // Este metodo se encarga de leer los mensajes del servidor y de enviar los mensajes al servidor
    public void start() {
        // Se inicia un hilo que se encarga de leer los mensajes del servidor
        new Thread(new ReadMessage()).start();
        Scanner scanner = new Scanner(System.in);
        // Se lee los mensajes del usuario y se envian al servidor
        while (true) {
            String message = scanner.nextLine();
            out.println(message);
        }
    }

    // Clase interna que se encarga de leer los mensajes del servidor
    private class ReadMessage implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Error al leer mensajes del servidor");
                e.printStackTrace();
            }
        }
    }
}