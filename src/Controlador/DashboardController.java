package src.Controlador;

import src.Modelo.SteamApiService;
import src.Vista.DashboardView;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class DashboardController {
    private final SteamApiService steamApiService;
    private final DashboardView dashboardView;
    private final String username;
    private List<Game> games;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;
    private List<Game> favoriteGames = new ArrayList<>(); // Lista de juegos favoritos

    public DashboardController(SteamApiService service, DashboardView view, String username) {
        this.steamApiService = service;
        this.dashboardView = view;
        this.username = username;

        // Configurar botón de siguiente
        dashboardView.nextButton.addActionListener(e -> nextPage());
        dashboardView.prevButton.addActionListener(e -> prevPage());
        // Cargar juegos del usuario al iniciar el cuadro de mando
        fetchGames();
    }

    private void fetchGames() {
        try {
            String steamId64 = steamApiService.getSteamIdFromUsername(username);
            games = steamApiService.getOwnedGames(steamId64);
            currentPage = 0;
            displayPage();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dashboardView.frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayPage() {
        dashboardView.gamesPanel.removeAll();

        if (games == null || games.isEmpty()) {
            dashboardView.gamesPanel.add(new JLabel("No games found."));
        } else {
            // Ordenar juegos
            games.sort(Comparator.comparingLong(Game::getPlaytimeForever).reversed());

            int start = currentPage * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, games.size());
            for (int i = start; i < end; i++) {
                Game game = games.get(i);

                JPanel gamePanel = new JPanel();
                gamePanel.setLayout(new BorderLayout());
                gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                // Cargar la imagen del juego
                String imageUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/"
                        + game.getAppid() + "/" + game.getImgIconUrl() + ".jpg";
                try {
                    URL url = new URL(imageUrl);
                    Image image = ImageIO.read(url);
                    if (image != null) {
                        ImageIcon icon = new ImageIcon(image);
                        JLabel imageLabel = new JLabel(icon);
                        gamePanel.add(imageLabel, BorderLayout.WEST);
                    } else {
                        gamePanel.add(new JLabel("No Image"), BorderLayout.WEST);
                    }
                } catch (IOException ex) {
                    gamePanel.add(new JLabel("Failed to load image"), BorderLayout.WEST);
                }

                // Añadir el nombre y tiempo de juego
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.add(new JLabel("Game: " + game.getName()));
                textPanel.add(new JLabel("Playtime (hours): " + game.getPlaytimeForever() / 60.0));

                // Botón para marcar como favorito
                JButton favoriteButton = new JButton("Añadir a Favoritos");
                favoriteButton.addActionListener(e -> addFavorite(game));
                textPanel.add(favoriteButton);

                gamePanel.add(textPanel, BorderLayout.CENTER);
                dashboardView.gamesPanel.add(gamePanel);
            }
        }

        dashboardView.gamesPanel.revalidate();
        dashboardView.gamesPanel.repaint();
    }

    private void addFavorite(Game game) {
        if (!favoriteGames.contains(game)) {
            favoriteGames.add(game);
            updateFavoritesPanel();
        }
    }

    private void updateFavoritesPanel() {
        dashboardView.favoritesPanel.removeAll();

        for (Game game : favoriteGames) {
            JPanel favoritePanel = new JPanel();
            favoritePanel.setLayout(new BorderLayout());
            JLabel gameLabel = new JLabel(game.getName());

            JButton removeButton = new JButton("Eliminar");
            removeButton.addActionListener(e -> removeFavorite(game));

            favoritePanel.add(gameLabel, BorderLayout.CENTER);
            favoritePanel.add(removeButton, BorderLayout.EAST);

            dashboardView.favoritesPanel.add(favoritePanel);
        }

        dashboardView.favoritesPanel.revalidate();
        dashboardView.favoritesPanel.repaint();
    }

    private void removeFavorite(Game game) {
        favoriteGames.remove(game);
        updateFavoritesPanel();
    }

    private void nextPage() {
        if ((currentPage + 1) * PAGE_SIZE < games.size()) {
            currentPage++;
            displayPage();
        } else {
            JOptionPane.showMessageDialog(dashboardView.frame, "No more games to display.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            displayPage();
        } else {
            JOptionPane.showMessageDialog(dashboardView.frame, "You are on the first page.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }


}
