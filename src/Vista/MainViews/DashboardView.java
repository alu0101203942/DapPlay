package src.Vista.MainViews;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
public class DashboardView {
    public JFrame frame;
    public JPanel userPanel, favoritesPanel, gamesPanel, statsPanel, achievementsPanel, friendsPanel, gameplayPanel;
    public JButton nextButton, prevButton;
    public JComboBox<String> sortComboBox;
    public JButton openNewDashboardButton;
    public JComboBox<String> chartTypeComboBox;
    public JLabel avatarLabel, usernameLabel, gamesCountLabel, profileStatusLabel, connectionStatusLabel;
    public JButton viewProfileButton;

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
        frame.setSize(1200, 800);
        frame.setLayout(new BorderLayout());

        // Panel principal del cuadro de mando
        JPanel dashboardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Panel de información del usuario
        userPanel = new JPanel(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("Información del Usuario"));
        userPanel.setPreferredSize(new Dimension(250, 120)); // Tamaño reducido

        // Avatar
        avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(SwingConstants.LEFT);
        avatarLabel.setPreferredSize(new Dimension(100, 100));

        // Panel para el texto
        usernameLabel = new JLabel("Nombre: Lecxyu");
        gamesCountLabel = new JLabel("Juegos: 410");
        profileStatusLabel = new JLabel("Estado del Perfil: Público");
        connectionStatusLabel = new JLabel("Conexión: Online");
        viewProfileButton = new JButton("Ver Perfil en Steam");

        // Crear un panel para la información del usuario
        JPanel userInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcText = new GridBagConstraints();
        gbcText.fill = GridBagConstraints.HORIZONTAL;
        gbcText.insets = new Insets(5, 5, 5, 5);

        // Añadir texto al panel de información
        gbcText.gridx = 0;
        gbcText.gridy = 0;
        gbcText.anchor = GridBagConstraints.CENTER;
        userInfoPanel.add(usernameLabel, gbcText);

        gbcText.gridy = 1;
        userInfoPanel.add(gamesCountLabel, gbcText);

        gbcText.gridy = 2;
        userInfoPanel.add(profileStatusLabel, gbcText);

        gbcText.gridy = 3;
        userInfoPanel.add(connectionStatusLabel, gbcText);

        gbcText.gridy = 4;
        userInfoPanel.add(viewProfileButton, gbcText);

        // Combinar avatar y texto en un panel
        JPanel userContentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcContent = new GridBagConstraints();

        gbcContent.gridx = 0;
        gbcContent.gridy = 0;
        gbcContent.insets = new Insets(5, 5, 5, 5);
        gbcContent.anchor = GridBagConstraints.CENTER;
        userContentPanel.add(avatarLabel, gbcContent);

        gbcContent.gridx = 1;
        gbcContent.gridy = 0;
        gbcContent.fill = GridBagConstraints.BOTH;
        userContentPanel.add(userInfoPanel, gbcContent);

        // Añadir contenido al panel principal
        userPanel.add(userContentPanel, BorderLayout.CENTER);


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.1;
        gbc.weighty = 0.01;
        dashboardPanel.add(userPanel, gbc);

        // Panel de juegos favoritos
        favoritesPanel = new JPanel();
        favoritesPanel.setPreferredSize(new Dimension(250, 300));
        favoritesPanel.setBorder(BorderFactory.createTitledBorder("Juegos Favoritos"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.4;
        dashboardPanel.add(favoritesPanel, gbc);

        // Panel de gameplays
        gameplayPanel = new JPanel();
        gameplayPanel.setPreferredSize(new Dimension(250, 300));
        gameplayPanel.setBorder(BorderFactory.createTitledBorder("Gameplays"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.4;
        dashboardPanel.add(gameplayPanel, gbc);

        // Panel de estadísticas
        statsPanel = new JPanel(new BorderLayout());
        statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas"));
        statsPanel.setPreferredSize(new Dimension(500, 500));
        chartTypeComboBox = new JComboBox<>(new String[]{
                "Gráfico de Barras",
                "Gráfico de Sectores",
                "Gráfico de Líneas",
                "Gráfico de Dispersión"
        });
        statsPanel.add(chartTypeComboBox, BorderLayout.NORTH);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.4;
        gbc.weighty = 0.6;
        dashboardPanel.add(statsPanel, gbc);

        // Panel de juegos del usuario
        gamesPanel = new JPanel();
        gamesPanel.setBorder(BorderFactory.createTitledBorder("Juegos del Usuario"));
        statsPanel.setPreferredSize(new Dimension(500, 300));
        gamesPanel.setLayout(new GridLayout(5, 1, 10, 10));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.weighty = 0.4;
        dashboardPanel.add(gamesPanel, gbc);

        // Panel de amigos
        friendsPanel = new JPanel();
        friendsPanel.setBorder(BorderFactory.createTitledBorder("Amigos"));
        friendsPanel.setPreferredSize(new Dimension(300, 400));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 0.2;
        dashboardPanel.add(friendsPanel, gbc);

        // Panel de logros
        achievementsPanel = new JPanel();
        achievementsPanel.setBorder(BorderFactory.createTitledBorder("Logros"));
        achievementsPanel.setPreferredSize(new Dimension(300, 400));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        gbc.weightx = 0.3;
        gbc.weighty = 0.2;
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

        frame.add(dashboardPanel, BorderLayout.CENTER);
        frame.add(navigationPanel, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }
}
