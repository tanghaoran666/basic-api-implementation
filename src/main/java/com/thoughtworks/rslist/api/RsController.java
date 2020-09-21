package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.RsService;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import lombok.Data;

import javax.validation.Valid;
import javax.xml.soap.SAAJResult;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

public class RsController {

  final RsService rsService;

  public RsController(RsService rsService){
    this.rsService = rsService;
  }

  @GetMapping("/rs/{id}")
  public ResponseEntity getOneList(@PathVariable int id){

    RsEvent rsEvent = rsService.getEvent(id);
    return ResponseEntity.ok(rsEvent);
  }

  @GetMapping("/rs/list")
  public ResponseEntity getBetweenList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    List<RsEvent> rsEvents = rsService.getEvents(start,end);
    return ResponseEntity.ok(rsEvents);
  }

  @GetMapping("/users")
  public ResponseEntity getUserList(){

    List<User> users = rsService.getUsers();
    return ResponseEntity.ok(users);
  }

  @PostMapping("/rs/event")
  public ResponseEntity postList(@RequestBody @Valid RsEvent rsEvent){
    rsService.postEvent(rsEvent);
    return ResponseEntity.created(null).build();
  }

  @PatchMapping("/rs/{eventId}")
  public ResponseEntity patchListViaBody(@PathVariable int eventId,@RequestBody RsEvent rsEvent){
    rsService.patchEvent(eventId,rsEvent);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{id}")
  public ResponseEntity deleteList(@PathVariable int id){

    rsService.deleteEvent(id);
    return ResponseEntity.ok(null);
  }



  @PostMapping("/rs/vote/{eventId}")
  public ResponseEntity voteRsEvent(@PathVariable int eventId, @RequestBody  Vote vote){
    rsService.vote(vote,eventId);
    return ResponseEntity.ok(null);
  }
}
