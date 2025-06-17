package grupo12.service;

import grupo12.dao.InscricoesDao;
import grupo12.model.Inscricoes;
import java.util.List;

public class InscricoesService {

    private final InscricoesDao dao = new InscricoesDao();

    public List<Inscricoes> listarTodas() {
        return dao.selectAllWithDetails();
    }

    public boolean aprovarInscricao(Long inscricaoId) {
        if (inscricaoId == null || inscricaoId <= 0) {
            System.err.println("Erro: ID de inscrição inválido.");
            return false;
        }
        return dao.updateStatusAprovacao(inscricaoId, 1);
    }

    public boolean reprovarInscricao(Long inscricaoId) {
        if (inscricaoId == null || inscricaoId <= 0) {
            System.err.println("Erro: ID de inscrição inválido.");
            return false;
        }
        return dao.updateStatusAprovacao(inscricaoId, 0);
    }
}