package cliente.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import cliente.Client;

public class Username {
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JTextField textField;
    private JButton button;
    private String username;

    public Username(Client client) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Nombre de Usuario");
        panel = new JPanel();
        label = new JLabel("Introduzca nombre de usuario:");
        textField = new JTextField(20);
        button = new JButton("Enter");

        button.setBackground(Color.GREEN);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(label, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(textField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(button, gbc);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        button.addActionListener(e -> {
            try {
                username = textField.getText();
                client.setUsername(username);
                Chat chat = new Chat(username, client);
                client.setChat(chat);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al conectar con el servidor",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick();
                }
            }
        });
    }
}