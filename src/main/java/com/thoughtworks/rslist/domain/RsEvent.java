package com.thoughtworks.rslist.domain;

public class RsEvent {
    private String eventName;

    public RsEvent(String eventName, String value) {
        this.eventName = eventName;
        this.keyWord = value;
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

    private String keyWord;
}
