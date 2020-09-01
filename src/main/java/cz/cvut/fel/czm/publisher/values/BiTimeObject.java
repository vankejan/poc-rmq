package cz.cvut.fel.czm.publisher.values;

import org.json.JSONObject;

import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class BiTimeObject implements TimeObject {

    private final ZonedDateTime startDate;
    private final ZonedDateTime endDate;

    public BiTimeObject(ZonedDateTime startDate, ZonedDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public ZonedDateTime getstartDate() {
        return startDate;
    }

    @Override
    public ZonedDateTime getendDate() {
        return endDate;
    }

    @Override
    public Object getValue() {
        JSONObject value = new JSONObject();
        value.put("from", ISO_OFFSET_DATE_TIME.format(startDate));
        value.put("to", ISO_OFFSET_DATE_TIME.format(endDate));
        return value;
    }
}
