package cz.cvut.fel.czm.publisher;

public enum MessageTypes {
    EXAM("EXAM"),
    TEST("TEST"),
    TASK("TASK"),
    EVENT("EVENT"),
    NOTIFICATION("NOTIFICATION");


    private final String value;

    MessageTypes(String value) {
        this.value = value;
    }

    public String toLowerCase() {
        return this.getValue().toLowerCase();
    }

    public String getValue() {
        return value;
    }
}