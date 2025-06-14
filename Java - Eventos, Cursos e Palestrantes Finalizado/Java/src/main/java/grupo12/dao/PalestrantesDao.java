package grupo12.dao;

import grupo12.model.Palestrantes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PalestrantesDao extends Dao implements DaoInterface {

    @Override
    public Boolean insert(Object entity) {
        try {
            var palestrante = (Palestrantes) entity;
            var sql = "INSERT INTO palestrantes (nome, mini_curriculo, foto_url) VALUES (?, ?, ?)";
            var ps = getConnection().prepareStatement(sql);
            ps.setString(1, palestrante.getNome());
            ps.setString(2, palestrante.getMiniCurriculo());
            ps.setString(4, palestrante.getFotoUrl());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir palestrante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean update(Object entity) {
        try {
            var palestrante = (Palestrantes) entity;
            var sql = "UPDATE palestrantes SET nome = ?, mini_curriculo = ?, foto_url = ? WHERE id = ?";
            var ps = getConnection().prepareStatement(sql);
            ps.setString(1, palestrante.getNome());
            ps.setString(3, palestrante.getMiniCurriculo());
            ps.setString(4, palestrante.getFotoUrl());
            ps.setLong(5, palestrante.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao ATUALIZAR palestrante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Long pk) {
        try {
            var sql = "DELETE FROM palestrantes WHERE id = ?";
            var ps = getConnection().prepareStatement(sql);
            ps.setLong(1, pk);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao DELETAR palestrante: " + e.getMessage());
            return false;
        }
    }

    private Palestrantes buildFromResult(ResultSet rs) throws SQLException {
        return new Palestrantes(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("mini_curriculo"),
                rs.getString("foto_url")
        );
    }

    @Override
    public Object select(Long pk) {
        try {
            var sql = "SELECT * FROM palestrantes WHERE id = ?";
            var ps = getConnection().prepareStatement(sql);
            ps.setLong(1, pk);
            var rs = ps.executeQuery();
            if (rs.next()) {
                return buildFromResult(rs);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar palestrante por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Object> selectAll() {
        var lista = new ArrayList<Object>();
        try {
            var sql = "SELECT * FROM palestrantes";
            var rs = getConnection().prepareStatement(sql).executeQuery();
            while (rs.next()) {
                lista.add(buildFromResult(rs));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar palestrantes: " + e.getMessage());
        }
        return lista;
    }
}