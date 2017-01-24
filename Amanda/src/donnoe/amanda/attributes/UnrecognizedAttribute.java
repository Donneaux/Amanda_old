package donnoe.amanda.attributes;

import donnoe.amanda.Amanda;
import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public class UnrecognizedAttribute extends IgnoredAttribute {

    public UnrecognizedAttribute(ClassFile cF, String name) {
        super(cF);
        Amanda.INSTANCE.println(name);
    }

}
