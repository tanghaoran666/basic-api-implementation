package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RsEvent {
    private String eventName;
    private String keyWord;
    private User user;

//    @JsonIgnore
    public User getUser() {
        return user;
    }
//    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public RsEvent(String eventName, String value, User user) {
        this.eventName = eventName;
        this.keyWord = value;
        this.user = user;
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
