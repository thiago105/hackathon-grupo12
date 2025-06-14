package grupo12.gui;

import grupo12.model.Cursos;
import grupo12.service.CursosService;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CursosGui extends JFrame {

    private JTextField tfId, tfNome;
    private JButton btSalvarNovo, btEditar, btExcluir, btLimpar;
    private JTable tbCursos;
    private final CursosService service;
    private Cursos cursoSelecionadoParaEdicao;

    public CursosGui(CursosService cursoService) {
        this.service = cursoService;
        setTitle("Cadastro de Cursos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);
    }

    private JPanel montarPainelEntrada() {
        var painel = new JPanel(new GridBagLayout());
        var utils = new GuiUtils();

        tfId = new JTextField(20);
        tfId.setEditable(false);
        tfNome = new JTextField(20);

        painel.add(new JLabel("ID"), utils.montarConstraints(0, 0));
        painel.add(tfId, utils.montarConstraintsParaCampo(1, 0));
        painel.add(new JLabel("Nome"), utils.montarConstraints(0, 1));
        painel.add(tfNome, utils.montarConstraintsParaCampo(1, 1));

        btSalvarNovo = new JButton("Salvar Novo");
        btSalvarNovo.addActionListener(this::salvar);

        btEditar = new JButton("Editar");
        btEditar.addActionListener(this::editar);

        btExcluir = new JButton("Excluir");
        btExcluir.addActionListener(this::excluir);

        btLimpar = new JButton("Limpar Campos");
        btLimpar.addActionListener(e -> limparCampos());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btSalvarNovo);
        painelBotoes.add(btEditar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btLimpar);
        painel.add(painelBotoes, utils.montarConstraints(0, 2, 2));

        return painel;
    }

    private JScrollPane montarPainelSaida() {
        tbCursos = new JTable();
        tbCursos.setDefaultEditor(Object.class, null);
        tbCursos.getSelectionModel().addListSelectionListener(this::selecionarLinha);
        tbCursos.setModel(montarTableModel());
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tbCursos.getColumnCount(); i++) {
            tbCursos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        return new JScrollPane(tbCursos);
    }

    private DefaultTableModel montarTableModel() {
        var tableModel = new DefaultTableModel(new Object[]{"ID", "Nome"}, 0);
        service.listarTodos().forEach(c ->
                tableModel.addRow(new Object[]{c.getId(), c.getNome()})
        );
        return tableModel;
    }

    private void atualizarTabela() {
        tbCursos.setModel(montarTableModel());
    }

    private void limparCampos() {
        tfId.setText(null);
        tfNome.setText(null);
        tbCursos.clearSelection();
        this.cursoSelecionadoParaEdicao = null;
    }

    private Cursos getCursoDoFormulario() {
        if (tfNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do curso é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        var curso = new Cursos();
        curso.setId(tfId.getText().isEmpty() ? null : Long.valueOf(tfId.getText()));
        curso.setNome(tfNome.getText());
        return curso;
    }

    private void salvar(ActionEvent event) {
        if (!tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Para salvar um novo curso, limpe os campos primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cursos curso = getCursoDoFormulario();
        if (curso != null && service.salvar(curso)) {
            JOptionPane.showMessageDialog(this, "Curso salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            atualizarTabela();
        } else if (curso != null) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o curso.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um curso na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cursos cursoDoFormulario = getCursoDoFormulario();
        if (cursoDoFormulario != null) {
            if (service.atualizar(cursoDoFormulario)) {
                JOptionPane.showMessageDialog(this, "Curso editado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao editar o curso.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluir(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            service.excluir(Long.valueOf(tfId.getText()));
            limparCampos();
            atualizarTabela();
        }
    }

    private void selecionarLinha(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int linhaSelecionada = tbCursos.getSelectedRow();
            if (linhaSelecionada != -1) {
                Long id = (Long) tbCursos.getValueAt(linhaSelecionada, 0);
                this.cursoSelecionadoParaEdicao = service.buscarPorId(id);
                if (this.cursoSelecionadoParaEdicao != null) {
                    tfId.setText(cursoSelecionadoParaEdicao.getId().toString());
                    tfNome.setText(cursoSelecionadoParaEdicao.getNome());
                }
            }
        }
    }
}