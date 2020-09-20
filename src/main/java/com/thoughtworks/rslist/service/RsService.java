package com.thoughtworks.rslist.service;

import com.google.common.collect.Lists;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.TradePO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.TradeRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RsService {

    final RsEventRepository rsEventRepository;

    final UserRepository userRepository;

    final VoteRepository voteRepository;

    final TradeRepository tradeRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository, TradeRepository tradeRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.tradeRepository = tradeRepository;
    }

    public ResponseEntity vote(Vote vote, int rsEventId) {
        Optional<RsEventPO> eventOptional = rsEventRepository.findById(rsEventId);
        Optional<UserPO> userOptional = userRepository.findById(vote.getUserId());
        if (!eventOptional.isPresent() || !userOptional.isPresent()
                || userOptional.get().getVoteNum() < vote.getVoteNum()) {
            throw new RsEventNotValidException("invalid vote num");
        }
        VotePO votePO = VotePO.builder().localDateTime(vote.getTime()).num(vote.getVoteNum())
                .rsEvent(eventOptional.get())
                .user(userOptional.get())
                .build();
        voteRepository.save(votePO);
        UserPO userPO = userOptional.get();
        userPO.setVoteNum(userPO.getVoteNum() - vote.getVoteNum());
        userRepository.save(userPO);
        RsEventPO rsEventPO = eventOptional.get();
        rsEventPO.setVoteNum(rsEventPO.getVoteNum() + vote.getVoteNum());
        rsEventRepository.save(rsEventPO);
        return ResponseEntity.ok().build();
    }

    public List<RsEvent> findAll() {
        return rsEventRepository.findAll().stream()
                .map(item -> RsEvent.builder().eventName(item.getEventName())
                        .keyWord(item.getKeyWord()).userId(item.getId())
                        .amount(item.getAmount()).voteNum(item.getVoteNum())
                        .rank(item.getRankNum()).build())
                .collect(Collectors.toList());
    }

    public List<RsEventPO> findAllPO() {
        return rsEventRepository.findAll().stream()
                .sorted(Comparator.comparing(RsEventPO::getRankNum))
                .collect(Collectors.toList());
    }

    public ResponseEntity deleteById(int rsEventId) {
        rsEventRepository.deleteById(rsEventId);
        return ResponseEntity.ok().build();
    }

    public void save(RsEventPO rsEventPO) {
        rsEventRepository.save(rsEventPO);
    }

    public Optional<RsEventPO> findById(int rsEventId) {
        return rsEventRepository.findById(rsEventId);
    }

    public void addEvent(RsEvent rsEvent, Optional<UserPO> optional) {
        boolean isFind = false;
        List<RsEventPO> saveReEventPOs = Lists.newArrayList();
        List<RsEventPO> rsEventPOs = findAllPO();
        int currRank = 0;
        for (RsEventPO event : rsEventPOs) {
            if (!isFind && event.getVoteNum() < rsEvent.getVoteNum()) {
                rsEvent.setRank(event.getRankNum());
                saveReEventPOs.add(event2PO(rsEvent, optional));
                isFind = true;
            }
            if (isFind) {
                event.setRankNum(event.getRankNum() + 1);
                saveReEventPOs.add(event);
            }
            currRank = event.getRankNum();
        }
        if (!isFind) {
            rsEvent.setRank(currRank + 1);
            saveReEventPOs.add(event2PO(rsEvent, optional));
        }
        rsEventRepository.saveAll(saveReEventPOs);
    }

    public Optional<RsEventPO> findByRankNum(int rank) {
        return rsEventRepository.findByRankNum(rank);
    }

    private RsEventPO event2PO(RsEvent rsEvent, Optional<UserPO> optional) {
        return RsEventPO.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName())
                .userPO(optional.get()).rankNum(rsEvent.getRank()).build();
    }

    public ResponseEntity buy(int amount, int rank, RsEvent rsEvent) {
        Optional<RsEventPO> eventOptional = rsEventRepository.findByRankNum(rank);
        Optional<UserPO> userOptional = userRepository.findById(rsEvent.getUserId());
        if (!eventOptional.isPresent() || !userOptional.isPresent()
                || eventOptional.get().getAmount() > amount) {
            // throw new RuntimeException();
            return ResponseEntity.badRequest().build();
        }
        RsEventPO rsEventPO = eventOptional.get();
        rsEventPO.setRankNum(rank);
        rsEventPO.setVoteNum(rsEvent.getVoteNum());
        rsEventPO.setEventName(rsEvent.getEventName());
        rsEventPO.setKeyWord(rsEvent.getKeyWord());
        rsEventPO.setUserPO(userOptional.get());
        rsEventPO.setAmount(amount);
        TradePO tradePO = TradePO.builder().amount(amount).rankNum(rank).rsEvent(rsEventPO)
                .user(userOptional.get()).build();
        rsEventRepository.save(rsEventPO);
        rsEventRepository.deleteById(eventOptional.get().getId());
        tradeRepository.save(tradePO);
        return ResponseEntity.ok().build();
    }
}
