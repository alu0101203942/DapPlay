package src.Vista;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class DashboardView {
    public JFrame frame;
    public JPanel favoritesPanel, gamesPanel, statsPanel, achievementsPanel, friendsPanel, favoritesInfoPanel;
    public JButton nextButton, prevButton; // Añadido prevButton para navegación anterior.
    public JComboBox<String> sortComboBox;
    public JButton openNewDashboardButton;
    public JComboBox<String> chartTypeComboBox;

    public DashboardView() {
        // Set FlatDarkLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Configuración de la interfaz gráfica (GUI)
        frame = new JFrame("Steam Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800); // Tamaño ajustado para acomodar todos los paneles
        frame.setLayout(new BorderLayout());

        // Crear el panel principal del cuadro de mando
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayout(2, 3, 10, 10)); // 2 filas y 3 columnas

        // Crear paneles para cada sección
        favoritesPanel = new JPanel();
        favoritesPanel.setLayout(new BoxLayout(favoritesPanel, BoxLayout.Y_AXIS));
        favoritesPanel.setBorder(BorderFactory.createTitledBorder("Juegos Favoritos"));

        // Envolver favoritesPanel en un JScrollPane
        JScrollPane favoritesScrollPane = new JScrollPane(favoritesPanel);
        favoritesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        favoritesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gamesPanel = new JPanel();
        gamesPanel.setBorder(BorderFactory.createTitledBorder("Juegos del Usuario"));
        gamesPanel.setLayout(new GridLayout(5, 1, 10, 10));

        statsPanel = new JPanel();
        chartTypeComboBox = new JComboBox<>(new String[]{"Gráfico de Barras","Gráfico de Sectores"});
        statsPanel.add(chartTypeComboBox, BorderLayout.NORTH);
        chartTypeComboBox.setSelectedIndex(0);


        friendsPanel = new JPanel();
        friendsPanel.setBorder(BorderFactory.createTitledBorder("Lista de Amigos"));

        favoritesInfoPanel = new JPanel();
        favoritesInfoPanel.setBorder(BorderFactory.createTitledBorder("Información de Favoritos"));

        achievementsPanel = new JPanel();
        achievementsPanel.setBorder(BorderFactory.createTitledBorder("Logros Desbloqueados"));

        // Añadir los paneles al layout del cuadro de mando
        dashboardPanel.add(favoritesScrollPane);
        dashboardPanel.add(statsPanel);
        dashboardPanel.add(friendsPanel);
        dashboardPanel.add(favoritesInfoPanel);
        dashboardPanel.add(gamesPanel);
        dashboardPanel.add(achievementsPanel);

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