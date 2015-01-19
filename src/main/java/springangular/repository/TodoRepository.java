package springangular.repository;

import org.springframework.data.repository.CrudRepository;
import springangular.domain.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {
    Todo findByDescription(String description);
}
