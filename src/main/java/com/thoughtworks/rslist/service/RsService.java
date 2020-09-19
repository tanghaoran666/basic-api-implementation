package com.thoughtworks.rslist.service;


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
import java.util.Optional;

@Service
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
}
