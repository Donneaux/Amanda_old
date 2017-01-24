package donnoe.amanda.attribute;

import donnoe.amanda.ClassFile;

/**
 *
 * @author joshuadonnoe
 */
public abstract class RecognizedAttribute extends Attribute {

    public RecognizedAttribute(ClassFile cF) {
        super(cF);
        skip(4);
    }

}
