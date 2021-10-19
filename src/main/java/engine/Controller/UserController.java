package engine.Controller;

import engine.Model.User;
import engine.Model.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService service;

    /**
     * Takes a JSON with new User and save in DB
     * Return HTTP status 200(ok) or HTTP status 400(Bad Request) If Email already in DB
     *
     * @param user User for save
     */
    @PostMapping("/api/register")
    public void saveUser(@Valid @RequestBody User user) {
        service.saveUser(user);
        throw new ResponseStatusException(HttpStatus.OK);
    }

    /**
     * Find and return JSON with User from DB by ID from DB
     * or return status code 404(Not found) if User not found in DB
     *
     * @param id id of the User in DB
     * @return JSON with User from DB
     */
    @GetMapping("/api/{id}")
    public User getUser(@PathVariable int id) {
        return service.getUserById(id);
    }

    /**
     * Find and delete User from DB by ID
     * Return HTTP status 200(ok) or HTTP status 404(Not found) If Email already in DB
     *
     * @param id id of the User in DB
     */
    @DeleteMapping("/api/user/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
        throw new ResponseStatusException(HttpStatus.OK);
    }
}
