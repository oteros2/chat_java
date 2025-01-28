package cliente.interfaz;

import cliente.Client;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Chat {
    private JFrame frame;
    private JPanel panel;
    private JTextPane textPane;
    private JTextArea usersArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton disconnectButton;
    private StyledDocument doc;

    public Chat(String name, Client client) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame = new JFrame("Usuario: " + name);
        panel = new JPanel();
        textPane = new JTextPane();
        usersArea = new JTextArea(20, 15);
        textField = new JTextField(30);
        sendButton = new JButton("Enviar");
        disconnectButton = new JButton("Desconectar");
        doc = textPane.getStyledDocument();

        textField.setPreferredSize(new Dimension(600, 60));
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textPane.setEditable(false);
        textPane.setFont(new Font("Arial", Font.PLAIN, 18));

        usersArea.setEditable(false);
        usersArea.setLineWrap(true);
        usersArea.setWrapStyleWord(true);
        usersArea.setFont(new Font("Arial", Font.PLAIN, 16));
        usersArea.setText("Usuarios conectados:\n");

        sendButton.setBackground(new Color(0, 153, 76));
        disconnectButton.setBackground(new Color(204, 0, 0));

        sendButton.addActionListener(e -> {
            String message = textField.getText();
            textField.setText("");
            if (!message.isEmpty()) {
                client.sendMessage(message);
            }
        });

        disconnectButton.addActionListener(e -> {
            frame.dispose();
            client.stop();
        });

        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(textPane);
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
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Metodo para añadir un mensaje a la interfaz grafica
    public void addMessage(String message) {
        try {
            // Si el mensaje contiene "USERLIST:", se muestra la lista de usuarios conectados
            if (message.startsWith("USERLIST:")) {
                String[] users = message.substring(9).split(",");
                StringBuilder usersList = new StringBuilder("Usuarios conectados:\n");
                for (String user : users) {
                    usersList.append(user).append("\n");
                }
                usersArea.setText(usersList.toString());
            } else {
                if (message.contains("Yo:")) {
                    doc.insertString(doc.getLength(), message + "\n", createStyle(Color.BLUE));
                } else {
                    doc.insertString(doc.getLength(), message + "\n", createStyle(Color.BLACK));
                }
                textPane.setCaretPosition(doc.getLength());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    // Metodo para crear un estilo de texto con un color especifico
    private SimpleAttributeSet createStyle(Color color) {
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, color);
        return style;
    }
}