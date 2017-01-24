package donnoe.amanda;

import static donnoe.amanda.ClassFile.escapeCharacter;
import donnoe.util.DefaultMap;
import static java.lang.Integer.parseInt;
import java.util.*;
import static java.util.Collections.unmodifiableMap;

/**
 *
 * @author joshuadonnoe
 */
public enum ValuePrinter {

    BOOLEAN {

        @Override
        public String visit(Object value) {
            return String.valueOf(value.toString().equals("1"));
        }

    },
    STANDARD {

        @Override
        public String visit(Object value) {
            return value.toString();
        }

    },
    CHARACTER {

        @Override
        public String visit(Object value) {
            return "'" + escapeCharacter((char) parseInt(value.toString())) + "'";
        }

    };

    private static final Map<String, ValuePrinter> VISITORS = unmodifiableMap(new DefaultMap<String, ValuePrinter>(new HashMap<String, ValuePrinter>() {
        {
            put("char", CHARACTER);
            put("boolean", BOOLEAN);

        }
    }, STANDARD)
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
