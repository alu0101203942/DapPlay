// src/Vista/FavoritePanelFactory.java
package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class FavoritePanelFactory implements PanelFactoryGame {
    @Override
    public JPanel createPanel(Game game, ActionListener removeAction) {
        JPanel favoritepanel = new JPanel(new BorderLayout());
        favoritepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel label = new JLabel(game.getName());
        favoritepanel.add(label, BorderLayout.CENTER);

        JButton removeButton = new JButton("Eliminar");
        removeButton.addActionListener(removeAction);
        favoritepanel.add(removeButton, BorderLayout.EAST);

        return favoritepanel;
    }

}