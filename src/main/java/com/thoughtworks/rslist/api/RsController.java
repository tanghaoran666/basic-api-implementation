package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.hibernate.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import javax.xml.soap.SAAJResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController

public class RsController {

  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> rsEvents = new ArrayList<>();
    rsEvents.add(new RsEvent("第一条事件","无参数"));
    rsEvents.add(new RsEvent("第二条事件","无参数"));
    rsEvents.add(new RsEvent("第三条事件","无参数"));
    return rsEvents;
  }



  @GetMapping("/rs/{index}")
  public RsEvent getOneList(@PathVariable int index){

    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getBetweenList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return rsList;
    }
    return rsList.subList(start-1 , end);
  }

  @PostMapping("/rs/event")
  public void postList(@RequestBody RsEvent rsEvent){

    rsList.add(rsEvent);
  }

//  @PatchMapping("/rs/{index}")
//  public void patchList(@PathVariable int index,@RequestParam(required = false) String eventName,@RequestParam(required = false) String keyWord){
//    RsEvent patchRsEvent = rsList.get(index-1);
//    if(eventName != null) patchRsEvent.setEventName(eventName);
//    if(keyWord != null) patchRsEvent.setKeyWord(keyWord);
//    rsList.set(index-1,patchRsEvent);
//
//  }

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
