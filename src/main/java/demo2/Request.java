package demo2;

import java.util.ArrayList;
import java.util.List;

public class Request
{
    private Tags[] Tags;
    //private ArrayList<Tags> Tags;

    private String availability_zone;

    private String vpc_id;

    private String cidr;

    public Tags[] getTags ()
    {
        return Tags;
    }

    /*public ArrayList<Tags> getTags ()
    {
        return Tags;
    }*/

    public Request setTags (Tags[] Tags)
    {
        this.Tags = Tags; return this;
    }

    /*public Request setTags (ArrayList<Tags> Tags)
    {
        this.Tags = Tags; return this;
    }*/

    public String getAvailability_zone ()
    {
        return availability_zone;
    }

    public Request setAvailability_zone (String availability_zone)
    {
        this.availability_zone = availability_zone;return this;
    }

    public String getVpc_id ()
    {
        return vpc_id;
    }

    public Request setVpc_id (String vpc_id)
    {
        this.vpc_id = vpc_id;return this;
    }

    public String getCidr ()
    {
        return cidr;
    }

    public Request setCidr (String cidr)
    {
        this.cidr = cidr;return this;
    }
/*
    @Override
    public String toString()
    {
        return "ClassPojo [Tags = "+Tags+", availability_zone = "+availability_zone+", vpc_id = "+vpc_id+", cidr = "+cidr+"]";
    }*/
}
