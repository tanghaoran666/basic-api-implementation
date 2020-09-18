package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userID,@RequestParam int rsEventId){
        List<Vote> votes = voteRepository.findAllByUserPoIdAndRsEventPoId(userID, rsEventId).stream().map(
                item -> Vote.builder().userId(item.getUserPo().getId())
                        .localDateTime(item.getLocalDateTime())
                        .voteNum(item.getVoteNum())
                        .rsEventId(item.getRsEventPo().getId())
                        .build()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);

    }
}
