package question1;

import java.io.Serializable;

/**
 * The interface Subscriber i.
 */
public interface SubscriberI extends Serializable
{
    /**
     * Notification sur ce theme.
     *
     * @param arg les arguments
     * @throws Exception si le souscripteur a leve une exception ou est injoignable
     */
    public void update(Object arg) throws Exception;

    /**
     * Installation d'une priorite, nombre eleve : priorite forte.
     *
     * @param priority la priorite de cet abonne
     */
    public void setPriority(int priority);

    /**
     * Obtention de la priorite.
     *
     * @return la priorite de cet abonne
     */
    public int getPriority();

    /**
     * Obtention du topic de ce souscripteur.
     * Un seul theme de publication par abonne.
     *
     * @return le topic de ce souscripteur
     */
    public Topic getTopic();

    /**
     * Ce souscripteur arrete la propagation de la notification.
     * Utilise seulement lors d'un sendOrderedBroadcast.
     */
    public void setAborted();

    /**
     * Interrogation de l'arret demande.
     * La valeur est remise a faux a chaque lecture.
     * Utilise seulement lors d'un sendOrderedBroadcast.
     *
     * @return true si arret demande
     */
    public boolean aborted();

}
