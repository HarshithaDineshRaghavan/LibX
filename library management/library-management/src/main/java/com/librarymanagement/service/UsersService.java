package com.librarymanagement.service;

import com.librarymanagement.dto.UserRequest;
import com.librarymanagement.entity.Authors;
import com.librarymanagement.entity.Roles;
import com.librarymanagement.entity.Users;
import com.librarymanagement.exception.UserAlreadyExistsException;
import com.librarymanagement.repository.RolesRepository;
import com.librarymanagement.repository.UsersRepository;
import com.librarymanagement.repository.AuthorsRepository;
import com.librarymanagement.utils.LibraryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    private LibraryUtils libraryUtils;

    @Autowired
    private RolesRepository rolesRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Optional<Users> getUserById(Integer id) {
        return usersRepository.findById(id);
    }


    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }
    public void deleteUser(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));

        boolean hasActiveBorrows = user.getBorrows()
                .stream()
                .anyMatch(b -> Boolean.FALSE.equals(b.getIsReturned()));

        if (hasActiveBorrows) {
            throw new IllegalStateException("Cannot delete user with active borrowed books");
        }

        usersRepository.deleteById(id);
    }


    public Users updateUser(Integer id, Users updatedUser) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setName(updatedUser.getName());
        user.setUserName(updatedUser.getUserName());
        user.setPassword(updatedUser.getPassword());
        user.setRoleId(updatedUser.getRoleId());
        user.setEmail(updatedUser.getEmail());
        return usersRepository.save(user);
    }
    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }
    public Users saveUser(UserRequest userRequest) {
        Optional<Users> existingUser = usersRepository.findByUserName(userRequest.getUserName());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        Users user = libraryUtils.mapDtoEntity(userRequest);
        user.setMembership(userRequest.getMembership());
        Roles defaultRole = rolesRepository.findFirstByRole("user");
        if (defaultRole == null) {
            throw new IllegalArgumentException("Default role not found: user");
        }

        user.setRoleId(defaultRole.getRoleId());
        user.setEmail(userRequest.getEmail());
        user.setMembership(userRequest.getMembership());
        return usersRepository.save(user);
    }
    public List<Users> searchUsers(String q) {

        List<Users> users = usersRepository.findAll();

        if (q == null || q.isBlank()) {
            return users;
        }

        String s = q.toLowerCase();

        return users.stream()
                .filter(u ->
                        (u.getName() != null && u.getName().toLowerCase().contains(s))
                                || u.getUserName()!= null && u.getUserName().toLowerCase().contains(s)
                                || u.getEmail()!= null && u.getEmail().toLowerCase().contains(s)
                )
                .toList();
    }
}
