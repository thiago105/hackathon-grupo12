package grupo12;

import grupo12.gui.EventosGui;
import grupo12.service.EventosService; // Importa o serviço
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::iniciar);
    }

    private static void iniciar() {
        var eventosService = new EventosService();
        var eventoGui = new EventosGui(eventosService);
        eventoGui.setVisible(true);
    }
}