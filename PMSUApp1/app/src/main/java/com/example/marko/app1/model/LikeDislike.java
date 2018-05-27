package com.example.marko.app1.model;

public class LikeDislike {

    //NAZIVI VARIJABLA MORAJU BITI IDENTICNI KAO NAZIVI U JSON-U KOJI VRACA SERVER
    private int id;
    private int entityId;
    private int authorId;
    private boolean post;
    private boolean like;


    public LikeDislike(int id, int entityId, int authorId, boolean like, boolean post) {
        this.id = id;
        this.entityId = entityId;
        this.authorId = authorId;
        this.like = like;
        this.post = post;

    }

    public LikeDislike() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public boolean isPost() {
        return post;
    }

    public void setPost(boolean post) {
        this.post = post;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    @Override
    public String toString() {
        return "LikeDislike{" +
                "id=" + id +
                ", entityId=" + entityId +
                ", authorId=" + authorId +
                ", post=" + post +
                ", like=" + like +
                '}';
    }
}
