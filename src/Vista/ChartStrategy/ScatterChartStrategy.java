package src.Vista.ChartStrategy;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultXYDataset;

import javax.swing.*;
import java.util.List;

public class ScatterChartStrategy implements ChartStrategy {
    @Override
    public JPanel createChart(List<Game> games) {
        DefaultXYDataset dataset = new DefaultXYDataset();

        double[][] data = new double[2][games.size()];
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            data[0][i] = i;
            data[1][i] = game.getPlaytimeForever() / 60.0;
        }
        dataset.addSeries("Horas Jugadas", data);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Gráfico de Dispersión de Horas Jugadas",
                "Juego (Índice)",
                "Horas Jugadas",
                dataset
        );
        return new ChartPanel(chart);
    }
}
