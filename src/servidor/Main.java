package servidor;

import UI.Chat;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(49000);
            server.start();
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor");
        }
    }
}