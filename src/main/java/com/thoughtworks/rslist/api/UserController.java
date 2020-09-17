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

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/user/")
    public void add_user_list(@RequestBody @Valid User user){
        UserPo userPo = new UserPo();
        userPo.setAge(user.getAge());
        userPo.setEmail(user.getEmail());
        userPo.setGender(user.getGender());
        userPo.setName(user.getName());
        userPo.setPhone(user.getPhone());
        userPo.setVoteNumber(user.getVoteNumber());
//        userPo.builder().age(user.getAge()).email(user.getEmail()).gender(user.getGender())
//                .name(user.getName()).phone(user.getPhone()).build();

        userRepository.save(userPo);
    }


}
