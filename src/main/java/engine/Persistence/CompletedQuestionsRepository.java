package engine.Persistence;

import engine.Model.CompletedQuestions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompletedQuestionsRepository extends PagingAndSortingRepository<CompletedQuestions, Integer> {

    /**
     * Find and return Page with all completed Question by User ID
     *
     * @param userId   User ID
     * @param pageable Page
     * @return Page with all completed Question by User ID
     */
    @Query(value = "select * from COMPLETED_QUESTIONS where user_id = ?1 order by completed_at desc",
            nativeQuery = true)
    Page<CompletedQuestions> findAllByUserId(int userId, Pageable pageable);
}
