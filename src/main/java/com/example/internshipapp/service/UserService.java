package com.example.internshipapp.service;

import com.example.internshipapp.exception.NoSuchRecordException;
import com.example.internshipapp.model.User;
import com.example.internshipapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Page<User> listAllByPage(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User findById(String id){
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();

        if(user!=null){
            return user;
        }
        else{
            throw new NoSuchRecordException("User with id not found");
        }
    }

    public User create(String username, String password, String firstName, String lastName, double toPay){

        User user = new User(username, password, firstName, lastName, toPay);
        return userRepository.save(user);
    }

    public User update(String id, String username, String password, String firstName, String lastName, double toPay){
        Optional<User> userOptional = userRepository.findById(id);

        if(!userOptional.isPresent()){
         throw new NoSuchRecordException("User with given id not found");
        }

        User user = userOptional.get();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setToPay(toPay);

        return userRepository.save(user);

    }

    public void delete(String id){
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()){
            userRepository.delete(user.get());
        }
        else{
            throw new NoSuchRecordException("User with given id not found");
        }
    }


}
