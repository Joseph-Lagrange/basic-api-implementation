package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public interface VoteRepository extends CrudRepository<VotePO, Integer> {

    @Override
    List<VotePO> findAll();

    @Query(value = "select v from VotePO v where v.user.id = :userId and v.rsEvent.id = :rsEventId")
    List<VotePO> findAllByUserIdAndRsEventId(int userId, int rsEventId, Pageable pageable);

    @Query(value = "select v from VotePO v where v.localDateTime between :startTime and :endTime")
    List<VotePO> findAllByTime(Date startTime, Date endTime);
    
}
