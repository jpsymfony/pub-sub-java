package question4;

import java.util.*;

import question1.*;

/**
 * The type Xml generator.
 */
public class XMLGenerator extends FilterMediator
{
    /**
     * Instantiates a new Xml generator.
     *
     * @param m the m
     */
    public XMLGenerator(MediatorI m)
    {
        super(m);
    }

    public MediatorI add(SubscriberI subscriber) throws Exception
    {
        System.out.println("<add>" + subscriber + "</add>\n");
        return super.add(subscriber);
    }

    public MediatorI remove(SubscriberI subscriber) throws Exception
    {
        System.out.println("<remove>" + subscriber + "+</remove>\n");
        return super.remove(subscriber);
    }

    public void sendBroadcast(Topic topic, Object arg) throws Exception
    {
        System.out.println("<sendBroadcast>" + topic + "\n" +
                "\t<arg>" + arg + "</arg>\n" +
                "</sendBroadcast>\n");
        super.sendBroadcast(topic, arg);
    }

    public void sendOrderedBroadcast(Topic topic, Object arg) throws Exception
    {
        System.out.println("<sendOrderedBroadcast>" + topic + "\n" +
                "\t<arg>" + arg + "</arg>\n" +
                "</sendOrderedBroadcast>\n");
        super.sendOrderedBroadcast(topic, arg);
    }

    public Iterator<Topic> iterator()
    {
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