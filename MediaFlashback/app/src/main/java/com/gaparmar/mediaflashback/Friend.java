package com.gaparmar.mediaflashback;

/**
 * Created by aapte on 3/10/2018.
 */

public class Friend
{
    private String name;
    private String id;
    private String proxy;

    public Friend(String name, String id, String proxy)
    {
        this.name = name;
        this.id = id;
        this.proxy = proxy;
    }
    public String getName()
    {
        return name;
    }
    public String getId() { return id; }
    public String getProxy()
    {
        return proxy;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public void setProxy(String proxy)
    {
        this.proxy = proxy;
    }
}
