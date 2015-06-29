package org.finasoft.metal.core.normalization;

import java.util.HashMap;
import java.util.Set;

/**
 * loose replication of a fix message structure
 * The tag value scheme is represented in a hashmap
 */
public class PseudoFIXMessage {
    private HashMap<Field,String> fields;

    public PseudoFIXMessage() {
        fields = new HashMap<Field, String>();
    }

    public void setField( Field field, String value) {
        fields.put( field, value);
    }

    public String getField( Field field) {
        return fields.get( field);
    }

    public Set<Field> getFields() { return fields.keySet(); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for( Field field : fields.keySet()) {
            sb.append( field.getNumber()).append('=').append( fields.get(field)).append('|');
        }
        return sb.toString();
    }
}
