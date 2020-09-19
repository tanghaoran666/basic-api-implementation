package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/rs/user")
    public void add_user_list(@RequestBody @Valid UserPo userPo){
        userRepository.save(userPo);
    }

    @GetMapping("/rs/user/{id}")
    public ResponseEntity get_user_in_database(@PathVariable int id){
        UserPo userPo = userRepository.findOneById(id);
        return ResponseEntity.ok(userPo);
    }

    @DeleteMapping("/rs/user/{id}")
    public ResponseEntity delete_user_in_database(@PathVariable int id){
        userRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }

}
