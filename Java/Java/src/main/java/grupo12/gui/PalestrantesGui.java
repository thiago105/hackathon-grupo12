package grupo12.gui;

import grupo12.model.Eventos;
import grupo12.model.Palestrantes;
import grupo12.service.PalestrantesService;
import grupo12.util.FileUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class PalestrantesGui extends JFrame {

    private JTextField tfId, tfNome, tfFotoUrl;
    private JTextField tfMiniCurriculo;
    private JButton btSalvarNovo, btEditar, btExcluir, btLimpar, btSelecionarFoto;
    private JTable tbPalestrantes;
    private final PalestrantesService service;
    private Palestrantes palestranteSelecionadoParaEdicao;

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
        btEditar = new JButton("Editar");
        btExcluir = new JButton("Excluir");
        btLimpar = new JButton("Limpar Campos");

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btSalvarNovo);
        painelBotoes.add(btEditar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btLimpar);
        painel.add(painelBotoes, utils.montarConstraints(0, 2, 4));

        btSalvarNovo.addActionListener(this::salvar);
        btEditar.addActionListener(this::editar);
        btExcluir.addActionListener(this::excluir);
        btLimpar.addActionListener(e -> limparCampos());
        btSelecionarFoto.addActionListener(this::selecionarFoto);

        return painel;
    }

    private JScrollPane montarPainelSaida() {
        var tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Mini Currículo");

        tbPalestrantes = new JTable(tableModel);
        tbPalestrantes.setDefaultEditor(Object.class, null);
        tbPalestrantes.getTableHeader().setReorderingAllowed(false); // Impede que o usuário arraste as colunas
        tbPalestrantes.getSelectionModel().addListSelectionListener(this::selecionarLinha);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tbPalestrantes.getColumnCount(); i++) {
            tbPalestrantes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tbPalestrantes.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        return new JScrollPane(tbPalestrantes);
    }

    private void atualizarTabela() {
        var tableModel = (DefaultTableModel) tbPalestrantes.getModel();
        tableModel.setRowCount(0);
        service.listarTodos().forEach(p -> tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getMiniCurriculo()}));
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

    private void limparCampos() {
        tfId.setText("");
        tfNome.setText("");
        tfMiniCurriculo.setText("");
        tfFotoUrl.setText("");
        tbPalestrantes.clearSelection();
        this.palestranteSelecionadoParaEdicao = null;
    }

    private Palestrantes getPalestranteDoFormulario() {
        if(tfNome.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        var p = new Palestrantes();
        p.setId(tfId.getText().isEmpty() ? null : Long.valueOf(tfId.getText()));
        p.setNome(tfNome.getText());
        p.setMiniCurriculo(tfMiniCurriculo.getText());
        p.setFotoUrl(tfFotoUrl.getText());
        return p;
    }

    private void finalizarAcao(boolean sucesso, String operacao) {
        String mensagem = sucesso ? "Palestrante " + operacao + " com sucesso!" : "Erro ao " + operacao.toLowerCase() + " o palestrante.";
        String titulo = sucesso ? "Sucesso" : "Erro";
        int tipoMsg = sucesso ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMsg);
        if (sucesso) {
            limparCampos();
            atualizarTabela();
        }
    }

    private void salvar(ActionEvent event) {
        if (!tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Limpe os campos para salvar um novo palestrante.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Palestrantes palestrante = getPalestranteDoFormulario();
        if (palestrante != null) {
            try {
                String caminhoImagemOriginal = tfFotoUrl.getText();
                if (caminhoImagemOriginal != null && !caminhoImagemOriginal.isEmpty()) {
                    File arquivoImagemOriginal = new File(caminhoImagemOriginal);
                    String novoCaminho = FileUtils.copiarImagem(arquivoImagemOriginal, "uploads");
                    palestrante.setFotoUrl(novoCaminho);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao copiar a imagem.", "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            finalizarAcao(service.salvar(palestrante), "salvo");
        }
    }

    private void editar(ActionEvent e) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Palestrantes palestranteDoFormulario = getPalestranteDoFormulario();
        if (palestranteDoFormulario != null) {
            try {
                String caminhoFotoOriginal = tfFotoUrl.getText();
                if (caminhoFotoOriginal != null && !caminhoFotoOriginal.equals(palestranteSelecionadoParaEdicao.getFotoUrl())) {
                    File arquivoImagemOriginal = new File(caminhoFotoOriginal);
                    String novoCaminho = FileUtils.copiarImagem(arquivoImagemOriginal, "uploads");
                    palestranteDoFormulario.setFotoUrl(novoCaminho);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao copiar a nova imagem.", "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;
            }
            finalizarAcao(service.atualizar(palestranteDoFormulario), "alterado");
        }
    }

    private void excluir(ActionEvent e) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um palestrante para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            finalizarAcao(service.excluir(Long.valueOf(tfId.getText())), "excluído");
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
}