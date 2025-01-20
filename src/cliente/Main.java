package cliente;

import UI.Chat;
import UI.Username;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new Username();

        try {
            Client client = new Client("localhost", 49000);
            client.start();
            new Chat();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor");
        }
    }
}