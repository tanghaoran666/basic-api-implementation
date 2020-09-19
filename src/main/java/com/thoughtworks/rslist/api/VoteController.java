package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
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
    public ResponseEntity<List<Vote>> getVoteRecordByTime(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime, @RequestParam int pageIndex){
        Pageable pageable = PageRequest.of(pageIndex-1,5);
        List<Vote> votes = voteRepository.findAll().stream().map(
                item -> Vote.builder().userId(item.getUser().getId())
                        .localDateTime(item.getLocalDateTime())
                        .voteNum(item.getVoteNum())
                        .rsEventId(item.getRsEvent().getId())
                        .build()
        ).collect(Collectors.toList());
        List<Vote> votesByTime = new ArrayList();
        for (Vote vote : votes) {
            if(vote.getLocalDateTime().isAfter(startTime)&&vote.getLocalDateTime().isBefore(endTime)){
                votesByTime.add(vote);
            }
        }
        return ResponseEntity.ok(votesByTime);

    }
}
