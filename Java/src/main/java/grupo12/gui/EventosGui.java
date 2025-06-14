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
import java.util.ArrayList;
import java.util.List;

public class EventosGui extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private JTextField tfId, tfNome, tfDataInicio, tfDataFim, tfHora, tfEndereco, tfFotoUrl;
    private JButton btSalvarNovo, btAlterar, btExcluir, btLimpar, btSelecionarFoto;
    private JTable tbEventos;
    private DefaultTableModel tableModel;

    private EventosService service;
    private List<Eventos> listaDeEventosCache;

    public EventosGui() {
        setTitle("Cadastro de Eventos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.service = new EventosService();
        this.listaDeEventosCache = new ArrayList<>();

        getContentPane().add(montarPainelEntrada(), BorderLayout.NORTH);
        getContentPane().add(montarPainelSaida(), BorderLayout.CENTER);

        atualizarTabela();
    }

    private JPanel montarPainelEntrada() {
        var painel = new JPanel(new GridBagLayout());
        var utils = new GuiUtils();

        // ... (as outras inicializações de tfId, tfNome, etc. continuam iguais)
        tfId = new JTextField(20);
        tfId.setEditable(false);
        tfNome = new JTextField(20);
        tfDataInicio = new JTextField(20);
        tfDataFim = new JTextField(20);
        tfHora = new JTextField(20);
        tfEndereco = new JTextField(20);
        tfFotoUrl = new JTextField(20);
        // --- INÍCIO DA MUDANÇA ---
        tfFotoUrl.setEditable(false); // Torna o campo de texto não editável
        btSelecionarFoto = new JButton("Selecionar...");
        btSelecionarFoto.addActionListener(this::selecionarFoto);
        // --- FIM DA MUDANÇA ---

        // A montagem dos outros campos continua a mesma
        painel.add(new JLabel("ID"), utils.montarConstraints(0, 0));
        painel.add(tfId, utils.montarConstraintsParaCampo(1, 0));
        painel.add(new JLabel("Nome"), utils.montarConstraints(0, 1));
        painel.add(tfNome, utils.montarConstraintsParaCampo(1, 1));
        painel.add(new JLabel("Data Início (dd/MM/aaaa)"), utils.montarConstraints(0, 2));
        painel.add(tfDataInicio, utils.montarConstraintsParaCampo(1, 2));
        painel.add(new JLabel("Data Fim (dd/MM/aaaa)"), utils.montarConstraints(0, 3));
        painel.add(tfDataFim, utils.montarConstraintsParaCampo(1, 3));
        painel.add(new JLabel("Hora (HH:MM)"), utils.montarConstraints(2, 0));
        painel.add(tfHora, utils.montarConstraintsParaCampo(3, 0));
        painel.add(new JLabel("Endereço"), utils.montarConstraints(2, 1));
        painel.add(tfEndereco, utils.montarConstraintsParaCampo(3, 1));

        // --- INÍCIO DA MUDANÇA NO LAYOUT DA URL DA FOTO ---
        painel.add(new JLabel("URL da Foto"), utils.montarConstraints(2, 2));

        // Cria um painel interno para juntar o campo de texto e o botão
        JPanel painelFoto = new JPanel(new BorderLayout(5, 0));
        painelFoto.add(tfFotoUrl, BorderLayout.CENTER);
        painelFoto.add(btSelecionarFoto, BorderLayout.EAST);

        // Adiciona o painel interno (com campo+botão) ao painel principal
        painel.add(painelFoto, utils.montarConstraintsParaCampo(3, 2));
        // --- FIM DA MUDANÇA ---

        // ... (a criação dos outros botões continua a mesma)
        btSalvarNovo = new JButton("Salvar Novo");
        btSalvarNovo.addActionListener(this::salvar);
        btAlterar = new JButton("Alterar Selecionado");
        btAlterar.addActionListener(this::alterar);
        btExcluir = new JButton("Excluir Selecionado");
        btExcluir.addActionListener(this::excluir);
        btLimpar = new JButton("Limpar Campos");
        btLimpar.addActionListener(e -> limparCampos());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btSalvarNovo);
        painelBotoes.add(btAlterar);
        painelBotoes.add(btExcluir);
        painelBotoes.add(btLimpar);
        painel.add(painelBotoes, utils.montarConstraints(0, 4, 4));

        return painel;
    }

    private void salvar(ActionEvent event) {
        if (!tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Para salvar um novo evento, limpe os campos primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Eventos evento = getEventoDoFormulario();
        if (evento == null) return;

        if (service.salvar(evento)) {
            JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o evento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterar(ActionEvent event) {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um evento da tabela para poder alterar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Eventos evento = getEventoDoFormulario();
        if (evento == null) return;

        if(service.atualizar(evento)) {
            JOptionPane.showMessageDialog(this, "Evento alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            atualizarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar o evento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
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
            return evento;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato. Verifique se a data está como dd/MM/aaaa e a hora como HH:MM.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado ao ler os dados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
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
        tableModel.addColumn("Hora");
        tableModel.addColumn("Endereço");
        tbEventos = new JTable(tableModel);
        tbEventos.setDefaultEditor(Object.class, null);
        tbEventos.getSelectionModel().addListSelectionListener(this::selecionarLinha);
        return new JScrollPane(tbEventos);
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        this.listaDeEventosCache = service.listarTodos();

        if (this.listaDeEventosCache != null) {
            for (Eventos e : this.listaDeEventosCache) {
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

    /**
     * Abre uma janela para o usuário selecionar um arquivo de imagem.
     */
    private void selecionarFoto(ActionEvent event) {
        // Cria o seletor de arquivos
        JFileChooser seletorDeArquivo = new JFileChooser();
        seletorDeArquivo.setDialogTitle("Selecione uma imagem para o evento");

        // Define um filtro para mostrar apenas arquivos de imagem comuns
        seletorDeArquivo.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Arquivos de Imagem", "jpg", "jpeg", "png", "gif"));

        // Mostra a janela de diálogo
        int resultado = seletorDeArquivo.showOpenDialog(this);

        // Verifica se o usuário selecionou um arquivo e clicou em "Abrir"
        if (resultado == JFileChooser.APPROVE_OPTION) {
            // Pega o arquivo que foi selecionado
            java.io.File arquivoSelecionado = seletorDeArquivo.getSelectedFile();
            // Coloca o caminho completo do arquivo no campo de texto
            tfFotoUrl.setText(arquivoSelecionado.getAbsolutePath());
        }
    }

    private void selecionarLinha(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
            int linhaSelecionada = tbEventos.getSelectedRow();
            if (linhaSelecionada != -1) {
                Eventos eventoSelecionado = this.listaDeEventosCache.get(linhaSelecionada);

                tfId.setText(eventoSelecionado.getId().toString());
                tfNome.setText(eventoSelecionado.getNome());
                tfDataInicio.setText(eventoSelecionado.getDataInicio().format(DATE_FORMATTER));
                tfDataFim.setText(eventoSelecionado.getDataFim().format(DATE_FORMATTER));
                tfHora.setText(eventoSelecionado.getHora().format(TIME_FORMATTER));
                tfEndereco.setText(eventoSelecionado.getEndereco());
                tfFotoUrl.setText(eventoSelecionado.getFotoUrl());
            }
        }
    }
}