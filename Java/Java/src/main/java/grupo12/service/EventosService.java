package grupo12.service;

import grupo12.dao.EventosDao;
import grupo12.model.Eventos;
import java.util.List;
import java.util.stream.Collectors;

public class EventosService {

    private EventosDao dao;

    public EventosService() {
        this.dao = new EventosDao();
    }

    public Boolean salvar(Eventos evento) {
        if (evento.getNome() == null || evento.getNome().trim().isEmpty()) {
            System.err.println("Erro: O nome do evento é obrigatório.");
            return false;
        }
        if (evento.getDataFim().isBefore(evento.getDataInicio())) {
            System.err.println("Erro: A data de fim não pode ser anterior à data de início.");
            return false;
        }
        if (dao.existeConflitoDeHorario(evento.getDataInicio(), evento.getHora(), null)) {
            System.err.println("Erro: Já existe um evento cadastrado para esta data e horário.");
            return false;
        }
        return this.dao.insert(evento);
    }

    public Boolean atualizar(Eventos evento) {
        if (evento.getId() == null || evento.getId() <= 0) {
            System.err.println("Erro: ID inválido para atualização.");
            return false;
        }
        if (dao.existeConflitoDeHorario(evento.getDataInicio(), evento.getHora(), evento.getId())) {
            System.err.println("Erro: Já existe um evento cadastrado para esta data e horário.");
            return false;
        }
        return this.dao.update(evento);
    }

    public Boolean excluir(Long id) {
        if (id == null || id <= 0) {
            System.err.println("Erro: ID inválido para exclusão.");
            return false;
        }
        return this.dao.delete(id);
    }

    public Eventos buscarPorId(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        Eventos evento = (Eventos) this.dao.select(id);
        if (evento != null && evento.getId() != null) {
            return evento;
        }
        return null;
    }

    public List<Eventos> listarTodos() {
        return this.dao.selectAll().stream()
                .map(obj -> (Eventos) obj)
                .collect(Collectors.toList());
    }
}