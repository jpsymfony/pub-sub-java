package question3;

import question1.*;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

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

    private JTextField notification = new JTextField("une notification", 24);
    private JButton send = new JButton("send");
    private JCheckBox orderedCheck = new JCheckBox("", false);
    private JCheckBox abortedCheck = new JCheckBox("", false);

    private JTextField topic = new JTextField("PlanifCnam2015", 10);

    private JPanel topicsPanel;
    private ButtonGroup topicsGroup = new ButtonGroup();
    private JButton loadTopic = new JButton("load topic");

    private JTextField subscriber = new JTextField("s8", 4);
    private JTextField priority = new JTextField("35", 2);
    private JButton add = new JButton("add");
    private JButton list = new JButton("list");


    private Mediator mediator;
    private Topic theTopic;  // le theme en cours
    private Map<String, Topic> topics;

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
        theTopic = null;
        setTitle("IHM de \"test\"");

        Container container = this.getContentPane();
        JPanel panelTools = new JPanel();
        panelTools.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelTools.add(new JLabel("enter a topic :"));
        panelTools.add(topic);
        panelTools.add(loadTopic);

        JPanel panelAdd = new JPanel();
        panelAdd.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelAdd.add(new JLabel("add a subscriber, name:"));
        panelAdd.add(subscriber);
        panelAdd.add(new JLabel(" priority:"));
        panelAdd.add(priority);
        panelAdd.add(new JLabel(" aborted:"));
        panelAdd.add(abortedCheck);
        panelAdd.add(add);
        panelAdd.add(list);

        JPanel panelSend = new JPanel();
        panelSend.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelSend.add(new JLabel("sendBroadcast:"));
        panelSend.add(notification);
        panelSend.add(new JLabel(" ordered:"));
        panelSend.add(orderedCheck);
        panelSend.add(send);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(panelTools);
        panel.add(panelAdd);
        panel.add(panelSend);

        this.topicsPanel = new JPanel();
        topicsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topicsPanel.add(new JLabel("Topics:"));

        container.setLayout(new BorderLayout());
        container.add(console, BorderLayout.NORTH);
        container.add(topicsPanel, BorderLayout.CENTER);
        container.add(panel, BorderLayout.SOUTH);

        topics = new HashMap<String, Topic>();

        this.loadTopic.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    String className = topic.getText();
                    if (topics.get(className) == null) {
                        Class<?> cl = Class.forName("question3." + className);
                        theTopic = (Topic) cl.newInstance();
                        topics.put(className, theTopic);

                        console.setText(theTopic.getName() + " class loaded\n");
                        JCheckBox check = new JCheckBox(className, true);
                        topicsGroup.add(check); // ajout au groupe
                        check.setSelected(true);
                        topicsPanel.add(check); // ajout au JPanel

                        check.addItemListener(new ItemListener()
                        {
                            public void itemStateChanged(ItemEvent e)
                            {
                                if (e.getStateChange() == ItemEvent.SELECTED) {
                                    JCheckBox check = (JCheckBox) e.getItem();
                                    theTopic = topics.get(check.getText());
                                    console.setText("topic: " + check.getText());
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    console.append(stringStackTrace(e) + "\n");
                }
                IHM.this.pack();
            }
        });


        this.list.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                List<SubscriberI> list = mediator.getSubscribers(theTopic);
                if (list != null)
                    console.setText("topic: " + theTopic + ": " + list.toString() + "\n");
                else
                    console.setText("topic: " + theTopic + ": aucun souscripteur\n");
            }
        });

        this.add.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    String subName = subscriber.getText();
                    int priorityValue = Integer.parseInt(priority.getText());
                    // remplace l'ancien abonne au meme nom ...
                    SubTest st = new SubTest(subName, theTopic, priorityValue);
                    if (abortedCheck.isSelected()) st.setAborted();
                    List<SubscriberI> list = mediator.getSubscribers(theTopic);
                    if (list != null && list.contains(st)) {
                        mediator.remove(st);
                    }
                    mediator.add(st);
                    console.setText("topic: " + theTopic + ": " + mediator.getSubscribers(theTopic).toString());
                } catch (Exception e) {
                    console.append(stringStackTrace(e) + "\n");
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

                    if (orderedCheck.isSelected()) {
                        console.append("sendOrderedBroadcast\n");
                        mediator.sendOrderedBroadcast(theTopic, msg);
                    } else
                        mediator.sendBroadcast(theTopic, msg);
                } catch (Exception e) {
                    console.append(stringStackTrace(e) + "\n");
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

    // extrait de http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
    private static String readFile(String fileName) throws Exception
    {
        return new Scanner(new File(fileName)).useDelimiter("\\A").next();
    }

    // http://stackoverflow.com/questions/7242596/e-printstacktrace-in-string
    private static String stringStackTrace(Exception e)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);
        e.printStackTrace(pw);
        return new String(baos.toByteArray());
    }

}