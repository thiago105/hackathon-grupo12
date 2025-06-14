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
    public Boolean update(Object entity) {
        try {
            var evento = (Eventos) entity;
            var updateSql = "UPDATE eventos SET nome = ?, data_inicio = ?, data_fim = ?, hora = ?, endereco = ?, foto_url = ? WHERE id = ?";
            var ps = getConnection().prepareStatement(updateSql);
            ps.setString(1, evento.getNome());
            ps.setDate(2, java.sql.Date.valueOf(evento.getDataInicio()));
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataFim()));
            ps.setTime(4, java.sql.Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getEndereco());
            ps.setString(6, evento.getFotoUrl());
            ps.setLong(7, evento.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao ATUALIZAR evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Long pk) {
        try {
            var deleteSql = "DELETE FROM eventos WHERE id = ?";
            var ps = getConnection().prepareStatement(deleteSql);
            ps.setLong(1, pk);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao DELETAR evento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object select(Long pk) {
        var evento = new Eventos();
        try {
            var selectSql = "SELECT * FROM eventos WHERE id = ?";
            var ps = getConnection().prepareStatement(selectSql);
            ps.setLong(1, pk);
            var rs = ps.executeQuery();

            if (rs.next()) {
                evento.setId(rs.getLong("id"));
                evento.setNome(rs.getString("nome"));
                evento.setDataInicio(rs.getDate("data_inicio").toLocalDate());
                evento.setDataFim(rs.getDate("data_fim").toLocalDate());
                evento.setHora(rs.getTime("hora").toLocalTime());
                evento.setEndereco(rs.getString("endereco"));
                evento.setFotoUrl(rs.getString("foto_url"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento por ID: " + e.getMessage());
        }
        return evento;
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
}