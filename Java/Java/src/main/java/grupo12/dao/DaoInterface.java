package grupo12.dao;

import java.util.List;

public interface DaoInterface {
    Boolean insert(Object entity);
    Boolean update(Object entity);
    Boolean delete(Long pk);
    Object select(Long pk);
    List<Object> selectAll();
}