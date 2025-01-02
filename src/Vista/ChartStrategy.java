package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;

import javax.swing.*;
import java.util.List;

public interface ChartStrategy {
    JPanel createChart(List<Game> games);
}
