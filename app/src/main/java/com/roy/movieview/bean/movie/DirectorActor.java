package com.roy.movieview.bean.movie;

/**
 * Created by 1vPy(Roy) on 2017/4/12.
 */

public class DirectorActor {

    private Avatars avatars;

    private String name;

    private String id;

    private int role;

    public Avatars getAvatars() {
        return avatars;
    }

    public void setAvatars(Avatars avatars) {
        this.avatars = avatars;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
