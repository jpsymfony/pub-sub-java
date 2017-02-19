package question1;

import java.io.Serializable;

/**
 * The type Topic.
 */
public abstract class Topic implements Serializable
{
    private String name;

    /**
     * Instantiates a new Topic.
     *
     * @param name the name
     */
    public Topic(String name)
    {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return name;
    }
}
