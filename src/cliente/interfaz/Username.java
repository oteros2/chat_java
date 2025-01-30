package cliente.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import cliente.Client;
import com.formdev.flatlaf.FlatLightLaf;

public class Username {
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JTextField textField;
    private JButton button;
    private String username;

    // Colores modernos
    private final Color PRIMARY_COLOR = new Color(88, 86, 214);    // Violeta moderno
    private final Color BACKGROUND_COLOR = new Color(245, 247, 251); // Gris muy claro
    private final Color ACCENT_COLOR = new Color(94, 92, 230);     // Violeta más claro
    private final Color TEXT_COLOR = new Color(51, 51, 51);        // Gris oscuro

    public Username(Client client) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            customizeUIComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setupComponents();
        setupLayout();
        setupActions(client);
    }

    private void customizeUIComponents() {
        UIManager.put("TextField.arc", 10);
        UIManager.put("Button.arc", 10);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Frame.background", BACKGROUND_COLOR);
    }

    private void setupComponents() {
        frame = new JFrame("Bienvenido al Chat");
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibuja formas decorativas
                g2d.setColor(new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 30));
                g2d.fillOval(-50, -50, 200, 200);
                g2d.fillOval(frame.getWidth() - 100, frame.getHeight() - 100, 150, 150);
            }
        };

        // Crear y estilizar la etiqueta
        label = new JLabel("¡Únete al chat!");
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(PRIMARY_COLOR);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Ingresa tu nombre de usuario para comenzar");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_COLOR);

        // Campo de texto estilizado
        textField = new JTextField(20);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setPreferredSize(new Dimension(300, 40));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        textField.setBackground(Color.WHITE);

        // Botón estilizado
        button = new JButton("Comenzar");
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 45));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover para el botón
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
    }

    private void setupLayout() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Agregar logo o icono (puedes reemplazarlo con tu propio icono)
        JLabel iconLabel = new JLabel("💬", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 30, 0, 30);
        panel.add(iconLabel, gbc);

        // Título
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 30, 5, 30);
        panel.add(label, gbc);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Ingresa tu nombre de usuario para comenzar", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_COLOR);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 30, 20, 30);
        panel.add(subtitleLabel, gbc);

        // Campo de texto
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 30, 10, 30);
        panel.add(textField, gbc);

        // Botón
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 30, 30, 30);
        panel.add(button, gbc);

        frame.add(panel);
        frame.setSize(450, 500);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void setupActions(Client client) {
        button.addActionListener(e -> {
            try {
                username = textField.getText().trim();
                if (!username.isEmpty()) {
                    client.setUsername(username);
                    Chat chat = new Chat(username, client);
                    client.setChat(chat);
                    client.start();
                    frame.dispose();
                } else {
                    showError("Por favor ingresa un nombre de usuario");
                }
            } catch (Exception ex) {
                showError("Error al conectar con el servidor");
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}