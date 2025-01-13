package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Vista.ChartStrategy.*;
import src.Vista.ChartStrategy.Decorator.ColorDecorator;
import src.Vista.ChartStrategy.Decorator.TitleDecorator;

import javax.swing.*;
import java.awt.*;
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
            case "Gráfico de Líneas":
                chartStrategy = new LineChartStrategy();
                break;
            case "Gráfico de Dispersión":
                chartStrategy = new ScatterChartStrategy();
                break;
            default:
                throw new IllegalArgumentException("Tipo de gráfico no soportado: " + chartType);
        }
        chartStrategy = new TitleDecorator(chartStrategy, "Gráfico: " + chartType);
        chartStrategy = new ColorDecorator(chartStrategy, "#4285F4");
        JPanel chartPanel = chartStrategy.createChart(games);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        return chartPanel;
    }
}