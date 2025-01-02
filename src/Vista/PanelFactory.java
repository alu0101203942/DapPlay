package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class PanelFactory {
    public static JPanel createGamePanel(Game game, ActionListener favoriteAction) {
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        return gamePanel;
    }
    public static JPanel createFavoritePanel(Game game, ActionListener removeAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        return panel;
    }

}
