package src.Vista.ChartStrategy.Decorator;

import src.Vista.ChartStrategy.ChartStrategy;
import javax.swing.*;
import java.util.List;
import com.lukaspradel.steamapi.data.json.ownedgames.Game;

public class ColorDecorator extends ChartDecorator {
    private String color;

    public ColorDecorator(ChartStrategy decoratedChart, String color) {
        super(decoratedChart);
        this.color = color;
    }

    @Override
    public JPanel createChart(List<Game> games) {
        JPanel panel = super.createChart(games);
        panel.setBackground(parseColor(color));
        return panel;
    }

    private java.awt.Color parseColor(String color) {
        switch (color.toLowerCase()) {
            case "azul": return java.awt.Color.BLUE;
            case "rojo": return java.awt.Color.RED;
            case "verde": return java.awt.Color.GREEN;
            default: return java.awt.Color.WHITE;
        }
    }
}
