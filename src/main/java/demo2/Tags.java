package demo2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tags
{
    //@JsonProperty("Value")
    private String Value;

    //@JsonProperty("Key")
    private String Key;

    public String getValue ()
    {
        return Value;
    }

    @JsonProperty("Value")
    public Tags setValue (String Value)
    {
        this.Value = Value;return this;
    }

    public String getKey ()
    {
        return Key;
    }

    @JsonProperty("Key")
    public Tags setKey (String Key)
    {
        this.Key = Key;return this;
    }

/*    @Override
    public String toString()
    {
        return "ClassPojo [Value = "+Value+", Key = "+Key+"]";
    }*/
}
