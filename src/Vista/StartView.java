package src.Vista;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StartView {
    public JFrame frame;
    public JTextField usernameField;
    public JButton nextButton;

    public StartView() {
        // Configurar el estilo visual
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Configurar la ventana principal
        frame = new JFrame("Inicio - Steam Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        // Panel central para el ingreso del nombre de usuario
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Ingrese su Nombre de Usuario de Steam");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextButton = new JButton("Siguiente");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        nextButton.addActionListener(this::onNextButtonPressed);
        usernameField.addActionListener(this::onNextButtonPressed);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(nextButton);

        frame.add(panel, BorderLayout.CENTER);
    }

    private void onNextButtonPressed(ActionEvent e) {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Por favor, ingrese un nombre de usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Nombre de usuario ingresado: " + username, "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}
