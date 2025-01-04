package src.Vista;



import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import src.Controlador.DashboardController;
import src.Controlador.GameplayController;
import src.Modelo.FavoritesManager;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewManager {
    private final DashboardView dashboardView;
    private final DashboardController dashboardController;
    private GameplayController gameplayController; // Cambiar a variable de instancia

    public ViewManager(DashboardView view, DashboardController dashboardController) {
        this.dashboardView = view;
        this.dashboardController = dashboardController;
    }

    public void setGameplayController(GameplayController gameplayController) {
        this.gameplayController = gameplayController;
    }

    public void displayUserInfo(Player user, int gameCount) {
        ImageIcon avatarIcon = new ImageIcon(user.getAvatarfull());
        dashboardView.avatarLabel.setIcon(avatarIcon);
        dashboardView.usernameLabel.setText("Username: " + user.getPersonaname());

        dashboardView.gamesCountLabel.setText("Number of Games: " + gameCount);

        ImageIcon backgroundIcon = new ImageIcon(user.getProfileurl()); // Assuming profileurl contains the background image URL
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        dashboardView.backgroundPanel.add(backgroundLabel, BorderLayout.CENTER);

        dashboardView.userPanel.revalidate();
        dashboardView.userPanel.repaint();
    }

    public void displayGames(List<Game> games, int currentPage, int pageSize, FavoritesManager favoritesManager) {
        dashboardView.gamesPanel.removeAll();
        dashboardView.gamesPanel.setLayout(new BorderLayout());

        JPanel gamesListPanel = new JPanel();
        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.Y_AXIS));
        GamePanelFactory gamePanelFactory = new GamePanelFactory();

        int start = currentPage * pageSize;
        int end = Math.min(start + pageSize, games.size());
        for (int i = start; i < end; i++) {
            Game game = games.get(i);

            // Crear panel para el juego
            JPanel gamePanel = gamePanelFactory.createPanel(game, e -> favoritesManager.addFavorite(game));

            // Crear un panel para los botones
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Organizar botones horizontalmente

            // Botón para ver logros
            JButton achievementsButton = new JButton("View Achievements");
            achievementsButton.addActionListener(e -> dashboardController.fetchAchievements(game));
            buttonsPanel.add(achievementsButton); // Agregar al panel de botones

            // Botón para ver gameplay
            JButton gameplayButton = new JButton("View Gameplay");
            gameplayButton.addActionListener(e -> dashboardController.viewGameplay(game));
            buttonsPanel.add(gameplayButton); // Agregar al panel de botones

            // Agregar el panel de botones al sur del gamePanel
            gamePanel.add(buttonsPanel, BorderLayout.SOUTH);

            gamesListPanel.add(gamePanel);
        }

        JScrollPane scrollPane = new JScrollPane(gamesListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.gamesPanel.getWidth(), 6 * 200));
        dashboardView.gamesPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardView.gamesPanel.revalidate();
        dashboardView.gamesPanel.repaint();
    }

    public void displayFriends(List<Player> friends) {
        dashboardView.friendsPanel.removeAll();
        dashboardView.friendsPanel.setLayout(new BorderLayout());

        JPanel friendsListPanel = new JPanel();
        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
        FriendPanelFactory friendPanelFactory = new FriendPanelFactory();

        for (Player friend : friends) {
            JPanel friendPanel = friendPanelFactory.createPanel(friend);
            friendsListPanel.add(friendPanel);
        }

        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
        scrollPane.setPreferredSize(new Dimension(dashboardView.friendsPanel.getWidth(), 4 * 100));
        dashboardView.friendsPanel.add(scrollPane, BorderLayout.CENTER);

        dashboardView.friendsPanel.revalidate();
        dashboardView.friendsPanel.repaint();
    }

    public void displayAchievements(List<Map<String, Object>> unlockedAchievements, List<Map<String, Object>> lockedAchievements) throws MalformedURLException {
        dashboardView.achievementsPanel.removeAll();
        dashboardView.achievementsPanel.setLayout(new GridLayout(1, 2)); // Dividir en dos columnas

        AchievementPanelFactory achievementPanelFactory = new AchievementPanelFactory();

        JPanel unlockedContainer = achievementPanelFactory.createPanel(unlockedAchievements, "Unlocked Achievements", Color.GREEN);
        dashboardView.achievementsPanel.add(unlockedContainer);

        JPanel lockedContainer = achievementPanelFactory.createPanel(lockedAchievements, "Locked Achievements", Color.RED);
        dashboardView.achievementsPanel.add(lockedContainer);

        dashboardView.achievementsPanel.revalidate();
        dashboardView.achievementsPanel.repaint();
    }

    public void updateFavorites(List<Game> updatedGames, FavoritesManager favoritesManager) {
        dashboardView.favoritesPanel.removeAll();
        FavoritePanelFactory favoritePanelFactory = new FavoritePanelFactory();

        List<Game> favoriteGames = favoritesManager.getFavoriteGames();

        for (Game game : favoriteGames) {
            JPanel favoritePanel = favoritePanelFactory.createPanel(game, e -> favoritesManager.removeFavorite(game)); // Añadir lógica para eliminar
            dashboardView.favoritesPanel.add(favoritePanel);
        }
        updateChart(Objects.requireNonNull(dashboardView.chartTypeComboBox.getSelectedItem()).toString(), favoriteGames);
        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
    }

    public void updateChart(String chartType, List<Game> favoriteGames) {
        dashboardView.statsPanel.removeAll();
        dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH);

        // Crear el gráfico con el ChartPanelFactory
        ChartPanelFactory chartPanelFactory = new ChartPanelFactory();
        JPanel chartPanel = chartPanelFactory.createChart(chartType, favoriteGames);

        // Agregar un MouseListener al panel del gráfico
        chartPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showChartInNewWindow(chartPanel); // Mostrar el gráfico en una ventana emergente
            }
        });

        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
        dashboardView.statsPanel.revalidate();
        dashboardView.statsPanel.repaint();
    }

    private void showChartInNewWindow(JPanel chartPanel) {
        // Crear un nuevo JFrame para mostrar el gráfico
        JFrame frame = new JFrame("Gráfico Ampliado");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo la ventana del gráfico
        frame.setSize(800, 600); // Tamaño de la ventana

        // Crear un contenedor para el gráfico
        JPanel enlargedChartPanel = new JPanel(new BorderLayout());
        enlargedChartPanel.add(chartPanel, BorderLayout.CENTER); // Agregar el gráfico al panel

        // Agregar el panel al frame
        frame.add(enlargedChartPanel);

        // Mostrar la ventana
        frame.setVisible(true);
    }



    public void playGameplay(String videoUrl) {
        dashboardView.favoritesInfoPanel.removeAll(); // Limpiar el contenido anterior del panel

        JFXPanel jfxPanel = new JFXPanel(); // Crear un panel JavaFX
        dashboardView.favoritesInfoPanel.add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            WebView webView = new WebView();
            webView.getEngine().load(videoUrl); // Cargar el video en el WebView

            Scene scene = new Scene(webView);
            jfxPanel.setScene(scene);
        });

        dashboardView.favoritesInfoPanel.revalidate(); // Actualizar el panel para reflejar los cambios
        dashboardView.favoritesInfoPanel.repaint();
    }

}
