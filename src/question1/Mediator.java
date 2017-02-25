package question1;

import java.util.*;
import java.io.Serializable;

/**
 * The type Mediator.
 */
public class Mediator implements MediatorI, Serializable
{
    /**
     * La table des souscripteurs par theme
     */
    private Map<Topic, List<SubscriberI>> map;

    /**
     * La table des souscripteurs installes, et leur Topic
     */
    private Map<SubscriberI, Topic> subscribers;

    /**
     * La liste des souscripteurs injoignables
     */
    private List<SubscriberI> deadSubscribers;

    /**
     * La relation d'ordre.
     */
    private static class ComparatorSubscriber implements Comparator<SubscriberI>, Serializable
    {
        public int compare(SubscriberI s1, SubscriberI s2)
        {
            if (s1.getPriority() < s2.getPriority()) {
                return 1;
            }
            if (s1.getPriority() > s2.getPriority()) {
                return -1;
            }
            return 0;
        }
    }

    private Comparator<SubscriberI> comparator = new ComparatorSubscriber();

    /**
     * Instantiates a new Mediator.
     */
    public Mediator()
    {
        this.map = new HashMap<>();
        this.subscribers = new HashMap<>();
        this.deadSubscribers = new ArrayList<>();
    }

    /**
     * @param subscriber le souscripteur
     * @return this
     * @throws Exception
     */
    public Mediator add(SubscriberI subscriber) throws Exception
    {
        if (null != subscribers.get(subscriber)) {
            throw new Exception("deja abonne");
        }

        Topic topic = subscriber.getTopic();
        subscribers.put(subscriber, topic);

        // On récupère la liste des subscribers a ce topic
        List<SubscriberI> list = map.get(topic);

        // si ce theme est absent de la liste
        if (null == list) {
            list = new ArrayList<>();
            map.put(topic, list);
        }
        list.add(subscriber);

        return this;
    }

    /**
     * @param subscriber le souscripteur a retirer
     * @return this
     * @throws Exception
     */
    public Mediator remove(SubscriberI subscriber) throws Exception
    {
        if (null == subscribers.get(subscriber)) {
            throw new Exception("abonne absent");
        }

        // On récupère la liste des subscribers a ce topic
        List<SubscriberI> list = map.get(subscriber.getTopic());
        // si la liste existe et que le subscriver y est présent
        if (null != list && list.contains(subscriber)) {
            // on retire le subscriber de la liste
            list.remove(subscriber);
            // on retire le subscriber de la hashMap
            subscribers.remove(subscriber);
        }
        return this;
    }

    /**
     * Obtention des souscripteurs a un theme.
     *
     * @param topic le theme souscrit
     * @return la liste souscripteurs a ce theme, null si ce theme n'existe pas
     */
    public List<SubscriberI> getSubscribers(Topic topic)
    {
        return map.get(topic);
    }


    /**
     * Obtention des souscripteurs injoignables a un theme.
     *
     * @return la liste souscripteurs a ce theme, null si ce theme n'existe pas
     */
    public List<SubscriberI> getDeadSubscribers()
    {
        return deadSubscribers;
    }

    /**
     * Publication sur ce theme.
     *
     * @param topic le theme de publication
     * @param arg   un parametre arguments
     * @throws Exception si ce theme n'existe pas et il n'y a aucun subscriber pour ce theme
     */
    public void sendBroadcast(Topic topic, Object arg) throws Exception
    {
        // On récupère la liste des subscribers a ce topic
        List<SubscriberI> list = map.get(topic);
        if (null == list) {
            throw new Exception("aucun subscriber sur ce theme");
        }

        Iterator<SubscriberI> it = list.iterator();
        while (it.hasNext()) {
            SubscriberI current = it.next();
            try {
                current.update(arg);
            } catch (Exception e) {
                it.remove();
                subscribers.remove(current);
                deadSubscribers.add(current);
            }
        }
    }

    /**
     * Publication sur ce theme.
     * Les souscripteurs sont notifies selon leur priorite.
     *
     * @param topic le theme de publication
     * @param arg   un parametre arguments
     * @throws Exception si ce theme n'existe pas
     */
    public void sendOrderedBroadcast(Topic topic, Object arg) throws Exception
    {
        // On récupère la liste des subscribers a ce topic
        List<SubscriberI> list = map.get(topic);
        if (null == list) {
            throw new Exception("aucun subscriber sur ce theme");
        }

        List<SubscriberI> orderedList = new ArrayList<>(list);
        Collections.sort(orderedList, comparator);

        for (SubscriberI current : orderedList) {
            try {
                current.update(arg);
                if (current.aborted()) {
                    return;
                }
            } catch (Exception e) {
                list.remove(current);
                subscribers.remove(current);
                deadSubscribers.add(current);
            }
        }
    }

    /**
     * Attention au remove ...
     */
    public Iterator<Topic> iterator()
    {
        return new Iterator<Topic>()
        {
            Iterator<Topic> it = map.keySet().iterator();

            public boolean hasNext()
            {
                return it.hasNext();
            }

            public Topic next()
            {
                return it.next();
            }

            public void remove()
            {
                throw new RuntimeException();
            }
        };
    }
}
