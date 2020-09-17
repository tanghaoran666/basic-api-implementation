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






  @GetMapping("/rs/{index}")
  public ResponseEntity getOneList(@PathVariable int index){
    if(index <=0 || index>rsEventRepository.findAll().size()){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsEventRepository.findAll().get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getBetweenList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){


    if(start == null || end == null){
      return ResponseEntity.ok(rsEventRepository.findAll());
    }
    if(start<=0 || end > rsEventRepository.findAll().size() || start>end){
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsEventRepository.findAll().subList(start-1 , end));
  }

  @GetMapping("/users")
  public ResponseEntity getUserList(){
    return ResponseEntity.ok(rsEventRepository.findAll());
  }

  @PostMapping("/rs/event")
  public ResponseEntity postList(@RequestBody @Valid RsEventPo rsEventPo){
    if(!userRepository.findById(rsEventPo.getUserPo().getId()).isPresent()){
      return ResponseEntity.badRequest().build();
    }
    RsEventPo savedRsEvent = rsEventRepository.save(rsEventPo);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{eventId}")
  public ResponseEntity patchListViaBody(@PathVariable int eventId,@RequestBody RsEvent rsEvent){
    if(!rsEventRepository.findById(eventId).isPresent()){
      return ResponseEntity.badRequest().build();
    }
    int userId = rsEvent.getUserId();
    RsEventPo rsEventPo = rsEventRepository.findById(eventId).get();

    if(userId!= rsEventPo.getUserPo().getId()){
      return ResponseEntity.badRequest().build();
    }
    if(rsEvent.getKeyWord()!=null){
      rsEventPo.setKeyWord(rsEvent.getKeyWord());
    }
    if(rsEvent.getEventName()!=null){
      rsEventPo.setEventName(rsEvent.getEventName());
    }
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity deleteList(@PathVariable int id){

    rsEventRepository.deleteById(id);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/reStart")
  public ResponseEntity reStart(){
    rsEventRepository.deleteAll();
    return ResponseEntity.ok(null);
  }

}
