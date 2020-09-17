package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.thoughtworks.rslist.api.UserController.users;

@RestController
@Validated
public class RsController {

    @Autowired
    RsEventRepository rsEventRepository;

    @Autowired
    UserRepository userRepository;

    private List<RsEvent> rsList = initRsEventList();

    private List<RsEvent> initRsEventList() {
        List<RsEvent> rsEventList = Lists.newArrayList();
        User user = new User("Mike", "male", 20, "a@thoughtworks.com", "13386688553");
        rsEventList.add(new RsEvent("FirstEvent", "Economy", 1));
        rsEventList.add(new RsEvent("SecondEvent", "Politics", 2));
        rsEventList.add(new RsEvent("ThirdEvent", "Cultural", 3));
        users.add(user);
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
        Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
        if (!userPO.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        RsEventPO rsEventPO = RsEventPO.builder().keyWord(rsEvent.getKeyWord()).eventName(rsEvent.getEventName())
                .userPO(userPO.get()).build();
        rsEventRepository.save(rsEventPO);
        return ResponseEntity.created(null).build();
    }

    private boolean isUserNameExist(String name) {
        return users.stream().anyMatch(v -> v.getUserName().equals(name));
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
