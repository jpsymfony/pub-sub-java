import question1.*;

/**
 * The type Main.
 */
public class Main
{
    /**
     * The type Sub.
     */
    public static class Sub extends Subscriber
    {
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
         * @param arg les arguments
         * @throws Exception
         */
        public void update(Object arg) throws Exception
        {
            System.out.println("update: " + this + " arg: " + arg);
        }

        public String toString()
        {
            return name;
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        Mediator mediator = new Mediator();
        Topic meteo, sensor;
        Sub s1, s2;
        meteo = new Meteo();
        sensor = new Sensor();

        s1 = new Sub("s1", meteo);
        s2 = new Sub("s2", sensor);

        try {
            mediator.add(s1);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        try {
            mediator.sendBroadcast(meteo, "sun");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            mediator.sendBroadcast(meteo, "rain");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            mediator.sendBroadcast(sensor, "25°C");

        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            mediator.add(s2);
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            mediator.sendBroadcast(meteo, "sun");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            mediator.sendBroadcast(sensor, "27°C");
        } catch(Exception e) {
            System.out.println(e.toString());
        }

        try {
            mediator.sendBroadcast(meteo, "sun");
        } catch(Exception e) {
            System.out.println(e.toString());
        }
    }
}
