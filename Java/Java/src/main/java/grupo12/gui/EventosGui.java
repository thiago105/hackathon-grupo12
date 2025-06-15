package grupo12.gui;

import grupo12.model.Cursos;
import grupo12.model.Eventos;
import grupo12.model.Palestrantes;
import grupo12.service.CursosService;
import grupo12.service.EventosService;
import grupo12.service.PalestrantesService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
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

    private JTextField tfId, tfNome, tfEndereco, tfFotoUrl;
    private JFormattedTextField tfDataInicio, tfDataFim, tfHora;
    private JComboBox<Cursos> cbCursos;
    private JComboBox<Palestrantes> cbPalestrantes;
    private JButton btSalvarNovo, btEditar, btExcluir, btLimpar, btSelecionarFoto;
    private JTable tbEventos;
    private final EventosService service;
    private Eventos eventoSelecionadoParaEdicao;

    public EventosGui(EventosService eventosService) {
        this.service = eventosService;
        setTitle("Cadastro de Eventos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);
        carregarCursosNoCombobox();
        carregarPalestrantesNoCombobox();
        atualizarTabela();
    }

    private JPanel montarPainelEntrada() {
        var painel = new JPanel(new GridBagLayout());
        var utils = new GuiUtils();
        tfId = new JTextField(20);
        tfId.setEditable(false);
        tfNome = new JTextField(20);
        tfDataInicio = new JFormattedTextField(createFormatter("##/##/####"));
        tfDataFim = new JFormattedTextField(createFormatter("##/##/####"));
        tfHora = new JFormattedTextField(createFormatter("##:##"));
        tfEndereco = new JTextField(20);
        tfFotoUrl = new JTextField(20);
        tfFotoUrl.setEditable(false);
        btSelecionarFoto = new JButton("Selecionar...");
        btSelecionarFoto.addActionListener(this::selecionarFoto);

        cbCursos = new JComboBox<>();
        cbCursos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cursos) setText(((Cursos) value).getNome());
                else setText("Nenhum curso selecionado");
                return this;
            }
        });

        cbPalestrantes = new JComboBox<>();
        cbPalestrantes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Palestrantes) setText(((Palestrantes) value).getNome());
                else setText("Nenhum palestrante selecionado");
                return this;
            }
        });

        painel.add(new JLabel("ID"), utils.montarConstraints(0, 0));
        painel.add(tfId, utils.montarConstraintsParaCampo(1, 0));
        painel.add(new JLabel("Nome"), utils.montarConstraints(0, 1));
        painel.add(tfNome, utils.montarConstraintsParaCampo(1, 1));
        painel.add(new JLabel("Data Início (dd/MM/aaaa)"), utils.montarConstraints(0, 2));
        painel.add(tfDataInicio, utils.montarConstraintsParaCampo(1, 2));
        painel.add(new JLabel("Hora (HH:MM)"), utils.montarConstraints(2, 2));
        painel.add(tfDataFim, utils.montarConstraintsParaCampo(1, 3));
        painel.add(new JLabel("Curso"), utils.montarConstraints(2, 0));
        painel.add(cbCursos, utils.montarConstraintsParaCampo(3, 0));
        painel.add(new JLabel("Palestrante"), utils.montarConstraints(2, 1));
        painel.add(cbPalestrantes, utils.montarConstraintsParaCampo(3, 1));
        painel.add(new JLabel("Hora (HH:MM)"), utils.montarConstraints(2, 2));
        painel.add(tfHora, utils.montarConstraintsParaCampo(3, 2));
        painel.add(new JLabel("Endereço"), utils.montarConstraints(2, 3));
        painel.add(tfEndereco, utils.montarConstraintsParaCampo(3, 3));
        painel.add(new JLabel("URL da Foto"), utils.montarConstraints(2, 4));
        JPanel painelFoto = new JPanel(new BorderLayout(5, 0));
        painelFoto.add(tfFotoUrl, BorderLayout.CENTER);
        painelFoto.add(btSelecionarFoto, BorderLayout.EAST);
        painel.add(painelFoto, utils.montarConstraintsParaCampo(3, 4));

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
        painel.add(painelBotoes, utils.montarConstraints(0, 4, 4));
        return painel;
    }

    private MaskFormatter createFormatter(String mask) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(mask);
            formatter.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            System.err.println("Erro na criação da máscara de formatação: " + e.getMessage());
        }
        return formatter;
    }

    private JScrollPane montarPainelSaida() {
        var tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Curso");
        tableModel.addColumn("Palestrante");
        tableModel.addColumn("Horário");
        tableModel.addColumn("Início");
        tableModel.addColumn("Fim");

        tbEventos = new JTable(tableModel);
        tbEventos.setDefaultEditor(Object.class, null);
        tbEventos.getTableHeader().setReorderingAllowed(false);
        tbEventos.getSelectionModel().addListSelectionListener(this::selecionarLinha);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tbEventos.getColumnCount(); i++) {
            tbEventos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        return new JScrollPane(tbEventos);
    }

    private void carregarCursosNoCombobox() {
        var cursosService = new CursosService();
        List<Cursos> cursos = cursosService.listarTodos();
        cbCursos.removeAllItems();
        cbCursos.addItem(null);
        for (Cursos curso : cursos) {
            cbCursos.addItem(curso);
        }
    }

    private void carregarPalestrantesNoCombobox() {
        var palestrantesService = new PalestrantesService();
        List<Palestrantes> palestrantes = palestrantesService.listarTodos();
        cbPalestrantes.removeAllItems();
        cbPalestrantes.addItem(null);
        for (Palestrantes palestrante : palestrantes) {
            cbPalestrantes.addItem(palestrante);
        }
    }

    private void atualizarTabela() {
        DefaultTableModel tableModel = (DefaultTableModel) tbEventos.getModel();
        tableModel.setRowCount(0);
        service.listarTodos().forEach(e ->
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getNome(),
                        (e.getCurso() != null) ? e.getCurso().getNome() : "N/A",
                        (e.getPalestrante() != null) ? e.getPalestrante().getNome() : "N/A",
                        e.getHora().format(TIME_FORMATTER),
                        e.getDataInicio().format(DATE_FORMATTER),
                        e.getDataFim().format(DATE_FORMATTER)
                })
        );
    }

    private void selecionarLinha(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int linhaSelecionada = tbEventos.getSelectedRow();
            if (linhaSelecionada != -1) {
                Long id = (Long) tbEventos.getValueAt(linhaSelecionada, 0);
                this.eventoSelecionadoParaEdicao = service.buscarPorId(id);
                if (this.eventoSelecionadoParaEdicao != null) {
                    tfId.setText(eventoSelecionadoParaEdicao.getId().toString());
                    tfNome.setText(eventoSelecionadoParaEdicao.getNome());
                    tfDataInicio.setText(eventoSelecionadoParaEdicao.getDataInicio().format(DATE_FORMATTER));
                    tfDataFim.setText(eventoSelecionadoParaEdicao.getDataFim().format(DATE_FORMATTER));
                    tfHora.setText(eventoSelecionadoParaEdicao.getHora().format(TIME_FORMATTER));
                    tfEndereco.setText(eventoSelecionadoParaEdicao.getEndereco());
                    tfFotoUrl.setText(eventoSelecionadoParaEdicao.getFotoUrl());
                    cbCursos.setSelectedItem(eventoSelecionadoParaEdicao.getCurso());
                    cbPalestrantes.setSelectedItem(eventoSelecionadoParaEdicao.getPalestrante());
                }
            }
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
        cbCursos.setSelectedItem(null);
        cbPalestrantes.setSelectedItem(null);
        tbEventos.clearSelection();
        this.eventoSelecionadoParaEdicao = null;
    }

    private Eventos getEventoDoFormulario() {
        if (tfNome.getText().trim().isEmpty() || tfDataInicio.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Data de Início são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            var evento = new Eventos();
            evento.setId(tfId.getText().isEmpty() ? null : Long.valueOf(tfId.getText()));
            evento.setNome(tfNome.getText());
            evento.setEndereco(tfEndereco.getText());
            evento.setFotoUrl(tfFotoUrl.getText());
            evento.setDataInicio(LocalDate.parse(tfDataInicio.getText(), DATE_FORMATTER));
            evento.setDataFim(LocalDate.parse(tfDataFim.getText(), DATE_FORMATTER));
            evento.setHora(LocalTime.parse(tfHora.getText(), TIME_FORMATTER));
            evento.setCurso((Cursos) cbCursos.getSelectedItem());
            evento.setPalestrante((Palestrantes) cbPalestrantes.getSelectedItem());
            return evento;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato. Verifique a data (dd/MM/aaaa) e a hora (HH:MM).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void finalizarAcao(boolean sucesso, String operacao) {
        String mensagem = sucesso ? "Evento " + operacao + " com sucesso!" : "Erro ao " + operacao.toLowerCase() + " o evento.";
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
            JOptionPane.showMessageDialog(this, "Para salvar um novo evento, limpe os campos primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Eventos evento = getEventoDoFormulario();
        if (evento != null) {
            finalizarAcao(service.salvar(evento), "salvo");
        }
    }

    private void editar(ActionEvent event) {
        if (tfId.getText().isEmpty() || this.eventoSelecionadoParaEdicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento da tabela para poder alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Eventos eventoDoFormulario = getEventoDoFormulario();
        if (eventoDoFormulario != null) {
            if (eventoSelecionadoParaEdicao.equals(eventoDoFormulario)) {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita nos dados do evento.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            finalizarAcao(service.atualizar(eventoDoFormulario), "alterado");
        }
    }

    private void excluir(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o evento selecionado?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            finalizarAcao(service.excluir(Long.valueOf(tfId.getText())), "excluído");
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