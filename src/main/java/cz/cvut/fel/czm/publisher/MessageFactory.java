package cz.cvut.fel.czm.publisher;

import cz.cvut.fel.czm.publisher.values.MultilingualString;
import cz.cvut.fel.czm.publisher.values.TimeObject;
import org.json.*;

import java.time.ZonedDateTime;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class MessageFactory {

    private String aid;
    private MultilingualString title;
    private MultilingualString defaultTitle;
    private MultilingualString description;
    private MessageTypes type;
    private ActionTypes action;
    private ZonedDateTime created;
    private String url;
    private String course;
    private String room;
    private String semester;
    private TimeObject time;

    private JSONObject message = new JSONObject();

    public MessageFactory() {
    }

    public MessageFactory withAid(String aid){
        this.aid = aid;
        return this;
    }
    public MessageFactory withTitle(MultilingualString title){
        this.title = title;
        return this;
    }
    public MessageFactory withDefaultTitle(MultilingualString defaultTitle){
        this.defaultTitle = defaultTitle;
        return this;
    }
    public MessageFactory withDescription(MultilingualString description){
        this.description = description;
        return this;
    }
    public MessageFactory withType(MessageTypes type){
        this.type = type;
        return this;
    }
    public MessageFactory withAction(ActionTypes action){
        this.action = action;
        return this;
    }
    public MessageFactory withCreated(ZonedDateTime created){
        this.created = created;
        return this;
    }
    public MessageFactory withURL(String url){
        this.url = url;
        return this;
    }
    public MessageFactory withTime(TimeObject time){
        this.time = time;
        return this;
    }
    public MessageFactory withCourse(String course) {
        this.course = course;
        return this;
    }
    public MessageFactory withRoom(String room) {
        this.room = room;
        return this;
    }
    public MessageFactory withSemester(String semester) {
        this.semester = semester;
        return this;
    }

    public JSONObject buildDeadlineMessage(){
        message = new JSONObject();
        message.put("aid", aid);
        message.put("title", title.getValue());
        message.put("default_title", defaultTitle.getValue());
        message.put("description", description.getValue());
        message.put("type", type.getValue());
        message.put("action", action.getValue());
        message.put("created", ISO_OFFSET_DATE_TIME.format(created));
        JSONObject info = new JSONObject();
        info.put("url", url);
        info.put("time", time.getValue());
        info.put("course", course);
        message.put("info", info);
        return this.message;
    }

    public JSONObject buildNotificationMessage(){
        message = new JSONObject();
        message.put("aid", aid);
        message.put("title", title.getValue());
        message.put("default_title", defaultTitle.getValue());
        message.put("description", description.getValue());
        message.put("type", type.getValue());
        message.put("created", ISO_OFFSET_DATE_TIME.format(created));
        JSONObject info = new JSONObject();
        info.put("url", url);
        message.put("info", info);
        return this.message;
    }

    public JSONObject buildEventMessage(){
        message = new JSONObject();
        message.put("aid", aid);
        message.put("title", title.getValue());
        message.put("default_title", defaultTitle.getValue());
        message.put("description", description.getValue());
        message.put("type", type.getValue());
        message.put("action", action.getValue());
        message.put("created", ISO_OFFSET_DATE_TIME.format(created));
        JSONObject info = new JSONObject();
        info.put("url", url);
        info.put("time", time.getValue());
        info.put("semester", semester);
        info.put("room", room);
        info.put("course", course);
        message.put("info", info);
        return this.message;
    }


    @Override
    public String toString() {
        return this.message.toString();
    }


}
