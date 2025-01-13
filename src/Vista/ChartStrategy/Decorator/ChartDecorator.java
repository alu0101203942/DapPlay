package src.Vista.ChartStrategy.Decorator;

import src.Vista.ChartStrategy.ChartStrategy;
import javax.swing.*;
import java.util.List;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

public abstract class ChartDecorator implements ChartStrategy {
    protected ChartStrategy decoratedChart;

    public ChartDecorator(ChartStrategy decoratedChart) {
        this.decoratedChart = decoratedChart;
    }

    @Override
    public JPanel createChart(List<Game> games) {
        return decoratedChart.createChart(games);
    }
}
