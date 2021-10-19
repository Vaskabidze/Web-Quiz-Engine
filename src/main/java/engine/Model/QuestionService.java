package engine.Model;

import engine.Persistence.CompletedQuestionsRepository;
import engine.Persistence.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repository;
    @Autowired
    private CompletedQuestionsRepository completedQuestionsRepository;

    /**
     * Save new Question and save in DB
     *
     * @param question new Question
     * @param user     User
     * @return Question FROM DB without answers
     */
    public Question saveQuestion(Question question, User user) {
        question.setUser(user);
        return repository.save(question);
    }

    /**
     * Chek is User owner if this question
     *
     * @param id   ID question in DB
     * @param user User
     * @return True if User is owner of this question, otherwise false
     * @throws QuestionNotFoundException with HTTP status 404(Not found) if Question not found in DB
     */
    public boolean checkOwner(int id, User user) throws QuestionNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(QuestionNotFoundException::new)
                .getUser().getId() == user.getId();
    }

    /**
     * Delete Question from DB
     *
     * @param id   ID Question in DB
     * @param user User
     * @throws ResponseStatusException HTTP status 403(Forbidden) if User is not owner of the question
     *                                 or QuestionNotFoundException with HTTP status 404(Not found) if Question not found in DB
     */
    public void deleteQuestion(int id, User user) throws ResponseStatusException {
        // Check is User owner of this questions
        if (checkOwner(id, user)) {
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Checks the answer and writes the question to the User database as resolved if the answer is correct.
     *
     * @param answer list of answers
     * @param id     ID of the Question in DB
     * @param user   User
     * @return True if answer is correct, False otherwise
     * or HTTP status 404(Not found) if Question not found in DB
     */
    public Answer checkAnswer(List<Integer> answer, int id, User user) {
        if (getQuestion(id).getAnswer().equals(answer)) {
            completedQuestionsRepository.save(new CompletedQuestions(id, user));
            return Answer.CORRECT_ANSWER;
        } else {
            return Answer.INCORRECT_ANSWER;
        }
    }

    /**
     * Find and return Question by ID from DB
     *
     * @param id ID of the Question in DB
     * @return qUESTION FROM DB
     * @throws QuestionNotFoundException with HTTP status 404(Not found) if Question not found in DB
     */
    public Question getQuestion(int id) throws QuestionNotFoundException {
        return repository
                .findById(id)
                .orElseThrow(QuestionNotFoundException::new);
    }

    /**
     * Return ALL questions from DB
     *
     * @return Iterable with all Questions from DB
     */
    public Iterable<Question> getAllQuestions() {
        return repository.findAll();
    }

    /**
     * Return ALL questions from DB with Paging
     *
     * @param pageNo   Page number
     * @param pageSize Number of questions per page
     * @return Pages with all Questions from DB
     */
    public Page<Question> getAllQuestions(int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return repository.findAll(paging);

    }

    /**
     * Find and return all solved Questions of this User with Paging and sorting
     *
     * @param userId   User ID
     * @param pageNo   Page number
     * @param pageSize Number of questions per page
     * @return Page with all resolved Questions by this User from DB
     */
    public Page<CompletedQuestions> getCompleted(int userId, int pageNo, int pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        return completedQuestionsRepository.findAllByUserId(userId, paging);
    }
}
