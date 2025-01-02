package src.Controlador;

import src.Modelo.SteamApiService;
import src.Vista.MainView;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;


public class StartController {
    private final SteamApiService steamApiService;
    private final MainView mainView;
    private List<Game> games;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 6;

    public StartController(SteamApiService steamApiService, MainView mainView) {
        this.steamApiService = steamApiService;
        this.mainView = mainView;

        mainView.fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchGames();
            }
        });

        mainView.prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    displayPage();
                }
            }
        });

        mainView.nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * PAGE_SIZE < games.size()) {
                    currentPage++;
                    displayPage();
                }
            }
        });

        mainView.sortOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPage();
            }
        });

    }

    private void fetchGames() {
        String username = mainView.usernameField.getText();
        try {
            String steamId64 = steamApiService.getSteamIdFromUsername(username);
            games = steamApiService.getOwnedGames(steamId64);
            currentPage = 0;
            displayPage();
        } catch (Exception ex) {
            mainView.statusBar.setText("Error: " + ex.getMessage());
        }
    }

    private void displayPage() {
        mainView.resultPanel.removeAll();

        if (games == null || games.isEmpty()) {
            mainView.resultPanel.add(new JLabel("No games found."));
        } else {
            String sortOption = (String) mainView.sortOptions.getSelectedItem();
            if ("Sort by Playtime Ascending".equals(sortOption)) {
                games.sort(Comparator.comparingLong(Game::getPlaytimeForever));
            } else {
                games.sort(Comparator.comparingLong(Game::getPlaytimeForever).reversed());
            }

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

                gamePanel.add(textPanel, BorderLayout.CENTER);
                mainView.resultPanel.add(gamePanel);
            }
        }

        mainView.resultPanel.revalidate();
        mainView.resultPanel.repaint();
    }


}
