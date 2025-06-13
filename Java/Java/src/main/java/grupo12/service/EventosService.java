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
        return this.dao.insert(evento);
    }

    public List<Eventos> listarTodos() {
        return this.dao.selectAll().stream()
                .map(obj -> (Eventos) obj)
                .collect(Collectors.toList());
    }
}