package src.Vista;


import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class BarChartStrategy implements ChartStrategy {
    @Override
    public JPanel createChart(List<Game> games) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Game game : games) {
            dataset.addValue(game.getPlaytimeForever() / 60.0, "Horas Jugadas", game.getName());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Horas Jugadas por Juego",
                "Juegos",
                "Horas",
                dataset
        );

        return new ChartPanel(chart);
    }
}