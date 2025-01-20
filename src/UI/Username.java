package UI;

import javax.swing.*;
import java.awt.*;

public class Username {
    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JTextField textField;
    private JButton button;

    public Username() {
        frame = new JFrame("Username");
        panel = new JPanel();
        label = new JLabel("Enter your username:");
        textField = new JTextField(20);
        button = new JButton("Enter");

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}