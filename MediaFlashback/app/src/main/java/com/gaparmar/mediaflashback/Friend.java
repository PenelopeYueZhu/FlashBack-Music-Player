package com.gaparmar.mediaflashback;

/**
 * Created by aapte on 3/10/2018.
 */

public class Friend
{
    private String name;
    private String id;

    public Friend(String name, String id)
    {
        this.name = name;
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public String getId() { return id; }
    public void setName(String name, String id)
    {
        this.name = name;
        this.id = id;
    }
}
