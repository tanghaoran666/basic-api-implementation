package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

import javax.validation.Valid;
import javax.xml.soap.SAAJResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController

public class RsController {

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;


  List<RsEvent> rsList;
  List<User> userList;




  @GetMapping("/rs/{index}")
  public ResponseEntity getOneList(@PathVariable int index){
    if(index <=0 || index>rsList.size()){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getBetweenList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){


    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    if(start<=0 || end > rsList.size() || start>end){
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsList.subList(start-1 , end));
  }

  @GetMapping("/users")
  public ResponseEntity getUserList(){
    return ResponseEntity.ok(userList);
  }

  @PostMapping("/rs/event")
  public ResponseEntity postList(@RequestBody @Valid RsEventPo rsEventPo){
    if(!userRepository.findById(rsEventPo.getUserPo().getId()).isPresent()){
      return ResponseEntity.badRequest().build();
    }
    RsEventPo savedRsEvent = rsEventRepository.save(rsEventPo);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{index}")
  public ResponseEntity patchListViaBody(@PathVariable int index,@RequestBody RsEvent rsEvent){
    RsEvent patchRsEvent = rsList.get(index-1);
    if(rsEvent.getEventName() != null) patchRsEvent.setEventName(rsEvent.getEventName());
    if(rsEvent.getKeyWord() != null) patchRsEvent.setKeyWord(rsEvent.getKeyWord());
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteList(@PathVariable int index){

    rsList.remove(index-1);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/reStart")
  public ResponseEntity reStart(){
    rsList.clear();
    return ResponseEntity.ok(null);
  }

}
