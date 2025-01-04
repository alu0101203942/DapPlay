package src.Vista;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class DashboardView {
    public JFrame frame;
    public JPanel userPanel, favoritesPanel, gamesPanel, statsPanel, achievementsPanel, friendsPanel, favoritesInfoPanel;
    public JButton nextButton, prevButton;
    public JComboBox<String> sortComboBox;
    public JButton openNewDashboardButton;
    public JComboBox<String> chartTypeComboBox;
    public JLabel avatarLabel, usernameLabel, gamesCountLabel;
    public JPanel backgroundPanel;

    public DashboardView() {
        // Set FlatDarkLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Configuración del marco principal
        frame = new JFrame("DapPlay");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800); // Tamaño ajustado para acomodar todos los paneles
        frame.setLayout(new BorderLayout());

        // Panel principal del cuadro de mando
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Panel de información del usuario
        userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("Información del Usuario"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        dashboardPanel.add(userPanel, gbc);

        // Fondo del perfil
        backgroundPanel = new JPanel(new BorderLayout());
        userPanel.add(backgroundPanel, BorderLayout.CENTER);

        // Panel de información del usuario
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        avatarLabel = new JLabel("Avatar"); // Etiqueta para el avatar
        usernameLabel = new JLabel("Usuario:"); // Etiqueta para el nombre del usuario
        gamesCountLabel = new JLabel("Número de juegos:"); // Etiqueta para el número de juegos

        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gamesCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        userInfoPanel.add(avatarLabel);
        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(gamesCountLabel);
        userPanel.add(userInfoPanel, BorderLayout.SOUTH);

        // Paneles de las demás secciones
        favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        favoritesPanel.setBorder(BorderFactory.createTitledBorder("Juegos Favoritos"));

        JScrollPane favoritesScrollPane = new JScrollPane(favoritesPanel);
        favoritesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        favoritesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gamesPanel = new JPanel();
        gamesPanel.setBorder(BorderFactory.createTitledBorder("Juegos del Usuario"));
        gamesPanel.setLayout(new GridLayout(5, 1, 10, 10));

        statsPanel = new JPanel();
        chartTypeComboBox = new JComboBox<>(new String[]{"Gráfico de Barras", "Gráfico de Sectores"});
        statsPanel.add(chartTypeComboBox, BorderLayout.NORTH);
        chartTypeComboBox.setSelectedIndex(0);

        friendsPanel = new JPanel();
        friendsPanel.setBorder(BorderFactory.createTitledBorder("Lista de Amigos"));

        favoritesInfoPanel = new JPanel();
        favoritesInfoPanel.setBorder(BorderFactory.createTitledBorder("Información de Favoritos"));

        achievementsPanel = new JPanel();
        achievementsPanel.setBorder(BorderFactory.createTitledBorder("Logros Desbloqueados"));

        // Añadir los paneles al layout del cuadro de mando
        gbc.gridwidth = 1;
        gbc.weighty = 0.4;

        gbc.gridx = 0;
        gbc.gridy = 1;
        dashboardPanel.add(favoritesScrollPane, gbc);

        gbc.gridx = 1;
        dashboardPanel.add(statsPanel, gbc);

        gbc.gridx = 2;
        dashboardPanel.add(friendsPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dashboardPanel.add(favoritesInfoPanel, gbc);

        gbc.gridx = 1;
        dashboardPanel.add(gamesPanel, gbc);

        gbc.gridx = 2;
        dashboardPanel.add(achievementsPanel, gbc);

        // Panel inferior con botones de navegación
        JPanel navigationPanel = new JPanel();
        prevButton = new JButton("Anterior");
        nextButton = new JButton("Siguiente");
        openNewDashboardButton = new JButton("Abrir Nuevo Dashboard");
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(openNewDashboardButton);
        sortComboBox = new JComboBox<>(new String[]{"Sort by Name", "Sort by Playtime"});
        navigationPanel.add(sortComboBox);

        // Agregar paneles al marco principal
        frame.add(dashboardPanel, BorderLayout.CENTER);
        frame.add(navigationPanel, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }
}
