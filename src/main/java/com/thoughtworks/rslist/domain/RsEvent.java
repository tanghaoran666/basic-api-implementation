package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

public class RsEvent {
    private String eventName;
    private String keyWord;
    @Valid
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
//    @JsonIgnore
//    @JsonProperty

    public RsEvent(String eventName, String value, int userId) {
        this.eventName = eventName;
        this.keyWord = value;
        this.userId = userId;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public RsEvent() {
    }
}
