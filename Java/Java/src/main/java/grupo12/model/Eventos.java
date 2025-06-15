package grupo12.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Eventos {

    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalTime hora;
    private String endereco;
    private String fotoUrl;
    private Cursos curso;
    private Palestrantes palestrante;

    public Eventos() {
    }

    public Eventos(Long id, String nome, LocalDate dataInicio, LocalDate dataFim, LocalTime hora, String endereco, String fotoUrl,  Cursos curso, Palestrantes palestrante) {
        this.id = id;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.hora = hora;
        this.endereco = endereco;
        this.fotoUrl = fotoUrl;
        this.curso = curso;
        this.palestrante = palestrante;
    }

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

    public Cursos getCurso() {
        return curso;
    }
    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    public Palestrantes getPalestrante() {
        return palestrante;
    }
    public void setPalestrante(Palestrantes palestrante) {
        this.palestrante = palestrante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Eventos eventos = (Eventos) o;
        return Objects.equals(id, eventos.id) && Objects.equals(nome, eventos.nome) && Objects.equals(dataInicio, eventos.dataInicio) && Objects.equals(hora, eventos.hora) && Objects.equals(endereco, eventos.endereco) && Objects.equals(fotoUrl, eventos.fotoUrl) && Objects.equals(curso, eventos.curso) && Objects.equals(palestrante, eventos.palestrante);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, dataInicio, hora, endereco, fotoUrl, curso, palestrante);
    }
}