package grupo12.gui;

import java.awt.*;

public class GuiUtils {

    public GridBagConstraints montarConstraints(int gridx, int gridy) {
        var constraints = new GridBagConstraints();
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }

    public GridBagConstraints montarConstraintsParaCampo(int gridx, int gridy) {
        var constraints = montarConstraints(gridx, gridy);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        return constraints;
    }

    public GridBagConstraints montarConstraints(int gridx, int gridy, int gridwidth) {
        var constraints = montarConstraints(gridx, gridy);
        constraints.gridwidth = gridwidth;
        return constraints;
    }
}