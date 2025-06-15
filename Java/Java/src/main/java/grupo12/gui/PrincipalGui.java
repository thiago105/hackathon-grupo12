package grupo12.gui;

import grupo12.service.CursosService;
import grupo12.service.EventosService;
import grupo12.service.PalestrantesService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PrincipalGui extends JFrame {

    private JMenuBar menuBar;

    public PrincipalGui() {
        setTitle("Eventos UniALFA - HACKATHON");
        setSize(700, 400);
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
        var miCursos = new JMenuItem("Cursos");
        var miPalestrantes = new JMenuItem("Palestrantes");

        menuCadastros.add(miEvento);
        menuCadastros.add(miCursos);
        menuCadastros.add(miPalestrantes);

        miEvento.addActionListener(this::abrirTelaDeEventos);
        miCursos.addActionListener(this::abrirTelaDeCursos);
        miPalestrantes.addActionListener(this::abrirTelaDePalestrantes);
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

    private void abrirTelaDePalestrantes(ActionEvent actionEvent) {
        var service = new PalestrantesService();
        var gui = new PalestrantesGui(service);
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
        miSobre.addActionListener(this::abrirTelaSobre);
        return menuConfig;
    }

    private void abrirTelaSobre(ActionEvent actionEvent) {
        JOptionPane.showMessageDialog(this,
                "Sistema de Gestão de Eventos UniALFA\n\n" +
                        "Desenvolvido por:\n\n" +
                        "Eduardo Henrique Pereira Dos Santos\n" +
                        "Thiago Vinicius Santos Da Silva\n" +
                        "Gabriel Henrique Friedrichsen\n" +
                        "Hendreu Satoshi Zampieri Itami\n" +
                        "André Mateus Roll\n\n" +
                        "Disciplina: Java Orientado a Objetos",
                "Sobre o Sistema",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void sairDaAplicacao(ActionEvent actionEvent) {
        int resultado = JOptionPane.showConfirmDialog(
                this,
                "Deseja sair do sistema?",
                "Finalizar Aplicação",
                JOptionPane.YES_NO_OPTION);

        if (resultado == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}