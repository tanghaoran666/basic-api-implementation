package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private List<User> userList = new ArrayList<>();

    @PostMapping("/user/")
    public void add_user_list(@RequestBody @Valid User user){
        userList.add(user);
    }

    @GetMapping("/user/")
    public List<User> get_user_list(){
        return userList;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    private ResponseEntity rsExceptionHandle(Exception e) {
        Error error = new Error();

        if(e instanceof MethodArgumentNotValidException){
            error.setError("invalid user");
        }
        return ResponseEntity.badRequest().body(error);

    }
}
