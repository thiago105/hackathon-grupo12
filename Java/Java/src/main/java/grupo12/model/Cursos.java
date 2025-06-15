package grupo12.model;

import java.util.Objects;

public class Cursos {

    private Long id;
    private String nome;

    public Cursos() {
    }

    public Cursos(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cursos cursos = (Cursos) o;
        return Objects.equals(id, cursos.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}