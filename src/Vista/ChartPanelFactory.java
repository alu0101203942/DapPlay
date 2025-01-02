package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ChartPanelFactory implements PanelFactory {

    public JPanel createChart(String chartType, List<Game> games) {
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