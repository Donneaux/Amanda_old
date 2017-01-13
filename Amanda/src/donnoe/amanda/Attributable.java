package donnoe.amanda;

import java.util.stream.Collectors;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Attributable extends Blob {

    public Attributable(ClassFile cF) {
        super(cF);
    }
    
    protected final void readAttributes() {
        cF.readObjects(cf -> {
            cf.skip(2);
            cf.skip(cf.readInt());
            return null;
        }, Collectors.toList());
    }
}
