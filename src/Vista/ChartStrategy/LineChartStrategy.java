package src.Vista.ChartStrategy;

import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class LineChartStrategy implements ChartStrategy {
    @Override
    public JPanel createChart(List<Game> games) {
        // Crear el dataset para el gráfico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Game game : games) {
            dataset.addValue(game.getPlaytimeForever() / 60.0, "Horas Jugadas", game.getName());
        }

        // Crear el gráfico de líneas
        JFreeChart chart = ChartFactory.createLineChart(
                "Horas Jugadas por Juego",   // Título del gráfico
                "Juegos",                   // Etiqueta del eje X
                "Horas",                    // Etiqueta del eje Y
                dataset                     // Dataset
        );

        // Retornar un ChartPanel que contiene el gráfico
        return new ChartPanel(chart);
    }
}
