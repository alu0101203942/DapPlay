package src.Vista;



import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import src.Controlador.AchievementsController;
import src.Controlador.GameplayController;
import src.Controlador.DashboardController;
import src.Modelo.API.YoutubeApiService;
import src.Modelo.Data.FavoritesManager;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Modelo.Data.GameplayModel;
import src.Vista.MainViews.DashboardView;
import src.Vista.PanelF.*;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.List;
import java.util.Map;

public class ViewManager {
    private final DashboardView dashboardView;
    private final DashboardController dashboardController;
    private GameplayController gameplayController; // Cambiar a variable de instancia
    private final AchievementsController achievementsController;

    public ViewManager(DashboardView view, DashboardController dashboardController, AchievementsController achievementsController, YoutubeApiService youtubeApiService) {
        this.dashboardView = view;
        this.dashboardController = dashboardController;
        this.gameplayController = new GameplayController(youtubeApiService);
        this.achievementsController = achievementsController;
    }


    public void setGameplayController(GameplayController gameplayController) {
        this.gameplayController = gameplayController;
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(dashboardView.frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayUserInfo(Player user, int gameCount) {
        UserPanelFactory userPanelFactory = new UserPanelFactory();
        JPanel userPanel = userPanelFactory.createPanel(user, gameCount);

        // Reemplazar el contenido del panel en DashboardView
        dashboardView.userPanel.removeAll();
        dashboardView.userPanel.add(userPanel, BorderLayout.CENTER);

        // Actualizar la vista
        dashboardView.userPanel.revalidate();
        dashboardView.userPanel.repaint();
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

//    public void updateChart(String chartType, List<Game> favoriteGames) {
//        // Limpiar el panel actual
//        dashboardView.statsPanel.removeAll();
//        dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH);
//
//        // Crear el gráfico con el ChartPanelFactory
//        ChartPanelFactory chartPanelFactory = new ChartPanelFactory();
//        JPanel chartPanel = chartPanelFactory.createChart(chartType, favoriteGames);
//
//        // Agregar un MouseListener al panel del gráfico
//        chartPanel.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                // Mostrar el gráfico en una ventana emergente al hacer clic
//                showChartInNewWindow(chartPanel);
//            }
//        });
//
//        // Agregar el gráfico al panel principal
//        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
//
//        // Refrescar la interfaz
//        dashboardView.statsPanel.revalidate();
//        dashboardView.statsPanel.repaint();
//    }
//
//    private void showChartInNewWindow(JPanel chartPanel) {
//        // Crear un nuevo JFrame para mostrar el gráfico
//        JFrame frame = new JFrame("Gráfico Ampliado");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo la ventana del gráfico
//        frame.setSize(800, 600); // Tamaño de la ventana
//
//        // Crear un contenedor para el gráfico
//        JPanel enlargedChartPanel = new JPanel(new BorderLayout());
//        enlargedChartPanel.add(chartPanel, BorderLayout.CENTER); // Agregar el gráfico al panel
//
//        // Agregar el panel al frame
//        frame.add(enlargedChartPanel);
//
//        // Mostrar la ventana
//        frame.setVisible(true);
//    }

    // In src/Vista/ViewManager.java

    public void updateGameplayPanel(List<GameplayModel> gameplays) {
        // Verificar si hay datos válidos
        if (gameplays == null || gameplays.isEmpty()) {
            System.out.println("No hay gameplays disponibles para mostrar.");
            JLabel noGameplaysLabel = new JLabel("No se encontraron gameplays para este juego.");
            dashboardView.gameplayPanel.removeAll();
            dashboardView.gameplayPanel.add(noGameplaysLabel);
            dashboardView.gameplayPanel.revalidate();
            dashboardView.gameplayPanel.repaint();
            return;
        }

        // Limpia el contenido previo
        dashboardView.gameplayPanel.removeAll();
        dashboardView.gameplayPanel.setLayout(new BoxLayout(dashboardView.gameplayPanel, BoxLayout.Y_AXIS));

        for (GameplayModel gameplay : gameplays) {
            System.out.println("Procesando gameplay: " + gameplay.getVideoUrl());

            // Crear un panel para cada video
            JPanel videoPanel = new JPanel(new BorderLayout());
            videoPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Miniatura del video
            try {
                URL thumbnailUrl = new URL(gameplay.getThumbnailUrl());
                ImageIcon thumbnailIcon = new ImageIcon(thumbnailUrl);
                Image scaledImage = thumbnailIcon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                JLabel thumbnailLabel = new JLabel(new ImageIcon(scaledImage));
                videoPanel.add(thumbnailLabel, BorderLayout.WEST);
            } catch (Exception ex) {
                System.out.println("Error cargando miniatura: " + ex.getMessage());
                JLabel thumbnailLabel = new JLabel("Thumbnail not available");
                videoPanel.add(thumbnailLabel, BorderLayout.WEST);
            }

            // Botón para abrir en navegador
            JButton videoButton = new JButton("Open in browser");
            videoButton.addActionListener(event -> {
                try {
                    Desktop.getDesktop().browse(new URL(gameplay.getVideoUrl()).toURI());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboardView.gameplayPanel, "Error opening link: " + ex.getMessage());
                }
            });

            // Contenedor para el botón y título opcional
            JPanel centerPanel = new JPanel(new BorderLayout());
            JLabel titleLabel = new JLabel("Video: " + gameplay.getVideoUrl()); // Mostrar enlace para depuración
            centerPanel.add(titleLabel, BorderLayout.NORTH);
            centerPanel.add(videoButton, BorderLayout.CENTER);
            videoPanel.add(centerPanel, BorderLayout.CENTER);

            dashboardView.gameplayPanel.add(videoPanel);
        }

        // Forzar la actualización visual
        dashboardView.gameplayPanel.invalidate();
        dashboardView.gameplayPanel.revalidate();
        dashboardView.gameplayPanel.repaint();
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
            achievementsButton.addActionListener(e -> {
                String steamId64 = dashboardController.getUsername(); // Obtener el Steam ID del usuario
                dashboardController.fetchAchievements(steamId64, game);
            });
            buttonsPanel.add(achievementsButton);

            // Botón para ver gameplay
            JButton gameplayButton = new JButton("View Gameplay");
            gameplayButton.addActionListener(e -> dashboardController.viewGameplay(game)); // Llama al método centralizado en el DashboardController
            buttonsPanel.add(gameplayButton);

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

    public void updateFavorites(List<Game> updatedGames, FavoritesManager favoritesManager) {
        dashboardView.favoritesPanel.removeAll();
        FavoritePanelFactory favoritePanelFactory = new FavoritePanelFactory();

        List<Game> favoriteGames = favoritesManager.getFavoriteGames();

        for (Game game : favoriteGames) {
            JPanel favoritePanel = favoritePanelFactory.createPanel(game, e -> favoritesManager.removeFavorite(game));
            dashboardView.favoritesPanel.add(favoritePanel);
        }
        //updateChart(Objects.requireNonNull(dashboardView.chartTypeComboBox.getSelectedItem()).toString(), favoriteGames);
        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
    }


//    public void updateAchievementsPanel(List<GameplayModel> achievements) {
//        dashboardView.achievementsPanel.removeAll();
//        dashboardView.achievementsPanel.setLayout(new BoxLayout(dashboardView.achievementsPanel, BoxLayout.Y_AXIS));
//
//        for (GameplayModel model : achievements) {
//            JLabel achievementLabel = new JLabel(model.getVideoUrl()); // Placeholder
//            dashboardView.achievementsPanel.add(achievementLabel);
//        }
//
//        dashboardView.achievementsPanel.revalidate();
//        dashboardView.achievementsPanel.repaint();
//    }



}
//
//    public void displayFriends(List<Player> friends) {
//        dashboardView.friendsPanel.removeAll();
//        dashboardView.friendsPanel.setLayout(new BorderLayout());
//
//        JPanel friendsListPanel = new JPanel();
//        friendsListPanel.setLayout(new BoxLayout(friendsListPanel, BoxLayout.Y_AXIS));
//        FriendPanelFactory friendPanelFactory = new FriendPanelFactory();
//
//        for (Player friend : friends) {
//            JPanel friendPanel = friendPanelFactory.createPanel(friend);
//            friendsListPanel.add(friendPanel);
//        }
//
//        JScrollPane scrollPane = new JScrollPane(friendsListPanel);
//        scrollPane.setPreferredSize(new Dimension(dashboardView.friendsPanel.getWidth(), 4 * 100));
//        dashboardView.friendsPanel.add(scrollPane, BorderLayout.CENTER);
//
//        dashboardView.friendsPanel.revalidate();
//        dashboardView.friendsPanel.repaint();
//    }

//    public void displayAchievements(List<Map<String, Object>> unlockedAchievements, List<Map<String, Object>> lockedAchievements) throws MalformedURLException {
//        dashboardView.achievementsPanel.removeAll();
//        dashboardView.achievementsPanel.setLayout(new GridLayout(1, 2)); // Dividir en dos columnas
//
//        AchievementPanelFactory achievementPanelFactory = new AchievementPanelFactory();
//
//        JPanel unlockedContainer = achievementPanelFactory.createPanel(unlockedAchievements, "Unlocked Achievements", Color.GREEN);
//        dashboardView.achievementsPanel.add(unlockedContainer);
//
//        JPanel lockedContainer = achievementPanelFactory.createPanel(lockedAchievements, "Locked Achievements", Color.RED);
//        dashboardView.achievementsPanel.add(lockedContainer);
//
//        dashboardView.achievementsPanel.revalidate();
//        dashboardView.achievementsPanel.repaint();
//    }

//    public void updateFavorites(List<Game> updatedGames, FavoritesManager favoritesManager) {
//        dashboardView.favoritesPanel.removeAll();
//        FavoritePanelFactory favoritePanelFactory = new FavoritePanelFactory();
//
//        List<Game> favoriteGames = favoritesManager.getFavoriteGames();
//
//        for (Game game : favoriteGames) {
//            JPanel favoritePanel = favoritePanelFactory.createPanel(game, e -> favoritesManager.removeFavorite(game)); // Añadir lógica para eliminar
//            dashboardView.favoritesPanel.add(favoritePanel);
//        }
//        updateChart(Objects.requireNonNull(dashboardView.chartTypeComboBox.getSelectedItem()).toString(), favoriteGames);
//        dashboardView.favoritesPanel.revalidate();
//        dashboardView.favoritesPanel.repaint();
//    }
//
//    public void updateChart(String chartType, List<Game> favoriteGames) {
//        // Limpiar el panel actual
//        dashboardView.statsPanel.removeAll();
//        dashboardView.statsPanel.add(dashboardView.chartTypeComboBox, BorderLayout.NORTH);
//
//        // Crear el gráfico con el ChartPanelFactory
//        ChartPanelFactory chartPanelFactory = new ChartPanelFactory();
//        JPanel chartPanel = chartPanelFactory.createChart(chartType, favoriteGames);
//
//        // Agregar un MouseListener al panel del gráfico
//        chartPanel.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent e) {
//                // Mostrar el gráfico en una ventana emergente al hacer clic
//                showChartInNewWindow(chartPanel);
//            }
//        });
//
//        // Agregar el gráfico al panel principal
//        dashboardView.statsPanel.add(chartPanel, BorderLayout.CENTER);
//
//        // Refrescar la interfaz
//        dashboardView.statsPanel.revalidate();
//        dashboardView.statsPanel.repaint();
//    }
//
//    private void showChartInNewWindow(JPanel chartPanel) {
//        // Crear un nuevo JFrame para mostrar el gráfico
//        JFrame frame = new JFrame("Gráfico Ampliado");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo la ventana del gráfico
//        frame.setSize(800, 600); // Tamaño de la ventana
//
//        // Crear un contenedor para el gráfico
//        JPanel enlargedChartPanel = new JPanel(new BorderLayout());
//        enlargedChartPanel.add(chartPanel, BorderLayout.CENTER); // Agregar el gráfico al panel
//
//        // Agregar el panel al frame
//        frame.add(enlargedChartPanel);
//
//        // Mostrar la ventana
//        frame.setVisible(true);
//    }


//    public void displayGameplayLinks(List<VideoData> videoUrls, String gameName) {
//        JFrame frame = new JFrame("Gameplays de " + gameName);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(400, 300);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        for (VideoData video : videoUrls) {
//            JButton videoButton = new JButton(video.getVideoUrl());
//            videoButton.addActionListener(e -> openInBrowser(video.getVideoUrl()));
//            panel.add(videoButton);
//        }
//
//        JScrollPane scrollPane = new JScrollPane(panel);
//        frame.add(scrollPane);
//        frame.setVisible(true);
//    }

//    public void displayGameplayLinksWithThumbnails(List<VideoData> videoDataList, JPanel gameplayPanel) {
//        gameplayPanel.removeAll(); // Limpiar contenido anterior
//
//        // Crear un panel interno con BoxLayout para alinear elementos verticalmente
//        JPanel contentPanel = new JPanel();
//        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
//
//        for (VideoData video : videoDataList) {
//            JPanel videoPanel = new JPanel(new BorderLayout());
//            videoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Espaciado entre elementos
//
//            // Miniatura
//            JLabel thumbnailLabel = new JLabel(video.getScaledThumbnail(100, 100));
//            videoPanel.add(thumbnailLabel, BorderLayout.WEST);
//
//            // Botón de enlace
//            JButton videoButton = new JButton("Open in browser");
//            videoButton.addActionListener(event -> {
//                try {
//                    Desktop.getDesktop().browse(new URL(video.getVideoUrl()).toURI());
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(gameplayPanel, "Error opening link: " + ex.getMessage());
//                }
//            });
//            videoPanel.add(videoButton, BorderLayout.CENTER);
//
//            // Agregar el panel del video al panel de contenido
//            contentPanel.add(videoPanel);
//        }
//
//        // Agregar el panel de contenido a un JScrollPane
//        JScrollPane scrollPane = new JScrollPane(contentPanel);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//        // Reemplazar el contenido del gameplayPanel con el JScrollPane
//        gameplayPanel.setLayout(new BorderLayout());
//        gameplayPanel.add(scrollPane, BorderLayout.CENTER);
//
//        gameplayPanel.revalidate(); // Refrescar el panel
//        gameplayPanel.repaint();
//    }

//    private void showVideosInNewWindow(List<VideoData> videoDataList) {
//        JFrame frame = new JFrame("Gameplays");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setSize(800, 600);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//        for (VideoData video : videoDataList) {
//            JPanel videoPanel = new JPanel(new BorderLayout());
//
//            // Thumbnail
//            try {
//                ImageIcon thumbnailIcon = new ImageIcon(new URL(video.getThumbnailUrl()));
//                Image image = thumbnailIcon.getImage(); // Get the image
//                Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Scale the image
//                JLabel thumbnailLabel = new JLabel(new ImageIcon(scaledImage));
//                videoPanel.add(thumbnailLabel, BorderLayout.WEST);
//            } catch (Exception ex) {
//                JLabel thumbnailLabel = new JLabel("Thumbnail not available");
//                videoPanel.add(thumbnailLabel, BorderLayout.WEST);
//            }
//
//            // Link
//            JButton videoButton = new JButton("Open in browser");
//            videoButton.addActionListener(event -> {
//                try {
//                    Desktop.getDesktop().browse(new URL(video.getVideoUrl()).toURI());
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(panel, "Error opening link: " + ex.getMessage());
//                }
//            });
//            videoPanel.add(videoButton, BorderLayout.CENTER);
//
//            panel.add(videoPanel);
//        }
//
//        JScrollPane scrollPane = new JScrollPane(panel);
//        frame.add(scrollPane);
//        frame.setVisible(true);
//    }
//
//
//    private void openInBrowser(String url) {
//        try {
//            Desktop.getDesktop().browse(new URI(url));
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Error al abrir el enlace: " + e.getMessage());
//        }
//    }



