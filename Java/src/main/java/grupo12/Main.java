package grupo12;

import grupo12.gui.EventosGui;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        var eventoGui = new EventosGui();
        eventoGui.setVisible(true);
    }
}