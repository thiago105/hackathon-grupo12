package grupo12.dao;

import grupo12.model.Eventos;
import grupo12.model.Inscricoes;
import grupo12.model.Usuarios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscricoesDao extends Dao {


    public List<Inscricoes> selectAllWithDetails() {
        var inscricoes = new ArrayList<Inscricoes>();
        var sql = "SELECT i.id as inscricao_id, i.data_inscricao, i.aprovado, " +
                "u.id as usuario_id, u.nome as usuario_nome, " +
                "e.id as evento_id, e.nome as evento_nome " +
                "FROM inscricoes i " +
                "JOIN usuarios u ON i.usuario_id = u.id " +
                "JOIN eventos e ON i.evento_id = e.id " +
                "ORDER BY i.data_inscricao DESC";

        try (var ps = getConnection().prepareStatement(sql);
             var rs = ps.executeQuery()) {
            while (rs.next()) {
                inscricoes.add(buildFromResultSet(rs));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar inscrições: " + e.getMessage());
            e.printStackTrace();
        }
        return inscricoes;
    }

    public boolean updateStatusAprovacao(Long inscricaoId, int status) {
        var sql = "UPDATE inscricoes SET aprovado = ? WHERE id = ?";
        try (var ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setLong(2, inscricaoId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status da inscrição: " + e.getMessage());
            return false;
        }
    }

    private Inscricoes buildFromResultSet(ResultSet rs) throws SQLException {
        Inscricoes inscricao = new Inscricoes();
        inscricao.setId(rs.getLong("inscricao_id"));
        inscricao.setDataInscricao(rs.getTimestamp("data_inscricao").toLocalDateTime());

        Object aprovadoObj = rs.getObject("aprovado");
        if (aprovadoObj != null) {
            inscricao.setAprovado((Integer) aprovadoObj);
        } else {
            inscricao.setAprovado(null);
        }

        Usuarios usuario = new Usuarios();
        usuario.setId(rs.getLong("usuario_id"));
        usuario.setNome(rs.getString("usuario_nome"));
        inscricao.setUsuario(usuario);

        Eventos evento = new Eventos();
        evento.setId(rs.getLong("evento_id"));
        evento.setNome(rs.getString("evento_nome"));
        inscricao.setEvento(evento);

        return inscricao;
    }
}