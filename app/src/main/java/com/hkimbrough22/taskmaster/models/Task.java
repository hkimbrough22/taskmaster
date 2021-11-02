package com.hkimbrough22.taskmaster.models;

public class Task {
    protected String title;
    protected String body;
    protected String state;
//    protected Date addedOn;

    public Long id;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
//        this.addedOn = addedOn;
        if(state == "new" || state == "assigned" || state == "in progress" || state == "complete"){
        this.state = state;
        } else throw new IllegalArgumentException("The state of your task must be \"new\", \"assigned\", \"in progress\", or \"complete\"!");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

//    public Date getAddedOn() {
//        return addedOn;
//    }
//
//    public void setAddedOn(Date addedOn) {
//        this.addedOn = addedOn;
//    }

    @Override
    public String toString() {
        return  "title= " + title +
                "\nbody= " + body +
                "\nstate= " + state;
    }
}
