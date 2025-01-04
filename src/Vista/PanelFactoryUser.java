package src.Vista;

import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.swing.*;

public interface PanelFactoryUser extends PanelFactory {
    JPanel createPanel(Player player, int games);
}
