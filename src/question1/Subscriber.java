package question1;

/**
 * The type Subscriber.
 */
public abstract class Subscriber implements SubscriberI
{
    private Topic topic;     // theme souscrit
    private int priority;    // priorite du souscripteur
    private boolean aborted; // arret de la notification

    /**
     * Instantiates a new Subscriber.
     *
     * @param topic the topic
     */
    public Subscriber(Topic topic)
    {
        this.topic = topic;
    }

    /**
     * Notification sur ce theme.
     *
     * @param arg les arguments
     * @throws Exception si le souscripteur a leve une exception ou est injoignable …
     */
    public abstract void update(Object arg) throws Exception;

    /**
     * Installation d'une priorite, nombre eleve : priorite forte.
     *
     * @param priority la priorite de cet abonne
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    /**
     * Obtention de la priorite.
     *
     * @return la priorite de cet abonne
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Obtention du topic de ce souscripteur.
     * Un seul theme de publication par abonne.
     *
     * @return le topic de ce souscripteur
     */
    public Topic getTopic()
    {
        return topic;
    }

    /**
     * Ce souscripteur arrete la propagation de la notification
     * seulement apres l'appel de la methode update.
     * Est utilise seulement lors d'un sendOrderedBroadcast.
     */
    public void setAborted()
    {
        aborted = true;
    }

    /**
     * Interrogation de l'arret  de la notification.
     * La valeur est remise a faux a chaque lecture.
     * Utilise seulement lors d'un sendOrderedBroadcast.
     *
     * @return true si arret demande
     */
    public boolean aborted()
    {
        boolean temp = aborted;
        aborted = false;
        return temp;
    }

    /**
     * Interrogation de l'arret de la notification.
     *
     * @return true si arret demande, faux autrement
     */
    public boolean isAborted()
    {
        return aborted;
    }
}
