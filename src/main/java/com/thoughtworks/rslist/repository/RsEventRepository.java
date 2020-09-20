package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RsEventRepository extends CrudRepository<RsEventPO, Integer> {

    @Override
    List<RsEventPO> findAll();

    Optional<RsEventPO> findByRankNum(int rank);

    // void deleteAllByUserId(int userId);

}
