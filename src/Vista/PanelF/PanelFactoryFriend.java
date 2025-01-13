package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.swing.*;

public interface PanelFactoryFriend extends PanelFactory {
    JPanel createPanel(Player player);
}
