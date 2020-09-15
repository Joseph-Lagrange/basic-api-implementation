package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class RsController {

    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEventList = new ArrayList<>();
        rsEventList.add(new RsEvent("FirstEvent", "Economy"));
        rsEventList.add(new RsEvent("SecondEvent", "Politics"));
        rsEventList.add(new RsEvent("ThirdEvent", "Cultural"));
        return rsEventList;
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start,
                                           @RequestParam(required = false) Integer end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            return rsList;
        }
        return rsList.subList(start - 1, end);
    }

    @PostMapping("/rs/add")
    public void addEvent(@RequestBody String rsEvent) throws JsonProcessingException {
        RsEvent event = json2RsEvent(rsEvent);
        rsList.add(event);
    }

    @PostMapping("/rs/modify")
    public void modifyEvent(@RequestParam int index, @RequestBody String rsEvent) throws JsonProcessingException {
        RsEvent event = json2RsEvent(rsEvent);
        RsEvent modifyEvent = getEventByIndex(index);
        if (Objects.nonNull(event.getEventName())) {
            modifyEvent.setEventName(event.getEventName());
        }
        if (Objects.nonNull(event.getKeyWord())) {
            modifyEvent.setKeyWord(event.getKeyWord());
        }
    }

    @DeleteMapping("/rs/delete")
    public void deleteEvent(@RequestParam int index) {
        rsList.remove(index - 1);
    }

    private RsEvent json2RsEvent(String rsEvent) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(rsEvent, RsEvent.class);
    }

    private RsEvent getEventByIndex(int index) {
        return rsList.get(index - 1);
    }
}
