// src/Vista/PanelFactoryGame.java
package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public interface PanelFactoryGame extends PanelFactory {
    JPanel createPanel(Game game, ActionListener removeAction);
}