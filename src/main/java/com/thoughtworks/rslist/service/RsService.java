package com.thoughtworks.rslist.service;


import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPo;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public void vote(Vote vote, int rsEventId){
        vote.setRsEventId(rsEventId);
        vote.setLocalDateTime(LocalDateTime.now());
        int voteNum = vote.getVoteNum();
        Optional<UserPo> userPo = userRepository.findById(vote.getUserId());
        Optional<RsEventPo> rsEventPo = rsEventRepository.findById(rsEventId);
        if(voteNum>userPo.get().getVoteNumber()
            ||!userPo.isPresent()
            ||!rsEventPo.isPresent()){
            throw new RsEventNotValidException("invalid vote param");
        }
        UserPo user = userPo.get();
        user.setVoteNumber(user.getVoteNumber() - voteNum);
        RsEventPo rsEvent = rsEventPo.get();
        rsEvent.setVoteNum(rsEvent.getVoteNum() + voteNum);
        VotePo votePo = VotePo.builder().voteNum(voteNum)
                .user(user)
                .rsEvent(rsEvent)
                .localDateTime(vote.getLocalDateTime())
                .build();
        rsEventRepository.save(rsEvent);
        userRepository.save(user);
        voteRepository.save(votePo);
    }

    public RsEvent getEvent(int id) {
        Optional<RsEventPo> existPo = rsEventRepository.findById(id);
        if(!existPo.isPresent()){
            throw new RsEventNotValidException("invalid id");
        }
        RsEventPo rsEventPo = existPo.get();
        RsEvent rsEvent = RsEvent.builder()
                .userId(rsEventPo.getId())
                .keyWord(rsEventPo.getKeyWord())
                .eventName(rsEventPo.getEventName())
                .rsEventId(rsEventPo.getId())
                .voteNum(rsEventPo.getVoteNum())
                .build();
        return rsEvent;
    }

    public List<RsEvent> getEvents(Integer start, Integer end) {
        List<RsEvent> rsEvents = rsEventRepository.findAll().stream().map(
                item -> RsEvent.builder().keyWord(item.getKeyWord())
                        .eventName(item.getEventName())
                        .userId(item.getUserPo().getId()).build()
        ).collect(Collectors.toList());


        if(start == null || end == null){
            return rsEvents;
        }
        if(start<=0 || end > rsEventRepository.findAll().size() || start>end){
            throw new RsEventNotValidException("invalid request param");
        }
        return rsEvents.subList(start-1 , end);
    }

    public List<User> getUsers() {
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
        return users;
    }

    public void postEvent(RsEvent rsEvent) {
        if(!userRepository.findById(rsEvent.getUserId()).isPresent()){
            throw new RsEventNotValidException("invalid param");
        }
        UserPo userPo = userRepository.findById(rsEvent.getUserId()).get();
        RsEventPo rsEventPo = RsEventPo
                .builder()
                .keyWord(rsEvent.getKeyWord())
                .eventName(rsEvent.getEventName())
                .userPo(userPo).build();
        rsEventRepository.save(rsEventPo);
    }

    public void patchEvent(int eventId, RsEvent rsEvent) {
        if(!rsEventRepository.findById(eventId).isPresent()){
            throw new RsEventNotValidException("invalid param");
        }
        int userId = rsEvent.getUserId();
        RsEventPo rsEventPo = rsEventRepository.findById(eventId).get();

        if(userId!= rsEventPo.getUserPo().getId()){
            throw new RsEventNotValidException("invalid param");
        }
        if(rsEvent.getKeyWord()!=null){
            rsEventPo.setKeyWord(rsEvent.getKeyWord());
        }
        if(rsEvent.getEventName()!=null){
            rsEventPo.setEventName(rsEvent.getEventName());
        }
        rsEventRepository.save(rsEventPo);
    }

    public void deleteEvent(int id) {
        rsEventRepository.deleteById(id);
    }
}
