package cliente.interfaz;

import cliente.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Chat {
    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JTextArea usersArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton disconnectButton;

    public Chat(String name, Client client) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("Usuario: " + name);
        panel = new JPanel();
        textArea = new JTextArea(30, 50);
        usersArea = new JTextArea(20, 15);
        textField = new JTextField(30);
        sendButton = new JButton("Enviar");
        disconnectButton = new JButton("Desconectar");

        textField.setPreferredSize(new Dimension(600, 60));
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        usersArea.setEditable(false);
        usersArea.setLineWrap(true);
        usersArea.setWrapStyleWord(true);
        usersArea.setText("Usuarios conectados:\n");

        sendButton.setBackground(Color.GREEN);
        disconnectButton.setBackground(Color.RED);

        sendButton.addActionListener(e -> {
            String message = textField.getText();
            textField.setText("");
            if (!message.isEmpty()) {
              client.sendMessage(message);
            }
        });

        disconnectButton.addActionListener(e -> {
            client.stop();
            frame.dispose();
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                   sendButton.doClick();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JScrollPane usersScrollPane = new JScrollPane(usersArea);
        usersScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(usersScrollPane, BorderLayout.EAST);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(textField);
        inputPanel.add(sendButton);
        inputPanel.add(disconnectButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addMessage(String message){
        textArea.append(message + "\n");
    }
}