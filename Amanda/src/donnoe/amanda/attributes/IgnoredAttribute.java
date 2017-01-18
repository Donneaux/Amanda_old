package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public class IgnoredAttribute extends Attribute {

    public IgnoredAttribute(ClassFile cF) {
        cF.skip(cF.readInt());
    }
    
}
