package grupo12.gui;

import grupo12.service.CursosService;
import grupo12.service.EventosService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PrincipalGui extends JFrame {

    private JMenuBar menuBar;

    public PrincipalGui() {
        setTitle("Eventos UniALFA - HACKATHON");
        setSize(850, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(montarMenuBar());
    }

    private JMenuBar montarMenuBar() {
        menuBar = new JMenuBar();
        menuBar.add(montarMenuCadastros());
        menuBar.add(montarMenuConfig());
        return menuBar;
    }

    private JMenu montarMenuCadastros() {
        var menuCadastros = new JMenu("Cadastros");
        var miEvento = new JMenuItem("Eventos");
        var miCursos = new JMenuItem("Cursos"); // Texto do item de menu atualizado
        var miPalestrante = new JMenuItem("Palestrantes (futuro)");

        menuCadastros.add(miEvento);
        menuCadastros.add(miCursos);
        menuCadastros.add(miPalestrante);

        miEvento.addActionListener(this::abrirTelaDeEventos);
        miCursos.addActionListener(this::abrirTelaDeCursos);

        return menuCadastros;
    }

    private void abrirTelaDeEventos(ActionEvent actionEvent) {
        var service = new EventosService();
        var gui = new EventosGui(service);
        gui.setVisible(true);
    }

    private void abrirTelaDeCursos(ActionEvent actionEvent) {
        var service = new CursosService();
        var gui = new CursosGui(service);
        gui.setVisible(true);
    }

    private JMenu montarMenuConfig() {
        var menuConfig = new JMenu("Configurações");
        var miSobre = new JMenuItem("Sobre");
        var miSair = new JMenuItem("Sair");

        menuConfig.add(miSobre);
        menuConfig.addSeparator();
        menuConfig.add(miSair);

        miSair.addActionListener(this::sairDaAplicacao);
        miSobre.addActionListener(this::exibirSobre);
        return menuConfig;
    }

    private void exibirSobre(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(this,
                "Sistema de Gestão de Eventos\n\nDesenvolvido por: Grupo 12\nDisciplina: Java Orientado a Objetos",
                "Sobre o Sistema",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void sairDaAplicacao(ActionEvent actionEvent) {
        int resultado = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Finalizar Aplicação",
                JOptionPane.YES_NO_OPTION);

        if (resultado == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}