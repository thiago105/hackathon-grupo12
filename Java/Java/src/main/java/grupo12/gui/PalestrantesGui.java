package grupo12.gui;

import grupo12.model.Palestrantes;
import grupo12.service.PalestrantesService;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PalestrantesGui extends JFrame {

    private JTextField tfId, tfNome, tfFotoUrl;
    private JTextField tfMiniCurriculo;
    private JButton btSalvarNovo, btAlterar, btExcluir, btLimpar, btSelecionarFoto;
    private JTable tbPalestrantes;
    private final PalestrantesService service;

    public PalestrantesGui(PalestrantesService palestranteService) {
        this.service = palestranteService;
        setTitle("Cadastro de Palestrantes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        tfMiniCurriculo = new JTextField(20);
        tfFotoUrl = new JTextField(20);
        tfFotoUrl.setEditable(false);
        btSelecionarFoto = new JButton("Selecionar...");

        painel.add(new JLabel("ID"), utils.montarConstraints(0, 0));
        painel.add(tfId, utils.montarConstraintsParaCampo(1, 0));
        painel.add(new JLabel("Nome"), utils.montarConstraints(0, 1));
        painel.add(tfNome, utils.montarConstraintsParaCampo(1, 1));
        painel.add(new JLabel("Mini Currículo"), utils.montarConstraints(2, 0));
        painel.add(new JScrollPane(tfMiniCurriculo), utils.montarConstraintsParaCampo(3, 0));
        painel.add(new JLabel("URL da Foto"), utils.montarConstraints(2, 1));
        JPanel painelFoto = new JPanel(new BorderLayout(5,0));
        painelFoto.add(tfFotoUrl, BorderLayout.CENTER);
        painelFoto.add(btSelecionarFoto, BorderLayout.EAST);
        painel.add(painelFoto, utils.montarConstraintsParaCampo(3, 1));

        btSalvarNovo = new JButton("Salvar Novo");
        btAlterar = new JButton("Alterar Selecionado");
        btExcluir = new JButton("Excluir Selecionado");
        btLimpar = new JButton("Limpar Campos");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btSalvarNovo);
        painelBotoes.add(btAlterar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btLimpar);
        painel.add(painelBotoes, utils.montarConstraints(0, 2, 4));

        btSalvarNovo.addActionListener(this::salvar);
        btAlterar.addActionListener(this::alterar);
        btExcluir.addActionListener(this::excluir);
        btLimpar.addActionListener(e -> limparCampos());
        btSelecionarFoto.addActionListener(this::selecionarFoto);

        return painel;
    }

    private JScrollPane montarPainelSaida() {
        tbPalestrantes = new JTable(new DefaultTableModel(new Object[]{"ID", "Nome", "Mini Currículo"}, 0));
        tbPalestrantes.setDefaultEditor(Object.class, null);
        tbPalestrantes.getSelectionModel().addListSelectionListener(this::selecionarLinha);
        var centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tbPalestrantes.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tbPalestrantes.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        return new JScrollPane(tbPalestrantes);
    }

    private void atualizarTabela() {
        var tableModel = (DefaultTableModel) tbPalestrantes.getModel();
        tableModel.setRowCount(0);
        service.listarTodos().forEach(p -> tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getMiniCurriculo()}));
    }

    private void limparCampos() {
        tfId.setText("");
        tfNome.setText("");
        tfMiniCurriculo.setText("");
        tfFotoUrl.setText("");
        tbPalestrantes.clearSelection();
    }

    private Palestrantes getPalestranteDoFormulario() {
        var p = new Palestrantes();
        p.setId(tfId.getText().isEmpty() ? null : Long.valueOf(tfId.getText()));
        p.setNome(tfNome.getText());
        p.setMiniCurriculo(tfMiniCurriculo.getText());
        p.setFotoUrl(tfFotoUrl.getText());
        return p;
    }

    private void salvar(ActionEvent e) {
        if (!tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Limpe os campos para salvar um novo palestrante.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        var palestrante = getPalestranteDoFormulario();
        if(palestrante.getNome().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (service.salvar(palestrante)) {
            JOptionPane.showMessageDialog(this, "Salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar(ActionEvent e) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante para alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        var palestrante = getPalestranteDoFormulario();
        if(palestrante.getNome().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (service.atualizar(palestrante)) {
            JOptionPane.showMessageDialog(this, "Alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir(ActionEvent e) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            service.excluir(Long.valueOf(tfId.getText()));
            limparCampos();
            atualizarTabela();
        }
    }

    private void selecionarLinha(ListSelectionEvent e) {
        int row = tbPalestrantes.getSelectedRow();
        if (row != -1) {
            Long id = (Long) tbPalestrantes.getValueAt(row, 0);
            var p = service.buscarPorId(id);
            if (p != null) {
                tfId.setText(p.getId().toString());
                tfNome.setText(p.getNome());
                tfMiniCurriculo.setText(p.getMiniCurriculo());
                tfFotoUrl.setText(p.getFotoUrl());
            }
        }
    }

    private void selecionarFoto(ActionEvent event) {
        JFileChooser seletorDeArquivo = new JFileChooser();
        seletorDeArquivo.setDialogTitle("Selecione uma imagem para o evento");
        seletorDeArquivo.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Arquivos de Imagem", "jpg", "jpeg", "png", "gif"));
        int resultado = seletorDeArquivo.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            java.io.File arquivoSelecionado = seletorDeArquivo.getSelectedFile();
            tfFotoUrl.setText(arquivoSelecionado.getAbsolutePath());
        }
    }
}