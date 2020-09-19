package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.UserPo;
import com.thoughtworks.rslist.po.VotePo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VotePo,Integer> {
    @Override
    List<VotePo> findAll();

    @Query("select v from VotePo v where v.user.id = :userId and v.rsEvent.id = :rsEventId")
    List<VotePo> findAccordingToUserAndRsEvent(int userId, int rsEventId);
}
