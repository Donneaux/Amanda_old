package donnoe.amanda.attribute;

import donnoe.amanda.Blob;
import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Attribute extends Blob {

    public Attribute(ClassFile cF) {
        super(cF);
    }

}
