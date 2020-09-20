package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Validated
public class RsController {

    final RsService rsService;

    final UserService userService;

    public RsController(RsService rsService, UserService userService) {
        this.rsService = rsService;
        this.userService = userService;
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEvent(@PathVariable int index) {
        List<RsEvent> rsEvents = rsService.findAll();
        if (index <= 0 || index > rsEvents.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return ResponseEntity.ok(rsEvents.get(index - 1));
    }

    @GetMapping("/rs/events")
    public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,
                                            @RequestParam(required = false) Integer end) {
        List<RsEvent> rsEvents = rsService.findAll();
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return ResponseEntity.ok(rsEvents);
        }
        if (start <= 0 || start > rsEvents.size() ||
                end <= 0 || end > rsEvents.size()) {
            throw new RsEventNotValidException("invalid request param");
        }
        return ResponseEntity.ok(rsEvents.subList(start - 1, end));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addEvent(@RequestBody @Valid RsEvent rsEvent) {
        Optional<UserPO> optional = userService.findById(rsEvent.getUserId());
        if (!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        rsService.addEvent(rsEvent, optional);
        return ResponseEntity.created(null).build();
    }

    @PatchMapping("/rs/{rsEventId}")
    public ResponseEntity updateEvent(@PathVariable int rsEventId, @RequestBody RsEvent rsEvent) {
        Optional<RsEventPO> optional = rsService.findById(rsEventId);
        if (!optional.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        RsEventPO rsEventPO = optional.get();
        if (rsEventPO.getUserPO().getId() == rsEvent.getUserId()) {
            if (Objects.nonNull(rsEvent.getKeyWord())) {
                rsEventPO.setKeyWord(rsEvent.getKeyWord());
            }
            if (Objects.nonNull(rsEvent.getEventName())) {
                rsEventPO.setEventName(rsEvent.getEventName());
            }
            rsService.save(rsEventPO);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/rs/{rsEventId}")
    public ResponseEntity deleteEvent(@PathVariable int rsEventId) {
        return rsService.deleteById(rsEventId);
    }

    @PostMapping("/rs/vote/{rsEventId}")
    public ResponseEntity voteEvent(@PathVariable int rsEventId, @RequestBody Vote vote) {
        return rsService.vote(vote, rsEventId);
    }

    @PostMapping("/rs/buy")
    public ResponseEntity buyEvent(@RequestParam int amount, @RequestParam int rank,
                                   @RequestBody RsEvent rsEvent) {
        return rsService.buy(amount, rank, rsEvent);
    }

}
