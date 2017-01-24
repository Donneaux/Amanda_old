package donnoe.amanda.attributes;

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
