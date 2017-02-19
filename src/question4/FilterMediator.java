package question4;

import question1.SubscriberI;
import question1.*;

import java.util.*;

/**
 * The type Filter mediator.
 */
public abstract class FilterMediator implements MediatorI
{
    private MediatorI mediator;

    /**
     * Instantiates a new Filter mediator.
     *
     * @param mediator the mediator
     */
    public FilterMediator(MediatorI mediator)
    {
        this.mediator = mediator;
    }

    public MediatorI add(SubscriberI subscriber) throws Exception
    {
        return mediator.add(subscriber);
    }

    public MediatorI remove(SubscriberI subscriber) throws Exception
    {
        return mediator.remove(subscriber);
    }

    public void sendBroadcast(Topic topic, Object arg) throws Exception
    {
        mediator.sendBroadcast(topic, arg);
    }

    public void sendOrderedBroadcast(Topic topic, Object arg) throws Exception
    {
        mediator.sendOrderedBroadcast(topic, arg);
    }

    public Iterator<Topic> iterator()
    {
        return mediator.iterator();
    }

    public List<? extends SubscriberI> getSubscribers(Topic topic)
    {
        return mediator.getSubscribers(topic);
    }

    public List<SubscriberI> getDeadSubscribers()
    {
        return mediator.getDeadSubscribers();
    }

}