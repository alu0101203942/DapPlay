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
        // Crear el dataset para el gráfico
        DefaultXYDataset dataset = new DefaultXYDataset();

        // Crear arrays para los datos (ejemplo simplificado: Playtime vs. Índice)
        double[][] data = new double[2][games.size()];
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            data[0][i] = i; // Índice del juego (X)
            data[1][i] = game.getPlaytimeForever() / 60.0; // Horas jugadas (Y)
        }
        dataset.addSeries("Horas Jugadas", data);

        // Crear el gráfico de dispersión
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Gráfico de Dispersión de Horas Jugadas", // Título
                "Juego (Índice)",                        // Eje X
                "Horas Jugadas",                         // Eje Y
                dataset                                  // Dataset
        );

        // Retornar un ChartPanel que contiene el gráfico
        return new ChartPanel(chart);
    }
}
