package src.Vista.PanelF;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Vista.ChartStrategy.*;
import src.Vista.ChartStrategy.Decorator.ColorDecorator;
import src.Vista.ChartStrategy.Decorator.TitleDecorator;

import javax.swing.*;
import java.util.List;
public class ChartPanelFactory implements PanelFactory {

    public JPanel createChart(String chartType, List<Game> games) {
        ChartStrategy chartStrategy;

        // Crear el gráfico según el tipo
        switch (chartType) {
            case "Gráfico de Barras":
                chartStrategy = new BarChartStrategy();
                break;
            case "Gráfico de Sectores":
                chartStrategy = new PieChartStrategy();
                break;
            case "Gráfico de Líneas":
                chartStrategy = new LineChartStrategy(); // Nuevo tipo de gráfico
                break;
            case "Gráfico de Dispersión":
                chartStrategy = new ScatterChartStrategy(); // Nuevo tipo de gráfico
                break;
            default:
                throw new IllegalArgumentException("Tipo de gráfico no soportado: " + chartType);
        }

        // Aplicar decoradores (ejemplo: añadir título y color)
        chartStrategy = new TitleDecorator(chartStrategy, "Gráfico: " + chartType);
        chartStrategy = new ColorDecorator(chartStrategy, "Azul");

        // Crear el panel del gráfico
        return chartStrategy.createChart(games);
    }
}