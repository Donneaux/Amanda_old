package donnoe.amanda;

import java.util.stream.Collectors;

public abstract class Accessible extends Blob {
    protected int access;
    
    protected void readAttributes(ClassFile cF) {
        cF.readObjects(cf -> {
            cf.skip(2);
            cf.skip(cf.readInt());
            return null;
        }, Collectors.toList());
    }
}
