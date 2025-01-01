package src.Vista;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainView {
    public JFrame frame;
    public JTextField usernameField;
    public JButton fetchButton, prevButton, nextButton;
    public JComboBox<String> sortOptions;
    public JPanel resultPanel;
    public JLabel statusBar;

    public MainView() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Configuración de la interfaz gráfica (GUI)
        frame = new JFrame("Steam API Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Steam API Client");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField("Enter Steam Username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));

        fetchButton = new JButton("Fetch Owned Games");
        fetchButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        sortOptions = new JComboBox<>(new String[]{"Sort by Playtime Ascending", "Sort by Playtime Descending"});

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");

        statusBar = new JLabel("Ready");

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fetchButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(sortOptions);

        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(resultPanel));

        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(statusBar, BorderLayout.NORTH);
        southPanel.add(navigationPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(BorderLayout.SOUTH, southPanel);
    }

    public void show() {
        frame.setVisible(true);
    }
}