package cliente;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 49000);
            client.start();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor");
        }
    }
}