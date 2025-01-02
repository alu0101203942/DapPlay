package src.Vista;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.util.List;

public class PieChartStrategy implements ChartStrategy {
    @Override
    public JPanel createChart(List<Game> games) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Game game : games) {
            dataset.setValue(game.getName(), game.getPlaytimeForever() / 60.0);
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribución de Horas Jugadas",
                dataset,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }
}
