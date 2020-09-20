package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.RsEventReturn;
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

  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  VoteRepository voteRepository;

  @GetMapping("/rs/{id}")
  public ResponseEntity getOneList(@PathVariable int id){
    Optional<RsEventPo> existPo = rsEventRepository.findById(id);
    if(!existPo.isPresent()){
      throw new RsEventNotValidException("invalid id");
    }
    RsEventPo rsEventPo = existPo.get();
    RsEventReturn rsEvent = RsEventReturn.builder()
            .keyWord(rsEventPo.getKeyWord())
            .eventName(rsEventPo.getEventName())
            .id(rsEventPo.getId())
            .voteNum(rsEventPo.getVoteNum())
            .build();
    return ResponseEntity.ok(rsEvent);
  }

  @GetMapping("/rs/list")
  public ResponseEntity getBetweenList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    List<RsEventReturn> rsEvents = rsEventRepository.findAll().stream().map(
            item -> RsEventReturn.builder().keyWord(item.getKeyWord())
                    .eventName(item.getEventName())
                    .id(item.getId()).build()
    ).collect(Collectors.toList());


    if(start == null || end == null){
      return ResponseEntity.ok(rsEvents);
    }
    if(start<=0 || end > rsEventRepository.findAll().size() || start>end){
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsEvents.subList(start-1 , end));
  }

  @GetMapping("/users")
  public ResponseEntity getUserList(){
    List<User> users = userRepository.findAll().stream().map(
            item -> User.builder()
                    .voteNumber(item.getVoteNumber())
                    .phone(item.getPhone())
                    .name(item.getName())
                    .gender(item.getGender())
                    .email(item.getEmail())
                    .age(item.getAge())
                    .build()
    ).collect(Collectors.toList());
    return ResponseEntity.ok(users);
  }

  @PostMapping("/rs/event")
  public ResponseEntity postList(@RequestBody @Valid RsEvent rsEvent){
    if(!userRepository.findById(rsEvent.getUserId()).isPresent()){
      return ResponseEntity.badRequest().build();
    }
    UserPo userPo = userRepository.findById(rsEvent.getUserId()).get();
    RsEventPo rsEventPo = RsEventPo
                                    .builder()
                                    .keyWord(rsEvent.getKeyWord())
                                    .eventName(rsEvent.getEventName())
                                    .userPo(userPo).build();
    rsEventRepository.save(rsEventPo);
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/rs/vote/{eventId}")
  public ResponseEntity voteRsEvent(@PathVariable int eventId, @RequestBody  Vote vote){
    vote.setRsEventId(eventId);
    vote.setLocalDateTime(LocalDateTime.now());
    int voteNum = vote.getVoteNum();
    UserPo userPo = userRepository.findById(vote.getUserId()).get();
    if(voteNum > userPo.getVoteNumber()){
      throw new RsEventNotValidException("invalid vote number");
    }
    RsEventPo rsEventPo = rsEventRepository.findById(eventId).get();
    userPo.setVoteNumber(userPo.getVoteNumber() - voteNum);
    rsEventPo.setVoteNum(rsEventPo.getVoteNum() + voteNum);
    VotePo votePo = VotePo.builder().voteNum(voteNum)
            .userPo(userPo)
            .rsEventPo(rsEventPo)
            .localDateTime(vote.getLocalDateTime())
            .build();
    rsEventRepository.save(rsEventPo);
    userRepository.save(userPo);
    voteRepository.save(votePo);
    return ResponseEntity.ok(null);
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
    rsEventRepository.save(rsEventPo);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{id}")
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
