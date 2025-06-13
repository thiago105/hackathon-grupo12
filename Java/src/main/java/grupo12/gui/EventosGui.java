package grupo12.gui;

import grupo12.model.Eventos;
import grupo12.service.EventosService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EventosGui extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private JTextField tfId, tfNome, tfDataInicio, tfDataFim, tfHora, tfEndereco, tfFotoUrl;
    private JButton btSalvar, btLimpar;
    private JTable tbEventos;
    private DefaultTableModel tableModel;

    private EventosService service;

    public EventosGui() {
        setTitle("Cadastro de Eventos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.service = new EventosService();

        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);

        atualizarTabela();
    }

    private JPanel montarPainelEntrada() {
        var painel = new JPanel(new GridBagLayout());
        var utils = new GuiUtils();

        tfId = new JTextField(20);
        tfId.setEditable(false);
        tfNome = new JTextField(20);
        tfDataInicio = new JTextField(20);
        tfDataFim = new JTextField(20);
        tfHora = new JTextField(20);
        tfEndereco = new JTextField(20);
        tfFotoUrl = new JTextField(20);

        painel.add(new JLabel("ID"), utils.montarConstraints(0, 0));
        painel.add(tfId, utils.montarConstraints(1, 0));
        painel.add(new JLabel("Nome"), utils.montarConstraints(0, 1));
        painel.add(tfNome, utils.montarConstraints(1, 1));
        painel.add(new JLabel("Data Início (dd/MM/aaaa)"), utils.montarConstraints(0, 2));
        painel.add(tfDataInicio, utils.montarConstraints(1, 2));
        painel.add(new JLabel("Data Fim (dd/MM/aaaa)"), utils.montarConstraints(0, 3));
        painel.add(tfDataFim, utils.montarConstraints(1, 3));
        painel.add(new JLabel("Hora (HH:MM)"), utils.montarConstraints(2, 0));
        painel.add(tfHora, utils.montarConstraints(3, 0));
        painel.add(new JLabel("Endereço"), utils.montarConstraints(2, 1));
        painel.add(tfEndereco, utils.montarConstraints(3, 1));
        painel.add(new JLabel("URL da Foto"), utils.montarConstraints(2, 2));
        painel.add(tfFotoUrl, utils.montarConstraints(3, 2));

        btSalvar = new JButton("Salvar Novo Evento");
        btSalvar.addActionListener(this::salvar);

        btLimpar = new JButton("Limpar Campos");
        btLimpar.addActionListener(e -> limparCampos());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btSalvar);
        painelBotoes.add(btLimpar);
        painel.add(painelBotoes, utils.montarConstraints(0, 4));

        return painel;
    }

    private void salvar(ActionEvent event) {
        if (!tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Para salvar um novo evento, limpe os campos primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            var evento = new Eventos();
            evento.setNome(tfNome.getText());
            evento.setEndereco(tfEndereco.getText());
            evento.setFotoUrl(tfFotoUrl.getText());
            evento.setDataInicio(LocalDate.parse(tfDataInicio.getText(), DATE_FORMATTER));
            evento.setDataFim(LocalDate.parse(tfDataFim.getText(), DATE_FORMATTER));
            evento.setHora(LocalTime.parse(tfHora.getText(), TIME_FORMATTER));

            if (service.salvar(evento)) {
                JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar evento. Verifique o console.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato. Verifique se a data está como dd/MM/aaaa e a hora como HH:MM.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        tfId.setText(null);
        tfNome.setText(null);
        tfDataInicio.setText(null);
        tfDataFim.setText(null);
        tfHora.setText(null);
        tfEndereco.setText(null);
        tfFotoUrl.setText(null);
        tbEventos.clearSelection();
    }

    private JScrollPane montarPainelSaida() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Início");
        tableModel.addColumn("Fim");
        tableModel.addColumn("Horário");
        tableModel.addColumn("Endereço");
        tbEventos = new JTable(tableModel);
        tbEventos.setDefaultEditor(Object.class, null);
        tbEventos.getSelectionModel().addListSelectionListener(this::selecionarLinha);
        return new JScrollPane(tbEventos);
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Eventos> eventos = service.listarTodos();
        if (eventos != null) {
            for (Eventos e : eventos) {
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getNome(),
                        e.getDataInicio().format(DATE_FORMATTER),
                        e.getDataFim().format(DATE_FORMATTER),
                        e.getHora().format(TIME_FORMATTER),
                        e.getEndereco()
                });
            }
        }
    }

    private void selecionarLinha(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int linhaSelecionada = tbEventos.getSelectedRow();
            if (linhaSelecionada != -1) {
                tfId.setText(tbEventos.getValueAt(linhaSelecionada, 0).toString());
                tfNome.setText(tbEventos.getValueAt(linhaSelecionada, 1).toString());
                tfDataInicio.setText(tbEventos.getValueAt(linhaSelecionada, 2).toString());
                tfDataFim.setText(tbEventos.getValueAt(linhaSelecionada, 3).toString());
                tfHora.setText(tbEventos.getValueAt(linhaSelecionada, 4).toString());
                tfEndereco.setText(tbEventos.getValueAt(linhaSelecionada, 5).toString());

                tfFotoUrl.setText("");
            }
        }
    }
}