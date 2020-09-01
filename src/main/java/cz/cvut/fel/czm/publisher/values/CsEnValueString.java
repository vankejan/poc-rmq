package cz.cvut.fel.czm.publisher.values;

import org.json.JSONObject;

public class CsEnValueString implements MultilingualString {

    private final String csValue;
    private final String enValue;

    public CsEnValueString(String csValue, String enValue) {
        this.csValue = csValue;
        this.enValue = enValue;
    }

    @Override
    public Object getValue() {
        JSONObject value = new JSONObject();
        value.put("cs", csValue);
        value.put("en", enValue);

        if (csValue == null){
            return enValue;
        } else if (enValue == null){
            return csValue;
        } else {
            return value;
        }
    }

    @Override
    public String toString() {
        return "{cs='" + csValue + "', en='" + enValue + "'}";
    }

}
