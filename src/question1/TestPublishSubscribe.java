package question1;

import java.util.*;

/**
 * The type Test publish subscribe.
 */
public class TestPublishSubscribe extends junit.framework.TestCase
{
    private Mediator topics = new Mediator();

    /**
     * The type Sub.
     */
    public static class Sub extends Subscriber
    {
        private boolean notified; // pour les tests : verification
        private String name; // le nom du souscripteur

        /**
         * Instantiates a new Sub.
         *
         * @param name  the name
         * @param topic the topic
         */
        public Sub(String name, Topic topic)
        {
            super(topic);
            this.name = name;
        }

        /**
         * Instantiates a new Sub.
         *
         * @param name     the name
         * @param topic    the topic
         * @param priority the priority
         */
        public Sub(String name, Topic topic, int priority)
        {
            this(name, topic);
            setPriority(priority);
        }

        public void update(Object arg) throws Exception
        {
            notified = true;
            System.out.println("update: " + this + " arg: " + arg);
        }

        /**
         * Notified boolean.
         *
         * @return the boolean
         */
        public boolean notified()
        {  // test de la notification
            boolean temp = notified;
            notified = false;
            return temp;
        }

        public String toString()
        {
            String abort = ",F";
            if (isAborted()) {
                abort = ",T";
            }
            return name + "(" + getPriority() + abort + ")";
        }

        public boolean equals(Object obj)
        {
            Sub s = (Sub) obj;
            return name.equals(s.name);
        }

        public int hashCode()
        {
            return name.hashCode();
        }
    }

    private Mediator mediator;
    private Topic planif, meteo;
    private Sub s1, s2, s3, s4, s5, s6, s7;

    /**
     * Methode appelee avant chaque appel de methode de test.
     */
    protected void setUp()
    {
        this.mediator = topics;
        this.planif = new PlanifCnam();
        this.s1 = new Sub("s1", planif, 10); // priorite 10
        this.s2 = new Sub("s2", planif, 20); //  "    "  20
        this.s3 = new Sub("s3", planif, 30); //  "    "  30
        this.s4 = new Sub("s4", planif, 40); //  "    "  40
        this.s5 = new Sub("s5", planif, 50); //  "    "  50
        this.meteo = new Meteo();
        this.s6 = new Sub("s6", meteo);
        this.s7 = new Sub("s7", meteo);
    }

    /**
     * Test subscribers send broadcast.
     *
     * @throws Exception the exception
     */
    public void testSubscribersSendBroadcast() throws Exception
    {
        mediator.add(s1);
        boolean result = mediator.getSubscribers(planif).contains(s1);
        assertTrue("subscriber s1 absent de la liste ? ", result);
        mediator.add(s2).add(s3).add(s4).add(s5);
        int taille = mediator.getSubscribers(planif).size();
        assertEquals("taille de la liste pour planif ?", 5, taille);
        mediator.add(s6).add(s7);
        taille = mediator.getSubscribers(meteo).size();
        assertEquals("taille de la liste pour meteo ?", 2, taille);
        mediator.sendBroadcast(planif, "examen 10/02/2017");
        assertTrue("s1 non notifie ? ", s1.notified());
        assertTrue("s2 non notifie ? ", s2.notified());
        assertTrue("s3 non notifie ? ", s3.notified());
        assertTrue("s4 non notifie ? ", s4.notified());
        assertTrue("s5 non notifie ? ", s5.notified());
        assertFalse("s6 notifie ? ", s6.notified());
        assertFalse("s7 notifie ? ", s7.notified());
    }

    /**
     * Test subscribers send ordered broadcast.
     *
     * @throws Exception the exception
     */
    public void testSubscribersSendOrderedBroadcast() throws Exception
    {
        mediator.add(s1).add(s2).add(s3).add(s4).add(s5);
        s3.setAborted(); // apres avoir ete notifie s3 arrete la propagation

        mediator.sendOrderedBroadcast(planif, "examen 10/02/2017");
        assertFalse("s1 ou s2 notifie ? ", s1.notified() | s2.notified());
        assertTrue("s3 non notifie ? ", s3.notified());
        assertTrue("s4 non notifie ? ", s4.notified());
        assertTrue("s5 non notifie ? ", s5.notified());

        s4.setAborted();// arret par s4, seuls s4 et s5 doivent etre notifies
        mediator.sendOrderedBroadcast(planif, "examen 10/02/2017");
        assertTrue("s4 ou s5 non notifie ? ", s4.notified() && s5.notified());
        assertFalse("s3 notifie ? ", s3.notified());
    }

    /**
     * Test subscribers exceptions.
     *
     * @throws Exception the exception
     */
    public void testSubscribersExceptions() throws Exception
    {
        mediator.add(s1).add(s2).add(s3).add(s4).add(s5);
        try {  // exception un abonne est deja present
            mediator.add(s2);
            fail("exception attendue");
        } catch (Exception e) {
        }
        Topic topic = new AlerteAgenda();
        try {  // exception, pas d'abonne pour ce theme
            mediator.sendBroadcast(topic, "...");
            fail("exception attendue");
        } catch (Exception e) {
        }
        try {  // exception, pas d'abonne pour ce theme
            mediator.sendOrderedBroadcast(topic, "...");
            fail("exception attendue");
        } catch (Exception e) {
        }
        try {  // pas d'exception, retrait d'un abonne existant pour ce theme
            mediator.remove(s3);
        } catch (Exception e) {
            fail("exception inattendue ici ");
        }
        try {  // exception retrait d'un abonne absent
            mediator.remove(s3);
            fail("exception attendue");
        } catch (Exception e) {
        }
        // pas d'exception, null est retournee, si aucun abonne
        assertNull("null est attendu ...", mediator.getSubscribers(topic));
        assertNotNull("not null est attendu ...", mediator.getDeadSubscribers());
        assertEquals("0 est attendu ...", 0, mediator.getDeadSubscribers().size());
    }

    /**
     * The type Sub exception.
     */
    public static class SubException extends Sub
    {
        /**
         * Instantiates a new Sub exception.
         *
         * @param name     the name
         * @param topic    the topic
         * @param priority the priority
         */
        public SubException(String name, Topic topic, int priority)
        {
            super(name, topic, priority);
        }

        public void update(Object arg) throws Exception
        {
            throw new Exception();
        }
    }

    /**
     * Test dead subscribers.
     *
     * @throws Exception the exception
     */
    public void testDeadSubscribers() throws Exception
    {
        try { //toute instance de SubExc leve une exception, cf. methode update
            SubException subExc, subExc1;
            subExc = new SubException("se", planif, 36);
            subExc1 = new SubException("se1", planif, 16);
            mediator.add(s1).add(s2).add(subExc).add(s3).add(s4).add(subExc1).add(s5);             // 6
            int tailleAvant = mediator.getSubscribers(planif).size();
            assertEquals("taille de la liste pour planif ?", 7, tailleAvant);
            mediator.sendOrderedBroadcast(planif, " resultats dans la semaine");
            int tailleApres = mediator.getSubscribers(planif).size();
            assertEquals("taille de la liste pour planif ?", tailleAvant - 2, tailleApres);

            int taille = mediator.getDeadSubscribers().size();
            assertEquals("taille de la dead liste ?", 2, taille);
            boolean result = mediator.getDeadSubscribers().contains(subExc);
            assertTrue("subscriber subExc absent de la dead liste ? ", result);

            mediator.add(mediator.getDeadSubscribers().get(0));
            assertEquals("taille de la liste pour planif ?", tailleAvant - 1, tailleApres + 1);

        } catch (Exception e) {
            fail("exception inattendue !" + e.getMessage());
        }
    }

    /**
     * Test iterator.
     *
     * @throws Exception the exception
     */
    public void testIterator() throws Exception
    {
        mediator.add(s1).add(s6); // deux themes, deux abonnes
        Iterator<Topic> it = mediator.iterator();
        Topic t1 = it.next();
        assertTrue(" topic ?", t1.equals(planif) || t1.equals(meteo));
        Topic t2 = it.next();
        assertTrue(" topic ?", t2.equals(planif) || t2.equals(meteo));
        try {
            it = mediator.iterator();
            it.next();
            it.remove();
            fail(" une exception  est attendue !!");
        } catch (Exception e) {
            assertTrue(e instanceof RuntimeException);
        }
        int count = 0;
        for (Topic t : mediator) {
            count = count + mediator.getSubscribers(t).size();
        }
        assertEquals(" nombre d'abonnes ?", 2, count);
    }
}

