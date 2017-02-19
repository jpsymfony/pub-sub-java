package question2;

import question1.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.List;
import java.util.Scanner;
import java.io.*;

/**
 * The type Ihm.
 */
public class IHM extends JFrame
{
    private JTextArea console = new JTextArea("", 7, 60);

    private JTextField notification = new JTextField("notification !", 24);
    private JButton send = new JButton("send");
    private JCheckBox orderedCheck = new JCheckBox("", true);
    private JCheckBox abortedCheck = new JCheckBox("", true);

    private ButtonGroup topics = new ButtonGroup();
    private JCheckBox planifCheck = new JCheckBox("", true);
    private JCheckBox meteoCheck = new JCheckBox("", false);

    private JTextField subscriber = new JTextField("s8", 4);
    private JTextField priority = new JTextField("35", 2);
    private JButton add = new JButton("add");
    private JButton list = new JButton("list");

    private Mediator mediator;
    private Topic planif, meteo;


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
            if (isAborted()) abort = ",T";
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

    private class SubTest extends Sub
    {
        /**
         * Instantiates a new Sub test.
         *
         * @param name     the name
         * @param topic    the topic
         * @param priority the priority
         */
        public SubTest(String name, Topic topic, int priority)
        {
            super(name, topic, priority);
        }

        public void update(Object arg)
        {
            console.append(this.toString() + " update " + arg.toString() + "\n");
        }
    }

    /**
     * Instantiates a new Ihm.
     */
    public IHM()
    {
        mediator = new Mediator();
        planif = new PlanifCnam();
        try {
            mediator.add(new SubTest("s1", planif, 10));
            mediator.add(new SubTest("s2", planif, 20));
            mediator.add(new SubTest("s3", planif, 30));
            mediator.add(new SubTest("s4", planif, 40));
            mediator.add(new SubTest("s5", planif, 50));
        } catch (Exception e) {
            console.append(e.getMessage() + "\n");
        }
        meteo = new Meteo();
        try {
            mediator.add(new SubTest("s6", meteo, 10));
            mediator.add(new SubTest("s7", meteo, 20));
        } catch (Exception e) {
            console.append(e.getMessage() + "\n");
        }
        setTitle("IHM de \"test\"");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());
        container.add(console, BorderLayout.NORTH);

        JPanel panelAdd = new JPanel();
        panelAdd.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelAdd.add(new JLabel("add a subscriber, name:"));
        panelAdd.add(subscriber);
        panelAdd.add(new JLabel(" priority:"));
        panelAdd.add(priority);
        panelAdd.add(new JLabel(" aborted:"));
        panelAdd.add(abortedCheck);
        panelAdd.add(add);
        topics.add(planifCheck);
        topics.add(meteoCheck);
        panelAdd.add(new JLabel(" planif:"));
        panelAdd.add(planifCheck);
        panelAdd.add(new JLabel(" meteo:"));
        panelAdd.add(meteoCheck);
        panelAdd.add(list);

        JPanel panelSend = new JPanel();
        panelSend.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelSend.add(new JLabel("sendBroadcast:"));
        panelSend.add(notification);
        panelSend.add(new JLabel(" ordered:"));
        panelSend.add(orderedCheck);
        panelSend.add(send);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.add(panelAdd);
        panel.add(panelSend);
        container.add(panel, BorderLayout.SOUTH);

        console.setText(mediator.getSubscribers(planif).toString() + "\n");

        list.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                Topic topic = planif;
                if (meteoCheck.isSelected()) topic = meteo;
                console.setText(mediator.getSubscribers(topic).toString() + "\n");
            }
        });

        this.add.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    String subName = subscriber.getText();
                    int priorityValue = Integer.parseInt(priority.getText());
                    Topic topic = planif;
                    if (meteoCheck.isSelected()) topic = meteo;
                    // remplace l'ancien abonne au meme nom...
                    SubTest st = new SubTest(subName, topic, priorityValue);
                    if (abortedCheck.isSelected()) st.setAborted();
                    if (mediator.getSubscribers(topic).contains(st)) {
                        mediator.remove(st);
                    }
                    mediator.add(st);
                    console.setText(mediator.getSubscribers(topic).toString());
                } catch (Exception e) {
                    console.append(e.getMessage() + "\n");
                }
                IHM.this.pack();
            }
        });

        this.send.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    console.setText("");
                    String msg = notification.getText();
                    Topic topic = planif;
                    if (meteoCheck.isSelected()) topic = meteo;
                    if (orderedCheck.isSelected()) {
                        console.append("sendOrderedBroadcast\n");
                        mediator.sendOrderedBroadcast(topic, msg);
                    } else
                        mediator.sendBroadcast(topic, msg);
                } catch (Exception e) {
                    console.append(e.getMessage() + "\n");
                }
                IHM.this.pack();
            }

        });

        this.pack();
        this.setVisible(true);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        new IHM();
    }

    // http://stackoverflow.com/questions/7242596/e-printstacktrace-in-string
    private static String stringStackTrace(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        e.printStackTrace(pw);
        return new String(baos.toByteArray());
    }

    // extrait de http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
    private static String readFile(String fileName) throws Exception
    {
        return new Scanner(new File(fileName)).useDelimiter("\\A").next();
    }

}