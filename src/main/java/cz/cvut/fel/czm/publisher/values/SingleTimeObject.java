package cz.cvut.fel.czm.publisher.values;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class SingleTimeObject implements TimeObject {

    private final ZonedDateTime value;

    public SingleTimeObject(ZonedDateTime value) {
        this.value = value;
    }

    @Override
    public ZonedDateTime getstartDate() {
        return value;
    }

    @Override
    public ZonedDateTime getendDate() {
        return value;
    }

    @Override
    public Object getValue() {
        return ISO_OFFSET_DATE_TIME.format(value);
    }
}
