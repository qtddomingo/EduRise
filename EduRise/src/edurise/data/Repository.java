package edurise.data;

import java.util.List;
import java.util.Optional;

/** Generic, type-safe data access contract. */
public interface Repository<T> {
    boolean save(T item);
    Optional<T> findById(int id);
    List<T> findAll();
}
