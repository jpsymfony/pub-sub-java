package question1;

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * The interface Mediator i.
 */
public interface MediatorI extends Iterable<Topic>, Serializable
{
    /**
     * Ajout d'un souscripteur sur ce theme de publication.
     *
     * @param subscriber le souscripteur
     * @return this mediator i
     * @throws Exception the exception
     */
    public MediatorI add(SubscriberI subscriber) throws Exception;

    //public Mediator addAll(List<SubscriberI> list) throws Exception;

    /**
     * Retrait d'un souscripteur.
     *
     * @param subscriber le souscripteur a retirer
     * @return this mediator i
     * @throws Exception the exception
     */
    public MediatorI remove(SubscriberI subscriber) throws Exception;

    /**
     * Publication sur ce theme.
     * Les souscripteurs sont tous notifies dans un ordre non defini.
     *
     * @param topic le theme de publication
     * @param arg   un parametre arguments
     * @throws Exception the exception
     */
    public void sendBroadcast(Topic topic, Object arg) throws Exception;

    /**
     * Publication sur ce theme.
     * Les souscripteurs sont notifies selon leur priorite.
     * Un souscripteur peut interrompre la notification, c'est la methode aborted
     * de chaque souscripteur qui est appelee
     *
     * @param topic le theme de publication
     * @param arg   un parametre arguments
     * @throws Exception the exception
     */
    public void sendOrderedBroadcast(Topic topic, Object arg) throws Exception;

    /**
     * Parcours du mediateur.
     *
     * @return un iterateur sur les themes presents
     */
    public Iterator<Topic> iterator();

    /**
     * Obtention de tous les souscripteurs a un theme.
     *
     * @param topic le theme de publication
     * @return la liste souscripteurs a ce theme, null si ce theme n’existe pas
     */
    public List<? extends SubscriberI> getSubscribers(Topic topic);

    /**
     * Obtention des souscripteurs injoignables.
     * Ces souscripteurs ont leve une exception
     *
     * @return la liste souscripteurs injoignables
     */
    public List<SubscriberI> getDeadSubscribers();
}
