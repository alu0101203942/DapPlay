package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface PanelFactoryFriend extends PanelFactory {
    JPanel createPanel(Player player, ActionListener actionListener);
}
