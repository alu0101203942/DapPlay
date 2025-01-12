package src.Vista.ChartStrategy.Decorator;

import src.Vista.ChartStrategy.ChartStrategy;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;
import src.Vista.ChartStrategy.Decorator.ChartDecorator;

public class TitleDecorator extends ChartDecorator {
    private String title;

    public TitleDecorator(ChartStrategy decoratedChart, String title) {
        super(decoratedChart);
        this.title = title;
    }

    @Override
    public JPanel createChart(List<Game> games) {
        JPanel panel = new JPanel(new BorderLayout());

        // Crear el título como un JLabel
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Agregar el gráfico decorado al centro del panel
        JPanel chartPanel = decoratedChart.createChart(games);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

}
