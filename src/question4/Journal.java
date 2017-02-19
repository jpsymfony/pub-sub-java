package question4;

import java.util.*;

import question1.*;

/**
 * The type Journal.
 */
public class Journal extends FilterMediator
{
    /**
     * Instantiates a new Journal.
     *
     * @param m the m
     */
    public Journal(MediatorI m)
    {
        super(m);
    }

    public MediatorI add(SubscriberI subscriber) throws Exception
    {
        System.out.println("add(" + subscriber + ")");
        return super.add(subscriber);
    }

    public MediatorI remove(SubscriberI subscriber) throws Exception
    {
        System.out.println("remove(" + subscriber + ")");
        return super.remove(subscriber);
    }

    public void sendBroadcast(Topic topic, Object arg) throws Exception
    {
        System.out.println("sendBroadcast(" + topic + "," + arg + ")");
        super.sendBroadcast(topic, arg);
    }

    public void sendOrderedBroadcast(Topic topic, Object arg) throws Exception
    {
        System.out.println("sendOrderedBroadcast(" + topic + "," + arg + ")");
        super.sendOrderedBroadcast(topic, arg);
    }

    public Iterator<Topic> iterator()
    {
        System.out.println("iterator()");
        return super.iterator();
    }

    public List<? extends SubscriberI> getSubscribers(Topic topic)
    {
        return super.getSubscribers(topic);
    }

    public List<SubscriberI> getDeadSubscribers()
    {
        return super.getDeadSubscribers();
    }
}