package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.TradePO;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<TradePO, Integer> {

}
