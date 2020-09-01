package cz.cvut.fel.czm.publisher;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import cz.cvut.fel.czm.publisher.values.BiTimeObject;
import cz.cvut.fel.czm.publisher.values.CsEnValueString;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Main {

    private final static String EXCHANGE_NAME = "fel.notifications";
    private static MessageFactory messageFactory = new MessageFactory();
    private static ConnectionFactory factory = new ConnectionFactory();
    private static Map<String, String> ROUTING_KEYS = new HashMap<String, String>();


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        // (example) connection setup
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("testapp");
        factory.setPassword("guest");

        // (example) ROUTING KEYS
        ROUTING_KEYS.put("username", "notifications.user.zidekja2");
        ROUTING_KEYS.put("role", "notifications.role.B-13393-ZAMESTNANEC");
        ROUTING_KEYS.put("course",  "notifications.course.B191.B6B36ZPR");
        ROUTING_KEYS.put("teachers", "notifications.course_teachers.B191.B6B36ZPR");
        ROUTING_KEYS.put("students", "notifications.course_students.B191.B6B36ZPR");
        ROUTING_KEYS.put("parallelStudents", "notifications.course_students.B191.A0B01PAN.1005611773005");
        ROUTING_KEYS.put("parallelTeachers", "notifications.course_teachers.B191.A0B01PAN.1005611773005");

        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            // declare exchange as the publisher side
            channel.exchangeDeclare(EXCHANGE_NAME, "topic", true, false, Collections.emptyMap());

            // set message envelop
            Calendar c = Calendar.getInstance();
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .userId("testapp")
                    .contentType("application\\JSON")
                    .timestamp(c.getTime())
                    .build();

            // publish messages to the exchange
            openSubmitGradeScenario(channel, properties);

             //multipleNotificationScenario(channel, properties);

             //createUpdateEventMessageScenario(channel, properties);
           }

    }

    private static void publishMessageToChannel(String message, Channel channel, String routingKey, AMQP.BasicProperties properties){
        try {
            channel.basicPublish(EXCHANGE_NAME, routingKey, properties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        } catch (IOException e) {
            System.out.println("Sending failed");
            System.out.println(e.getStackTrace());
        }
    }

    /*
            OPEN EXAM => STUDENTS
            SUBMIT EXAM => TEACHERS
            GRADED EXAM => STUDENTS
     */
    private static void openSubmitGradeScenario(Channel channel, AMQP.BasicProperties properties){
        String aid = getRandomAID();

        publishMessageToChannel(getDeadlineMessage(ActionTypes.OPEN, aid, "Zkouska TASK TASK", "Zkouska AAA byla otevrena", MessageTypes.TASK), channel, ROUTING_KEYS.get("students"), properties);

        publishMessageToChannel(getDeadlineMessage(ActionTypes.SUBMITTED, aid, "Zkouska bb", "Zkouska AAA byla odevzdana", MessageTypes.EXAM), channel, ROUTING_KEYS.get("teachers"), properties);

        publishMessageToChannel(getDeadlineMessage(ActionTypes.GRADED, aid, "Zkouska bbb", "Zkouska AAA byla ohodnocena", MessageTypes.EXAM), channel, ROUTING_KEYS.get("students"), properties);
    }

    private static void multipleNotificationScenario(Channel channel, AMQP.BasicProperties properties){
        publishMessageToChannel(getNotificationMessage(null, getRandomAID(), "Notifikace A", "Notifikace A default title", MessageTypes.NOTIFICATION), channel, ROUTING_KEYS.get("course"), properties);

        publishMessageToChannel(getNotificationMessage(null, getRandomAID(), "Notifikace B", "Notifikace B default title", MessageTypes.NOTIFICATION), channel, ROUTING_KEYS.get("course"), properties);
    }

    private static void createUpdateEventMessageScenario(Channel channel, AMQP.BasicProperties properties){
        String aid = getRandomAID();

        publishMessageToChannel(getEventMessage(ActionTypes.OPEN, aid, "Genericka udalost A", "Generic event CCC", MessageTypes.EVENT), channel, ROUTING_KEYS.get("course"), properties);

        publishMessageToChannel(getEventMessage(ActionTypes.SUBMITTED, aid, "Genericka udalost A po aktualizaci", "Generic event CCC", MessageTypes.EVENT), channel, ROUTING_KEYS.get("course"), properties);
    }

    private static String getDeadlineMessage(ActionTypes actionTypes, String aid, String title, String defaultTitle, MessageTypes type){
        return messageFactory
                .withAid(aid)
                .withTitle(new CsEnValueString(title, title))
                .withDefaultTitle(new CsEnValueString(defaultTitle, defaultTitle))
                .withDescription(new CsEnValueString("Popisek, muze byt dlouhy text, ale take nemusi", "Description"))
                .withCreated(ZonedDateTime.now())
                .withType(type)
                .withAction(actionTypes)
                .withTime(new BiTimeObject(ZonedDateTime.of(LocalDateTime.now().plusDays(1), ZoneId.systemDefault()), ZonedDateTime.of(LocalDateTime.now().plusDays(10).plusHours(2), ZoneId.systemDefault())))
                .withURL("https://www.moodle.felk.cvut.cz/")
                .withCourse("A0B01PAN")
                .buildDeadlineMessage()
                .toString();
    }

    private static String getNotificationMessage(ActionTypes actionTypes, String aid, String title, String defaultTitle, MessageTypes type){
        return messageFactory
                .withAid(aid)
                .withTitle(new CsEnValueString(title, title))
                .withDefaultTitle(new CsEnValueString(defaultTitle, defaultTitle))
                .withDescription(new CsEnValueString("Popisek, dlouhy text", "Description, long text"))
                .withCreated(ZonedDateTime.now())
                .withType(type)
                .withAction(actionTypes)
                .withURL("https://www.moodle.felk.cvut.cz/")
                .buildNotificationMessage()
                .toString();
    }

    private static String getEventMessage(ActionTypes actionTypes, String aid, String title, String defaultTitle, MessageTypes type){
        return messageFactory
                .withAid(aid)
                .withTitle(new CsEnValueString(title, title))
                .withDefaultTitle(new CsEnValueString(defaultTitle, defaultTitle))
                .withDescription(new CsEnValueString("TEST popisek, dlouhy text", "TEXT description, dlouhy text"))
                .withCreated(ZonedDateTime.now())
                .withType(type)
                .withAction(actionTypes)
                .withTime( new BiTimeObject(ZonedDateTime.of(LocalDateTime.now().plusDays(7), ZoneId.systemDefault()), ZonedDateTime.of(LocalDateTime.now().plusDays(7).plusHours(2), ZoneId.systemDefault())))
                .withURL("https://www.moodle.felk.cvut.cz/")
                .withCourse("B0B36PJV")
                .withRoom("T2:C2-82")
                .withSemester("B182")
                .buildEventMessage()
                .toString();
    }

    private static final String getRandomAID(){
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();

    }

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


}
