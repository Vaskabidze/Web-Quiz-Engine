package engine.Controller;


import engine.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class QuestionController {

    @Autowired
    QuestionService service;

    /**
     * Take a JSON with new Question and save in DB
     *
     * @param question JSON with new Question
     * @param user     User
     * @return A JSON with Question from DB with ID without answers
     */
    @PostMapping(value = "/api/quizzes")
    public Question newQuestion(@Valid @RequestBody Question question, @AuthenticationPrincipal User user) {
        return service.saveQuestion(question, user);
    }

    /**
     * Delete Question from DB by ID
     * Return HTTP status 204(No content) if Question was deleted
     * or HTTP status 403(Forbidden) if the user is not the owner of the question
     * or QuestionNotFoundException with HTTP status 404(Not found) if Question not found in DB
     *
     * @param id   ID of the Question IN DB
     * @param user User
     */
    @DeleteMapping(value = "/api/quizzes/{id}")
    public void deleteQuestion(@PathVariable int id, @AuthenticationPrincipal User user) {
        service.deleteQuestion(id, user);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    /**
     * Find and return JSON with Question by ID from DB
     * or HTTP status 404(Not found) if Question not found in DB
     *
     * @param id ID of the Question in DB
     * @return Question from DB by ID
     */
    @GetMapping(value = "/api/quizzes/{id}")
    public Question getQuestion(@PathVariable int id) {
        return service
                .getQuestion(id);
    }

    /**
     * Return ALL questions from DB
     *
     * @return A JSON with ALL Questions from DB
     */
    @Deprecated
    @GetMapping(value = "/api/quizzesOld")
    public Iterable<Question> getAllQuestionsOld() {
        return service.getAllQuestions();
    }

    /**
     * Return ALL questions from DB with Paging
     *
     * @param page Page number (default = 0)
     *             // * @param pageSize Number of questions per page (default = 10)
     * @return Pages with all Questions from DB
     */
    @GetMapping(value = "/api/quizzes")
    public Page<Question> getAllQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return service.getAllQuestions(page, pageSize);
        // return new ResponseEntity<List<Question>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Take a JSON with answers.
     * Checks the answer and writes the question to the user database as resolved if the answer is correct.
     *
     * @param answer list of answers
     * @param id     ID of the Question in DB
     * @param user   User
     * @return True if answer is correct, False otherwise
     * or HTTP status 404(Not found) if Question not found in DB
     */
    @PostMapping(value = "/api/quizzes/{id}/solve")
    public Answer checkAnswer
    (@RequestBody Map<String, List<Integer>> answer,
     @PathVariable int id,
     @AuthenticationPrincipal User user) {
        return service.checkAnswer(answer.get("answer"), id, user);
    }

    /**
     * Find and return all solved Questions of this User with Paging and sorting
     *
     * @param user     User
     * @param page     Page number (default = 0)
     * @param pageSize Number of questions per page (default = 10)
     * @return Page with all resolved Questions by this User from DB
     */
    @GetMapping(value = "/api/quizzes/completed")
    public Page<CompletedQuestions> completed(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return service.getCompleted(user.getId(), page, pageSize);
    }
}


