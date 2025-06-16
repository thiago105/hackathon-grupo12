package grupo12.model;

import java.util.Objects;
public class Palestrantes {

    private Long id;
    private String nome;
    private String miniCurriculo;
    private String tema;
    private String fotoUrl;

    public Palestrantes() {
    }

    public Palestrantes(Long id, String nome, String miniCurriculo, String tema, String fotoUrl) {
        this.id = id;
        this.nome = nome;
        this.miniCurriculo = miniCurriculo;
        this.tema = tema;
        this.fotoUrl = fotoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMiniCurriculo() { return miniCurriculo; }
    public void setMiniCurriculo(String miniCurriculo) { this.miniCurriculo = miniCurriculo; }
    public String getTema() {return tema;}
    public void setTema(String tema) {this.tema = tema;}
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Palestrantes that = (Palestrantes) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}