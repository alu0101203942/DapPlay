package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PanelFactory {
    public static JPanel createGamePanel(Game game, ActionListener favoriteAction) {
        JPanel gamePanel = new JPanel(new BorderLayout());
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
        favoriteButton.addActionListener(favoriteAction);
        textPanel.add(favoriteButton);

        gamePanel.add(textPanel, BorderLayout.CENTER);

        return gamePanel;
    }

    public static JPanel createFavoritePanel(Game game, ActionListener removeAction) {
        JPanel favoritepanel = new JPanel(new BorderLayout());
        favoritepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel label = new JLabel(game.getName());
        favoritepanel.add(label, BorderLayout.CENTER);

        JButton removeButton = new JButton("Eliminar");
        removeButton.addActionListener(removeAction);
        favoritepanel.add(removeButton, BorderLayout.EAST);

        return favoritepanel;
    }

    public static JPanel createChart(String chartType, List<Game> games) {
        ChartStrategy chartStrategy;
        switch (chartType) {
            case "Gráfico de Barras":
                chartStrategy = new BarChartStrategy();
                break;
            case "Gráfico de Sectores":
                chartStrategy = new PieChartStrategy();
                break;
            default:
                throw new IllegalArgumentException("Tipo de gráfico no soportado: " + chartType);
        }
        return chartStrategy.createChart(games);
    }


}
