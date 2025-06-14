package grupo12.service;

import grupo12.dao.PalestrantesDao;
import grupo12.model.Palestrantes;
import java.util.List;
import java.util.stream.Collectors;

public class PalestrantesService {

    private final PalestrantesDao dao;

    public PalestrantesService() {
        this.dao = new PalestrantesDao();
    }

    public boolean salvar(Palestrantes palestrante) {
        if (palestrante.getNome() == null || palestrante.getNome().trim().isEmpty() ||
                palestrante.getTema() == null || palestrante.getTema().trim().isEmpty()) {
            System.err.println("Erro: Nome e Tema são obrigatórios.");
            return false;
        }
        return dao.insert(palestrante);
    }

    public boolean atualizar(Palestrantes palestrante) {
        if (palestrante.getId() == null || palestrante.getId() <= 0) {
            System.err.println("Erro: ID inválido para atualização.");
            return false;
        }
        return dao.update(palestrante);
    }

    public boolean excluir(Long id) {
        if (id == null || id <= 0) {
            System.err.println("Erro: ID inválido para exclusão.");
            return false;
        }
        return dao.delete(id);
    }

    public Palestrantes buscarPorId(Long id) {
        return (Palestrantes) dao.select(id);
    }

    public List<Palestrantes> listarTodos() {
        return dao.selectAll().stream()
                .map(p -> (Palestrantes) p)
                .collect(Collectors.toList());
    }
}