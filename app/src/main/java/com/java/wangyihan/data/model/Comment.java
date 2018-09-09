package com.java.wangyihan.data.model;

public class Comment {
    private String username = "";
    private String comment = "";

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment(String user, String comm)
    {
        username = user;
        comment = comm;
    }
}
