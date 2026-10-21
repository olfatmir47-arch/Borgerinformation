package DAOs;

import entities.Category;
import entities.Occurrence;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface OccurrenceDAO<T extends Occurrence> {

    public T create(T object);

    public T findById(int id);

    public List<T> findAll();

    public T update(T object);

    public void delete(int id);

    public List<T> findByPostalCode(String postalCode);

    public Set<T> findByCity(String cityName);

    public List<T> findByCategory(int categoryId);

    public Set<T> findByDate(LocalDate startDate, LocalDate endDate);

    public Set<T> findByType(T object);
}
