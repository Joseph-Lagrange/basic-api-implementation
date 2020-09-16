package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class RsController {

    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEventList = new ArrayList<>();
        User user = new User("Mike", "male", 20, "a@thoughtworks.com", "13386688553");
        rsEventList.add(new RsEvent("FirstEvent", "Economy", user));
        rsEventList.add(new RsEvent("SecondEvent", "Politics", user));
        rsEventList.add(new RsEvent("ThirdEvent", "Cultural", user));
        return rsEventList;
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity getOneRsEvent(@PathVariable int index) {
        if (index <= 0 || index > rsList.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @GetMapping("/rs/list")
    public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,
                                            @RequestParam(required = false) Integer end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return ResponseEntity.ok(rsList);
        }
        if (start <=  0 || start > rsList.size() ||
                end <=  0 || end > rsList.size()) {
            throw new RsEventNotValidException("invalid request param");
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        rsList.add(rsEvent);
        return ResponseEntity.created(null)
                .header("index", String.valueOf(rsList.size() - 1))
                .build();
    }

    @PatchMapping("/rs/{index}")
    public ResponseEntity modifyEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
        RsEvent modifyEvent = getEventByIndex(index);
        if (Objects.nonNull(modifyEvent)) {
            if (Objects.nonNull(rsEvent.getEventName())) {
                modifyEvent.setEventName(rsEvent.getEventName());
            }
            if (Objects.nonNull(rsEvent.getKeyWord())) {
                modifyEvent.setKeyWord(rsEvent.getKeyWord());
            }
        }
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping("/rs/{index}")
    public ResponseEntity deleteEvent(@PathVariable int index) {
        if (index <= 0 || index > rsList.size()) {
            throw new RsEventNotValidException("invalid index");
        }
        rsList.remove(index - 1);
        return ResponseEntity.created(null).build();
    }

    private RsEvent json2RsEvent(String rsEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(rsEvent, RsEvent.class);
    }

    private RsEvent getEventByIndex(int index) {
        return rsList.get(index - 1);
    }
}
