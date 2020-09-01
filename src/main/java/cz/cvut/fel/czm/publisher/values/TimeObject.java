package cz.cvut.fel.czm.publisher.values;

import java.time.ZonedDateTime;

public interface TimeObject {
    ZonedDateTime getstartDate();
    ZonedDateTime getendDate();

    Object getValue();
}
