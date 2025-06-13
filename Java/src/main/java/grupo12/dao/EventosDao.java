package grupo12.dao;

import grupo12.model.Eventos;

import java.util.ArrayList;
import java.util.List;

public class EventosDao extends Dao implements DaoInterface {

    @Override
    public Boolean insert(Object entity) {
        try {
            var evento = (Eventos) entity;
            var insertSql = "INSERT INTO eventos (nome, data_inicio, data_fim, hora, endereco, foto_url) VALUES (?, ?, ?, ?, ?, ?)";
            var ps = getConnection().prepareStatement(insertSql);
            ps.setString(1, evento.getNome());
            ps.setDate(2, java.sql.Date.valueOf(evento.getDataInicio()));
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataFim()));
            ps.setTime(4, java.sql.Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getEndereco());
            ps.setString(6, evento.getFotoUrl());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Object> selectAll() {
        List<Eventos> listaDeEventos = new ArrayList<>();
        try {
            var selectSql = "SELECT * FROM eventos";
            var rs = getConnection().prepareStatement(selectSql).executeQuery();
            while (rs.next()) {
                listaDeEventos.add(new Eventos(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getDate("data_inicio").toLocalDate(),
                        rs.getDate("data_fim").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("endereco"),
                        rs.getString("foto_url")
                ));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        return new ArrayList<>(listaDeEventos);
    }

    // --- Métodos de update e delete como placeholders ---
    @Override
    public Boolean update(Object entity) {
        return null;
    }

    @Override
    public Boolean delete(Long pk) {
        return null;
    }

    @Override
    public List<Object> select(Long pk) {
        return List.of();
    }
}