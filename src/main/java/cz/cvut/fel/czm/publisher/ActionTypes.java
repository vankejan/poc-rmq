package cz.cvut.fel.czm.publisher;

public enum ActionTypes {
    GRADED("graded"),
    SUBMITTED("submitted"),
    OPEN("open"),
    DEADLINE("deadline");

    private final String value;

    ActionTypes(String value) {
        this.value = value;
    }

    public String toLowerCase() {
        return this.getValue().toLowerCase();
    }

    public String getValue() {
        return value;
    }
}

