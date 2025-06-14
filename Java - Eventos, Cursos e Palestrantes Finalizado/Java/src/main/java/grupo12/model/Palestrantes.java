package grupo12.model;

public class Palestrantes {

    private Long id;
    private String nome;
    private String miniCurriculo;
    private String fotoUrl;

    public Palestrantes() {
    }

    public Palestrantes(Long id, String nome, String miniCurriculo, String fotoUrl) {
        this.id = id;
        this.nome = nome;
        this.miniCurriculo = miniCurriculo;
        this.fotoUrl = fotoUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMiniCurriculo() { return miniCurriculo; }
    public void setMiniCurriculo(String miniCurriculo) { this.miniCurriculo = miniCurriculo; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}