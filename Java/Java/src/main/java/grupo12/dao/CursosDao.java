package grupo12.dao;

import grupo12.model.Cursos;
import java.util.ArrayList;
import java.util.List;

public class CursosDao extends Dao implements DaoInterface {

    @Override
    public Boolean insert(Object entity) {
        try {
            var curso = (Cursos) entity;
            var insertSql = "INSERT INTO cursos (nome) VALUES (?)";
            var ps = getConnection().prepareStatement(insertSql);
            ps.setString(1, curso.getNome());
            ps.execute();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao inserir curso: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean update(Object entity) {
        try {
            var curso = (Cursos) entity;
            var updateSql = "UPDATE cursos SET nome = ? WHERE id = ?";
            var ps = getConnection().prepareStatement(updateSql);
            ps.setString(1, curso.getNome());
            ps.setLong(2, curso.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao ATUALIZAR curso: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean delete(Long pk) {
        try {
            var deleteSql = "DELETE FROM cursos WHERE id = ?";
            var ps = getConnection().prepareStatement(deleteSql);
            ps.setLong(1, pk);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao DELETAR curso: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object select(Long pk) {
        var curso = new Cursos();
        try {
            var selectSql = "SELECT * FROM cursos WHERE id = ?";
            var ps = getConnection().prepareStatement(selectSql);
            ps.setLong(1, pk);
            var rs = ps.executeQuery();
            if (rs.next()) {
                curso.setId(rs.getLong("id"));
                curso.setNome(rs.getString("nome"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar curso por ID: " + e.getMessage());
        }
        return curso;
    }

    @Override
    public List<Object> selectAll() {
        List<Cursos> listaDeCursos = new ArrayList<>();
        try {
            var selectSql = "SELECT * FROM cursos";
            var rs = getConnection().prepareStatement(selectSql).executeQuery();
            while (rs.next()) {
                listaDeCursos.add(new Cursos(
                        rs.getLong("id"),
                        rs.getString("nome")
                ));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar cursos: " + e.getMessage());
        }
        return new ArrayList<>(listaDeCursos);
    }
}