package cz.cvut.fel.czm.publisher.values;

public class MonolingualValueString implements MultilingualString {

    private final String value;

    public MonolingualValueString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
