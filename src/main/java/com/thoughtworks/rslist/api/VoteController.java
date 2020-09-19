package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.repository.VoteRepository;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    VoteService voteService;

    @GetMapping("/voteRecord")
    public ResponseEntity<List<Vote>> getVoteRecord(@RequestParam int userId, @RequestParam int rsEventId,
                                                    @RequestParam int pageIndex) {
        return voteService.findAllByUserIdAndRsEventId(userId, rsEventId, pageIndex);
    }

    @GetMapping("/voteRecords")
    public ResponseEntity<List<Vote>> getVoteRecordByTime(@RequestParam String startTimeString, @RequestParam String endTimeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = simpleDateFormat.parse(startTimeString);
            endTime = simpleDateFormat.parse(endTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return voteService.findAllByTime(startTime, endTime);
    }
}
