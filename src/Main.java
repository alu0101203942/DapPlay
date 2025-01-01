package src;

import com.formdev.flatlaf.FlatDarkLaf;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final String API_KEY = "06166564FA99EDCBCEDAFFF71732218B";
    private static final int PAGE_SIZE = 6;
    private static int currentPage = 0;
    private static List<Game> games;

    public static void main(String[] args) {
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Steam API Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Steam API Client");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField("Enter Steam Username");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        usernameField.setBorder(new EmptyBorder(5, 5, 5, 5));
        usernameField.setToolTipText("Enter the Steam username of the user");

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton fetchButton = new JButton("Fetch Owned Games");
        fetchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fetchButton.setToolTipText("Click to fetch the list of owned games");

        JButton prevButton = new JButton("Previous");
        prevButton.setToolTipText("Go to the previous page");
        JButton nextButton = new JButton("Next");
        nextButton.setToolTipText("Go to the next page");
        JComboBox<String> sortOptions = new JComboBox<>(new String[]{"Sort by Playtime Ascending", "Sort by Playtime Descending"});
        sortOptions.setToolTipText("Select sorting order for the games");

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                SteamApiService steamApiService = new SteamApiService(API_KEY);

                try {
                    String steamId64 = steamApiService.getSteamIdFromUsername(username);
                    games = steamApiService.getOwnedGames(steamId64);
                    currentPage = 0;
                    displayPage(resultPanel, sortOptions);
                } catch (Exception ex) {
                    resultPanel.removeAll();
                    resultPanel.add(new JLabel("Failed to fetch games: " + ex.getMessage()));
                    resultPanel.revalidate();
                    resultPanel.repaint();
                }
            }
        });

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    displayPage(resultPanel, sortOptions);
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * PAGE_SIZE < games.size()) {
                    currentPage++;
                    displayPage(resultPanel, sortOptions);
                }
            }
        });

        sortOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPage(resultPanel, sortOptions);
            }
        });

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fetchButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(sortOptions);

        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, new JScrollPane(resultPanel));

        // Create a container for the navigation panel and status bar
        JPanel southPanel = new JPanel(new BorderLayout());

        // Create and add the status bar
        JLabel statusBar = new JLabel("Ready");
        southPanel.add(statusBar, BorderLayout.NORTH);

        // Create and add the navigation panel
        JPanel navigationPanel = createNavigationPanel(prevButton, nextButton);
        southPanel.add(navigationPanel, BorderLayout.SOUTH);

        // Add the south panel to the frame
        frame.getContentPane().add(BorderLayout.SOUTH, southPanel);

        // Add a menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
    }

    private static void displayPage(JPanel resultPanel, JComboBox<String> sortOptions) {
        resultPanel.removeAll();

        if (games == null || games.isEmpty()) {
            resultPanel.add(new JLabel("No games found or the response is null."));
            resultPanel.revalidate();
            resultPanel.repaint();
            return;
        }

        String sortOption = (String) sortOptions.getSelectedItem();
        if ("Sort by Playtime Ascending".equals(sortOption)) {
            games.sort(Comparator.comparingLong(Game::getPlaytimeForever));
        } else if ("Sort by Playtime Descending".equals(sortOption)) {
            games.sort(Comparator.comparingLong(Game::getPlaytimeForever).reversed());
        }

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, games.size());
        for (int i = start; i < end; i++) {
            Game game = games.get(i);
            resultPanel.add(new JLabel("Game: " + game.getName()));
            resultPanel.add(new JLabel("Playtime (in hours): " + game.getPlaytimeForever() / 60.0));

            String imageUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/" + game.getAppid() + "/" + game.getImgIconUrl() + ".jpg";
            try {
                URL url = new URL(imageUrl);
                Image image = ImageIO.read(url);
                ImageIcon icon = new ImageIcon(image);
                resultPanel.add(new JLabel(icon));
            } catch (IOException ex) {
                resultPanel.add(new JLabel("Failed to load image: " + ex.getMessage()));
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private static JPanel createNavigationPanel(JButton prevButton, JButton nextButton) {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new FlowLayout());
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        return navigationPanel;
    }
}