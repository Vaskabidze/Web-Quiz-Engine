package engine.Model;

import engine.Persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
    }

    /**
     * Save new User in DB
     *
     * @param user New User
     * @throws ResponseStatusException with HTTP status 400(Bad Request) If Email already in DB
     */
    public void saveUser(User user) throws ResponseStatusException {
        if (emailExisting(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Find and return User by Id from DB
     *
     * @param id ID of the User in DB
     * @return User from DB
     * @throws ResponseStatusException with status code 404(not found) if User not found in DB
     */
    public User getUserById(int id) throws ResponseStatusException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    /**
     * Delete User by email
     *
     * @param id id of the User for deleting
     * @throws ResponseStatusException If User not found in DB
     */
    public void deleteUser(int id) throws ResponseStatusException {
        if (!emailExisting(getUserById(id).getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    /**
     * Check if Email already existing in DB
     *
     * @param email Email for checking
     * @return TRUE if email already exist, otherwise FALSE
     */
    private boolean emailExisting(String email) {
        return userRepository
                .findByEmail(email)
                .isPresent();
    }
}
