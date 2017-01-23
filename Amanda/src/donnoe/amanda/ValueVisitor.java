package donnoe.amanda.accessibles;

import donnoe.amanda.ClassFile;
import donnoe.util.DefaultMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author joshuadonnoe
 */
public enum ValueVisitor {

    /**
     *
     */
    BOOLEAN {

        @Override
        public String visit(Object value) {
            return String.valueOf(value.toString().equals("1"));
        }
        
    },

    /**
     *
     */
    STANDARD {

        @Override
        public String visit(Object value) {
            return value.toString();
        }
    
    },

    /**
     *
     */
    CHARACTER {

        @Override
        public String visit(Object value) {
            return "'" + ClassFile.escapeCharacter((char) Integer.parseInt(value.toString())) + "'";
        }
        
    };

    /**
     *
     */
    private static final Map<String, ValueVisitor> VISITORS = Collections.unmodifiableMap(
            new DefaultMap<String, ValueVisitor>(new HashMap<String, ValueVisitor>(){{
                put("char", CHARACTER);
                put("boolean", BOOLEAN);
                        
            }}, STANDARD)
    );
    /**
     *
     * @param type
     * @param value
     * @return
     */
    public static String visit(String type, String value) {
        return VISITORS.get(type).visit(value);
    }

    /**
     *
     * @param value
     * @return
     */
    public abstract String visit(Object value);
}