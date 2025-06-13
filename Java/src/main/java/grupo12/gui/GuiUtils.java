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
}