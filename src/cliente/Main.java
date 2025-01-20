package cliente;

import UI.Chat;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Chat::new);
        try {
            Client client = new Client("localhost", 49000);
            client.start();
            new Chat();
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor");
        }
    }
}