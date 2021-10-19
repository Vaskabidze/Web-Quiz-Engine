package engine.Persistence;

import engine.Model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {

}
