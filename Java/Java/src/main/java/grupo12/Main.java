package grupo12;

import grupo12.gui.PrincipalGui;

import javax.swing.*;

public class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        var gui = new PrincipalGui();
        gui.setVisible(true);
    }
}