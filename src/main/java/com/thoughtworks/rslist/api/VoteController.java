package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord/byId")
    public ResponseEntity<List<Vote>> getVoteRecordById(@RequestParam Integer userId,@RequestParam Integer rsEventId,@RequestParam int pageIndex){
        Pageable pageable = PageRequest.of(pageIndex-1,5);
        List<Vote> votes = voteRepository.findAccordingToUserAndRsEvent(userId, rsEventId,pageable).stream().map(
                item -> Vote.builder().userId(item.getUser().getId())
                        .localDateTime(item.getLocalDateTime())
                        .voteNum(item.getVoteNum())
                        .rsEventId(item.getRsEvent().getId())
                        .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);

    }

    @GetMapping("/voteRecord/byTime")
    public ResponseEntity<List<Vote>> getVoteRecordByTime(@RequestParam String startTimeString, @RequestParam String endTimeString){
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(startTimeString,df);
        LocalDateTime endTime = LocalDateTime.parse(endTimeString,df);
        if(startTime.isAfter(endTime)){
            throw new RsEventNotValidException("invalid time");
        }
        List<Vote> votes = voteRepository.findAll().stream().map(
                item -> Vote.builder().userId(item.getUser().getId())
                        .localDateTime(item.getLocalDateTime())
                        .voteNum(item.getVoteNum())
                        .rsEventId(item.getRsEvent().getId())
                        .build()
        ).collect(Collectors.toList());

        List<Vote> voteInTime = votes.stream().filter(
                item -> item.getLocalDateTime().isBefore(endTime) && item.getLocalDateTime().isAfter(startTime)
        ).collect(Collectors.toList());
        if(voteInTime.size()==0){
            throw new RsEventNotValidException("invalid time");
        }
        return ResponseEntity.ok(voteInTime);

    }


}
