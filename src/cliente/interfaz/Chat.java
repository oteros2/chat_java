package cliente.interfaz;

import cliente.Client;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.border.EmptyBorder;
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

    // Colores modernos que coinciden con la interfaz de Username
    private final Color PRIMARY_COLOR = new Color(88, 86, 214);     // Violeta moderno
    private final Color BACKGROUND_COLOR = new Color(245, 247, 251); // Gris muy claro
    private final Color SECONDARY_COLOR = new Color(255, 255, 255); // Blanco
    private final Color TEXT_COLOR = new Color(51, 51, 51);        // Gris oscuro
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);    // Verde
    private final Color DANGER_COLOR = new Color(244, 67, 54);     // Rojo

    public Chat(String name, Client client) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            customizeUIComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupComponents(name);
        setupLayout();
        setupActions(client);
        frame.setVisible(true);
    }

    private void customizeUIComponents() {
        UIManager.put("TextField.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Frame.background", BACKGROUND_COLOR);
    }

    private void setupComponents(String name) {
        frame = new JFrame("Usuario - " + name);
        panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);

        // Área de mensajes
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPane.setBackground(SECONDARY_COLOR);
        textPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        doc = textPane.getStyledDocument();

        // Panel de usuarios
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersPanel.setBackground(SECONDARY_COLOR);
        JLabel usersTitle = new JLabel(" 👥 Usuarios conectados", SwingConstants.LEFT);
        usersTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usersTitle.setForeground(PRIMARY_COLOR);
        usersTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        usersArea = new JTextArea(20, 15);
        usersArea.setEditable(false);
        usersArea.setLineWrap(true);
        usersArea.setWrapStyleWord(true);
        usersArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usersArea.setBackground(SECONDARY_COLOR);
        usersArea.setForeground(TEXT_COLOR);
        usersArea.setBorder(new EmptyBorder(5, 10, 10, 10));

        usersPanel.add(usersTitle, BorderLayout.NORTH);
        usersPanel.add(usersArea, BorderLayout.CENTER);

        // Campo de mensaje
        textField = new JTextField(30);
        textField.setPreferredSize(new Dimension(600, 45));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        textField.setBackground(SECONDARY_COLOR);

        // Botones
        sendButton = createStyledButton("Enviar 📨", SUCCESS_COLOR);
        disconnectButton = createStyledButton("Desconectar", DANGER_COLOR);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 45));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        return button;
    }

    private void setupLayout() {
        panel.setLayout(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Chat en vivo 💬");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel);

        // Scroll panes
        JScrollPane chatScroll = new JScrollPane(textPane);
        chatScroll.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.brighter(), 1, true));

        JScrollPane usersScroll = new JScrollPane(usersArea);
        usersScroll.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR.brighter(), 1, true));

        // Panel de entrada
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.add(textField);
        inputPanel.add(sendButton);
        inputPanel.add(disconnectButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(chatScroll, BorderLayout.CENTER);
        panel.add(usersScroll, BorderLayout.EAST);
        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    private void setupActions(Client client) {
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
    }

    public void addMessage(String message) {
        try {
            if (message.startsWith("USERLIST:")) {
                String[] users = message.substring(9).split(",");
                StringBuilder usersList = new StringBuilder("Usuarios Conectados:\n");
                for (String user : users) {
                    usersList.append("👤 ").append(user).append("\n");
                }
                usersArea.setText(usersList.toString());
            } else {
                SimpleAttributeSet style = new SimpleAttributeSet();
                String formattedMessage;

                if (message.contains("Yo:")) {
                    StyleConstants.setForeground(style, PRIMARY_COLOR);
                    StyleConstants.setBold(style, true);
                    formattedMessage = message + "\n";
                } else {
                    StyleConstants.setForeground(style, TEXT_COLOR);
                    formattedMessage = message + "\n";
                }
                doc.insertString(doc.getLength(), formattedMessage, style);
                textPane.setCaretPosition(doc.getLength());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}