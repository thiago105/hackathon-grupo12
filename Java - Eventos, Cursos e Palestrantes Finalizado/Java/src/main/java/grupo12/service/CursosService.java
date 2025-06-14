package grupo12.service;

import grupo12.dao.CursosDao;
import grupo12.model.Cursos;
import java.util.List;
import java.util.stream.Collectors;

public class CursosService {

    private CursosDao dao;

    public CursosService() {
        this.dao = new CursosDao();
    }

    public Boolean salvar(Cursos curso) {
        if (curso.getNome() == null || curso.getNome().trim().isEmpty()) {
            System.err.println("Erro: O nome do curso é obrigatório.");
            return false;
        }
        return this.dao.insert(curso);
    }

    public Boolean atualizar(Cursos curso) {
        if (curso.getId() == null || curso.getId() <= 0) {
            System.err.println("Erro: ID inválido para atualização.");
            return false;
        }
        return this.dao.update(curso);
    }

    public Boolean excluir(Long id) {
        if (id == null || id <= 0) {
            System.err.println("Erro: ID inválido para exclusão.");
            return false;
        }
        return this.dao.delete(id);
    }

    public Cursos buscarPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        Cursos curso = (Cursos) this.dao.select(id);
        if (curso != null && curso.getId() != null) {
            return curso;
        }
        return null;
    }

    public List<Cursos> listarTodos() {
        return this.dao.selectAll().stream()
                .map(obj -> (Cursos) obj)
                .collect(Collectors.toList());
    }
}