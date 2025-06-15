package grupo12.dao;

import grupo12.model.Cursos;
import grupo12.model.Eventos;
import grupo12.model.Palestrantes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventosDao extends Dao implements DaoInterface {

    @Override
    public Boolean insert(Object entity) {
        try {
            var evento = (Eventos) entity;
            var sql = "INSERT INTO eventos (nome, data_inicio, data_fim, hora, endereco, foto_url, curso_id, palestrante_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            var ps = getConnection().prepareStatement(sql);
            ps.setString(1, evento.getNome());
            ps.setDate(2, java.sql.Date.valueOf(evento.getDataInicio()));
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataFim()));
            ps.setTime(4, java.sql.Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getEndereco());
            ps.setString(6, evento.getFotoUrl());
            if (evento.getCurso() != null && evento.getCurso().getId() != null) ps.setLong(7, evento.getCurso().getId());
            else ps.setNull(7, Types.INTEGER);
            if (evento.getPalestrante() != null && evento.getPalestrante().getId() != null) ps.setLong(8, evento.getPalestrante().getId());
            else ps.setNull(8, Types.INTEGER);
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir evento: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(Object entity) {
        try {
            var evento = (Eventos) entity;
            var sql = "UPDATE eventos SET nome = ?, data_inicio = ?, data_fim = ?, hora = ?, endereco = ?, foto_url = ?, curso_id = ?, palestrante_id = ? WHERE id = ?";
            var ps = getConnection().prepareStatement(sql);
            ps.setString(1, evento.getNome());
            ps.setDate(2, java.sql.Date.valueOf(evento.getDataInicio()));
            ps.setDate(3, java.sql.Date.valueOf(evento.getDataFim()));
            ps.setTime(4, java.sql.Time.valueOf(evento.getHora()));
            ps.setString(5, evento.getEndereco());
            ps.setString(6, evento.getFotoUrl());
            if (evento.getCurso() != null && evento.getCurso().getId() != null) ps.setLong(7, evento.getCurso().getId());
            else ps.setNull(7, Types.INTEGER);
            if (evento.getPalestrante() != null && evento.getPalestrante().getId() != null) ps.setLong(8, evento.getPalestrante().getId());
            else ps.setNull(8, Types.INTEGER);
            ps.setLong(9, evento.getId());
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

    public boolean existeConflitoDeHorario(LocalDate data, LocalTime hora, Long idExcluido) {
        String sql = "SELECT COUNT(*) FROM eventos WHERE data_inicio = ? AND hora = ?";
        if (idExcluido != null) {
            sql += " AND id != ?";
        }
        try (var ps = getConnection().prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(data));
            ps.setTime(2, java.sql.Time.valueOf(hora));
            if (idExcluido != null) {
                ps.setLong(3, idExcluido);
            }
            var rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Eventos buildEventoFromResultSet(ResultSet rs) throws SQLException {
        Cursos curso = null;
        long cursoId = rs.getLong("curso_id");
        if (!rs.wasNull()) {
            curso = new Cursos(cursoId, rs.getString("curso_nome"));
        }
        Palestrantes palestrante = null;
        long palestranteId = rs.getLong("palestrante_id");
        if (!rs.wasNull()) {
            palestrante = new Palestrantes(palestranteId, rs.getString("palestrante_nome"), null, null);
        }
        return new Eventos(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDate("data_inicio").toLocalDate(),
                rs.getDate("data_fim").toLocalDate(),
                rs.getTime("hora").toLocalTime(),
                rs.getString("endereco"),
                rs.getString("foto_url"),
                curso,
                palestrante
        );
    }

    @Override
    public Object select(Long pk) {
        Eventos evento = null;
        var sql = "SELECT e.*, c.nome as curso_nome, p.nome as palestrante_nome " +
                "FROM eventos e " +
                "LEFT JOIN cursos c ON e.curso_id = c.id " +
                "LEFT JOIN palestrantes p ON e.palestrante_id = p.id " +
                "WHERE e.id = ?";
        try (var ps = getConnection().prepareStatement(sql)) {
            ps.setLong(1, pk);
            var rs = ps.executeQuery();
            if (rs.next()) {
                evento = buildEventoFromResultSet(rs);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar evento por ID: " + e.getMessage());
        }
        return evento != null ? evento : new Eventos();
    }

    @Override
    public List<Object> selectAll() {
        var listaDeEventos = new ArrayList<Object>();
        var sql = "SELECT e.*, c.nome as curso_nome, p.nome as palestrante_nome " +
                "FROM eventos e " +
                "LEFT JOIN cursos c ON e.curso_id = c.id " +
                "LEFT JOIN palestrantes p ON e.palestrante_id = p.id";
        try (var ps = getConnection().prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                listaDeEventos.add(buildEventoFromResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar eventos: " + e.getMessage());
        }
        return listaDeEventos;
    }
}