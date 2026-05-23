package com.example.app.user;

import java.util.EnumSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.user.dto.UserProfileResponse;
import com.example.app.user.exceptions.EmailNotFoundException;
import com.example.app.user.exceptions.IdNotFoundException;
import com.example.app.user.exceptions.MissingFieldsException;
import com.example.app.user.exceptions.VerificationException;
import com.example.app.user.models.Permissions;
import com.example.app.user.models.User;
import com.example.app.user.models.UserUpdate;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
        UserProfileResponse response = new UserProfileResponse(user);
        return response;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse loadUserByEmail(String email) throws EmailNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EmailNotFoundException("Email not found: " + email));
        UserProfileResponse response = new UserProfileResponse(user);
        return response;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse loadUserById(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        UserProfileResponse response = new UserProfileResponse(user);
        return response;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse authenticateUser(String identifier, String password) throws VerificationException, EmailNotFoundException, UsernameNotFoundException {
        User user = identifier.contains("@") ? userRepository.findByEmail(identifier)
                .orElseThrow(() -> new EmailNotFoundException("Email not found: " + identifier)) : userRepository.findByUsername(identifier)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + identifier));
        if (passwordEncoder.matches(password, user.getPassword())) {
            UserProfileResponse response = new UserProfileResponse(user);
            return response;
        }
        throw new VerificationException("Could not authenticate user.");
    }

    @Transactional
    public UserProfileResponse createUser(String username, String password, String email) throws MissingFieldsException, IllegalArgumentException {
        if (username != null || password != null || email != null) {
            throw new MissingFieldsException("Unable to create account. Please fill in all required fields.");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User createdUser = new User(username, hashedPassword, email);
        userRepository.save(createdUser);
        UserProfileResponse response = new UserProfileResponse(createdUser);
        return response;
    }

    @Transactional
    public void setUserLockStatus(String id, boolean isLocked) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setActive(isLocked);
        userRepository.save(user);
    }

    @Transactional
    public User updateUser(String id, UserUpdate request) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        if (request.username() != null) {
            user.setUsername(request.username());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.password() != null) {
            String hashedPassword = passwordEncoder.encode(request.password());
            user.setPassword(hashedPassword);
        }
        if (request.faculty() != null) {
            user.setFaculty(request.faculty());
        }
        if (request.bio() != null) {
            user.setBio(request.bio());
        }
        userRepository.save(user);
        return user;
    }

    @Transactional
    public void deleteUser(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setExpired(true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserByForce(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public EnumSet<Permissions> updateUserPermissions(String id, EnumSet<Permissions> permissions) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setAuthorities(permissions);
        userRepository.save(user);
        return user.getAuthorities();
    }

    @Transactional
    public Set<String> joinForum(String id, String forumName) throws IdNotFoundException {
        throw new UnsupportedOperationException("This feature is not yet available.");
    }

    @Transactional
    public Set<String> leaveForum(String id, String forumName) throws IdNotFoundException {
        throw new UnsupportedOperationException("This feature is not yet available.");
    }

    @Transactional
    public Integer incrementPosts(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setPosts(user.getPosts() + 1);
        userRepository.save(user);
        return user.getPosts();
    }

    @Transactional
    public Integer decrementPosts(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setPosts(user.getPosts() - 1);
        userRepository.save(user);
        return user.getPosts();
    }

    @Transactional
    public Integer incrementComments(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setComments(user.getComments() + 1);
        userRepository.save(user);
        return user.getComments();
    }

    @Transactional
    public Integer decrementComments(String id) throws IdNotFoundException {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setComments(user.getComments() - 1);
        userRepository.save(user);
        return user.getComments();
    }

    @Transactional
    public void setUserOnlineStatus(String id, boolean isOnline) {

    }
}
