package grupo12;

import grupo12.gui.PrincipalGui;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        var gui = new PrincipalGui();
        gui.setVisible(true);
    }
}