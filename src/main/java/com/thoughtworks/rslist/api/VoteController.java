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


import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam Integer userId,@RequestParam Integer rsEventId,@RequestParam int pageIndex){
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
}
