package UI;

import javax.swing.*;
import java.awt.*;

public class Chat {
    private JFrame frame;
    private JPanel panel;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;
    private JButton disconnectButton;

    public Chat() {
        frame = new JFrame("Chat");
        panel = new JPanel();
        textArea = new JTextArea(20, 30);
        textField = new JTextField(25);
        sendButton = new JButton("Enviar");
        disconnectButton = new JButton("Desconectar");

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(textField);
        inputPanel.add(sendButton);
        inputPanel.add(disconnectButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

