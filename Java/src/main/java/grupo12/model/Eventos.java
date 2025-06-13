package grupo12.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Eventos {

    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalTime hora;
    private String endereco;
    private String fotoUrl;

    public Eventos() {
    }

    public Eventos(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, LocalTime hora, String endereco, String fotoUrl) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.hora = hora;
        this.endereco = endereco;
        this.fotoUrl = fotoUrl;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}