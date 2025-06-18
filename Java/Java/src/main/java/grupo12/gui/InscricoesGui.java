package grupo12.gui;

import grupo12.model.Inscricoes;
import grupo12.service.InscricoesService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InscricoesGui extends JFrame {

    private JTable tbInscricoes;
    private JButton btMarcarPresenca, btMarcarFalta, btAtualizar;
    private final InscricoesService service;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public InscricoesGui(InscricoesService inscricoesService) {
        this.service = inscricoesService;

        setTitle("Controle de Presença em Eventos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(montarPainelTabela(), BorderLayout.CENTER);
        getContentPane().add(montarPainelBotoes(), BorderLayout.SOUTH);
        atualizarTabela();
    }

    private JScrollPane montarPainelTabela() {
        var tableModel = new DefaultTableModel(new Object[]{"ID", "Aluno", "Evento", "Data Inscrição", "Presença"}, 0);
        tbInscricoes = new JTable(tableModel);
        tbInscricoes.setDefaultEditor(Object.class, null);
        tbInscricoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tbInscricoes.getColumnCount(); i++) {
            tbInscricoes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tbInscricoes.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        return new JScrollPane(tbInscricoes);
    }

    private JPanel montarPainelBotoes() {
        var painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btMarcarPresenca = new JButton("Marcar Presença");
        btMarcarFalta = new JButton("Marcar Falta");
        btAtualizar = new JButton("Atualizar Lista");
        btMarcarPresenca.addActionListener(this::marcarPresenca);
        btMarcarFalta.addActionListener(this::marcarFalta);
        btAtualizar.addActionListener(e -> atualizarTabela());

        painel.add(btMarcarPresenca);
        painel.add(btMarcarFalta);
        painel.add(btAtualizar);
        return painel;
    }

    private void atualizarTabela() {
        var tableModel = (DefaultTableModel) tbInscricoes.getModel();
        tableModel.setRowCount(0);
        List<Inscricoes> lista = service.listarTodas();
        for (Inscricoes inscricao : lista) {
            Integer statusNum = inscricao.getAprovado();
            String status;
            if (statusNum != null && statusNum == 1) {
                status = "Presente";
            } else {
                status = "Ausente";
            }
            tableModel.addRow(new Object[]{
                    inscricao.getId(),
                    inscricao.getUsuario().getNome(),
                    inscricao.getEvento().getNome(),
                    inscricao.getDataInscricao().format(FORMATTER),
                    status
            });
        }
    }

    private Long getIdSelecionado() {
        int selectedRow = tbInscricoes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um participante na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Long) tbInscricoes.getValueAt(selectedRow, 0);
    }

    private void marcarPresenca(ActionEvent e) {
        Long id = getIdSelecionado();
        if (id != null) {
            boolean sucesso = service.aprovarInscricao(id);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Presença marcada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao marcar presença.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void marcarFalta(ActionEvent e) {
        Long id = getIdSelecionado();
        if (id != null) {
            int resp = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja marcar falta para este participante?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                boolean sucesso = service.reprovarInscricao(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Falta marcada com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao marcar falta.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}