package com.example.app.user;

import com.example.app.user.dto.UserCreate;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.user.exceptions.IdNotFoundException;
import com.example.app.user.entity.Permissions;
import com.example.app.user.entity.User;
import com.example.app.user.dto.UserUpdate;
import com.example.app.user.exceptions.FieldAlreadyExistsException;
import com.example.app.user.exceptions.FieldNotFoundException;
import org.springframework.context.annotation.Primary;

@Service
@Primary
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        User response = userRepository.findByUsername(username)
            .orElseThrow(() -> new FieldNotFoundException("username"));
        return response;
    }

    @Transactional(readOnly = true)
    public User loadUserByEmail(String email) {
        User response = userRepository.findByEmail(email)
            .orElseThrow(() -> new FieldNotFoundException("email"));
        return response;
    }

    @Transactional(readOnly = true)
    public User loadUserById(String id) {
        User response = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        return response;
    }

    @Transactional
    public User createUser(UserCreate request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new FieldAlreadyExistsException("email");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new FieldAlreadyExistsException("username");
        }
        
        String hashedPassword = passwordEncoder.encode(request.password());
        User createdUser = new User(request.username(), hashedPassword, request.email());
        if (request.faculty() != null) {
            createdUser.setFaculty(request.faculty());
        }
        if (request.bio() != null) {
            createdUser.setBio(request.bio());
        }
        if (request.photoLink() != null) {
            createdUser.setPhotoLink(request.photoLink());
        }
        User response = userRepository.save(createdUser);
        return response;
    }
    
    @Transactional
    public void setUserLockStatus(String id, boolean isLocked) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setActive(isLocked);
        userRepository.save(user);
    }

    @Transactional
    public User updateUser(String id, UserUpdate request) {
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
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setExpired(true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserByForce(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        userRepository.delete(user);
    }

    @Transactional
    public Set<Permissions> updateUserPermissions(String id, Set<Permissions> permissions) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setAuthorities(permissions);
        userRepository.save(user);
        return user.getAuthorities();
    }

    @Transactional
    public Set<String> joinForum(String id, String forumName) {
        throw new UnsupportedOperationException("This feature is not yet available.");
    }

    @Transactional
    public Set<String> leaveForum(String id, String forumName) {
        throw new UnsupportedOperationException("This feature is not yet available.");
    }

    @Transactional
    public Integer incrementPosts(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setPosts(user.getPosts() + 1);
        userRepository.save(user);
        return user.getPosts();
    }

    @Transactional
    public Integer decrementPosts(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setPosts(user.getPosts() - 1);
        userRepository.save(user);
        return user.getPosts();
    }

    @Transactional
    public Integer incrementComments(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IdNotFoundException("Id not found: " + id));
        user.setComments(user.getComments() + 1);
        userRepository.save(user);
        return user.getComments();
    }

    @Transactional
    public Integer decrementComments(String id) {
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
