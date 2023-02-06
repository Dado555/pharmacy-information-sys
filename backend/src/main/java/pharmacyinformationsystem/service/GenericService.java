package pharmacyinformationsystem.service;

import java.util.List;

public interface GenericService<T> {

    List<T> findAll();

    T findOne(Integer id);

    T save(T entity);
}
