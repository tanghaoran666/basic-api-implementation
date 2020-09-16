package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.soap.SAAJResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController

public class RsController {

  private List<RsEvent> rsList = initRsList();
  private User user = new User("thr","male",19,"a@b.com","18888888888");
  private List<RsEvent> initRsList() {
    List<RsEvent> rsEvents = new ArrayList<>();
    rsEvents.add(new RsEvent("第一条事件","无参数",user));
    rsEvents.add(new RsEvent("第二条事件","无参数",user));
    rsEvents.add(new RsEvent("第三条事件","无参数",user));
    return rsEvents;
  }



  @GetMapping("/rs/{index}")
  public ResponseEntity getOneList(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getBetweenList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start-1 , end));
  }

  @PostMapping("/rs/event")
  public ResponseEntity postList(@RequestBody RsEvent rsEvent){
    rsList.add(rsEvent);
    return ResponseEntity.created(null).build();
  }


  @PatchMapping("/rs/{index}")
  public void patchListViaBody(@PathVariable int index,@RequestBody RsEvent rsEvent){
    RsEvent patchRsEvent = rsList.get(index-1);
    if(rsEvent.getEventName() != null) patchRsEvent.setEventName(rsEvent.getEventName());
    if(rsEvent.getKeyWord() != null) patchRsEvent.setKeyWord(rsEvent.getKeyWord());
  }

  @DeleteMapping("/rs/{index}")
  public void deleteList(@PathVariable int index){
    rsList.remove(index-1);
  }

  @DeleteMapping("/rs/reStart")
  public void reStart(){
    rsList.clear();
    rsList = initRsList();
  }
}
