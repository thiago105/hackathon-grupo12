package grupo12.gui;


import grupo12.model.Eventos;
import grupo12.model.Palestrantes;
import grupo12.model.Cursos;
import grupo12.dao.PalestrantesDao;
import grupo12.dao.CursosDao;

import grupo12.service.EventosService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EventosGui extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private JTextField tfId, tfNome, tfDataInicio, tfDataFim, tfHora, tfEndereco, tfFotoUrl;
    private JComboBox<String> cbPalestrantes, cbCursos;
    private Long[] idsPalestrantes = {1L, 2L, 3L}; // IDs mockados
    private Long[] idsCursos = {101L, 102L, 103L}; // IDs mockados

    private JButton btSalvarNovo, btEditar, btExcluir, btLimpar, btSelecionarFoto;
    private JTable tbEventos;
    private final EventosService service;
    private Eventos eventoSelecionadoParaEdicao;

    public EventosGui(EventosService eventosService) {
        this.service = eventosService;
        setTitle("Cadastro de Eventos");
        setSize(900, 500);
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
        tfDataInicio = new JTextField(20);
        tfDataFim = new JTextField(20);
        tfHora = new JTextField(20);
        tfEndereco = new JTextField(20);
        tfFotoUrl = new JTextField(20);
        tfFotoUrl.setEditable(false);
        btSelecionarFoto = new JButton("Selecionar...");
        btSelecionarFoto.addActionListener(this::selecionarFoto);

        cbPalestrantes = new JComboBox<>(new String[]{"Maria Silva", "Carlos Lima", "Ana Paula"});
        cbCursos = new JComboBox<>(new String[]{"Análise e Desenvolvimento de Sistemas", "Gestão de TI", "Ciência da Computação"});

        painel.add(new JLabel("Palestrante"), utils.montarConstraints(2, 3));
        painel.add(cbPalestrantes, utils.montarConstraintsParaCampo(3, 3));

        painel.add(new JLabel("Curso"), utils.montarConstraints(0, 5));
        painel.add(cbCursos, utils.montarConstraintsParaCampo(1, 5));

        painel.add(new JLabel("ID"), utils.montarConstraints(0, 0));
        painel.add(tfId, utils.montarConstraintsParaCampo(1, 0));
        painel.add(new JLabel("Nome"), utils.montarConstraints(0, 1));
        painel.add(tfNome, utils.montarConstraintsParaCampo(1, 1));
        painel.add(new JLabel("Data Início (dd/MM/aaaa)"), utils.montarConstraints(0, 2));
        painel.add(tfDataInicio, utils.montarConstraintsParaCampo(1, 2));
        painel.add(new JLabel("Data Fim (dd/MM/aaaa)"), utils.montarConstraints(0, 3));
        painel.add(tfDataFim, utils.montarConstraintsParaCampo(1, 3));
        painel.add(new JLabel("Horário (HH:MM)"), utils.montarConstraints(2, 0));
        painel.add(tfHora, utils.montarConstraintsParaCampo(3, 0));
        painel.add(new JLabel("Endereço"), utils.montarConstraints(2, 1));
        painel.add(tfEndereco, utils.montarConstraintsParaCampo(3, 1));
        painel.add(new JLabel("URL da Foto"), utils.montarConstraints(2, 2));
        JPanel painelFoto = new JPanel(new BorderLayout(5, 0));
        painelFoto.add(tfFotoUrl, BorderLayout.CENTER);
        painelFoto.add(btSelecionarFoto, BorderLayout.EAST);
        painel.add(painelFoto, utils.montarConstraintsParaCampo(3, 2));

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

    private JScrollPane montarPainelSaida() {
        tbEventos = new JTable();
        tbEventos.setDefaultEditor(Object.class, null);
        tbEventos.getTableHeader().setReorderingAllowed(false);
        tbEventos.getSelectionModel().addListSelectionListener(this::selecionarLinha);
        tbEventos.setModel(montarTableModel());
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tbEventos.getColumnCount(); i++) {
            tbEventos.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        return new JScrollPane(tbEventos);
    }

    private DefaultTableModel montarTableModel() {
        var tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Início");
        tableModel.addColumn("Fim");
        tableModel.addColumn("Horário");
        tableModel.addColumn("Endereço");
        service.listarTodos().forEach(e ->
                tableModel.addRow(new Object[]{
                        e.getId(),
                        e.getNome(),
                        e.getDataInicio().format(DATE_FORMATTER),
                        e.getDataFim().format(DATE_FORMATTER),
                        e.getHora().format(TIME_FORMATTER),
                        e.getEndereco()
                })
        );
        return tableModel;
    }

    private void atualizarTabela() {
        tbEventos.setModel(montarTableModel());
    }

    private void salvar(ActionEvent event) {
        if (!tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Para salvar um novo evento, limpe os campos primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Eventos evento = getEventoDoFormulario();
        if (evento != null && service.salvar(evento)) {
            JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            atualizarTabela();
        } else if (evento != null) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o evento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar(ActionEvent event) {
        if (tfId.getText().isEmpty() || this.eventoSelecionadoParaEdicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione um evento da tabela para poder editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Eventos eventoDoFormulario = getEventoDoFormulario();
        if (eventoDoFormulario != null) {
            if (eventoSelecionadoParaEdicao != null && eventoSelecionadoParaEdicao.equals(eventoDoFormulario)) {
                JOptionPane.showMessageDialog(this, "Nenhuma alteração foi feita nos dados do evento.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(service.atualizar(eventoDoFormulario)) {
                JOptionPane.showMessageDialog(this, "Evento alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                atualizarTabela();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao editar o evento.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluir(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um evento na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o evento selecionado?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.valueOf(tfId.getText());
                if (service.excluir(id)) {
                    JOptionPane.showMessageDialog(this, "Evento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                    atualizarTabela();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir evento.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

    private void limparCampos() {
        tfId.setText(null);
        tfNome.setText(null);
        tfDataInicio.setText(null);
        tfDataFim.setText(null);
        tfHora.setText(null);
        tfEndereco.setText(null);
        tfFotoUrl.setText(null);
        tbEventos.clearSelection();
        cbPalestrantes.setSelectedIndex(-1);
        cbCursos.setSelectedIndex(-1);
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

            int indexPalestrante = cbPalestrantes.getSelectedIndex();
            int indexCurso = cbCursos.getSelectedIndex();
            evento.setPalestrante_id(Math.toIntExact(indexPalestrante >= 0 ? idsPalestrantes[indexPalestrante] : null));
            evento.setCurso_id(Math.toIntExact(indexCurso >= 0 ? idsCursos[indexCurso] : null));

            return evento;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato. Verifique se a data está como dd/MM/aaaa e a hora como HH:MM.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado ao ler os dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
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
                }
            }
        }
    }
}