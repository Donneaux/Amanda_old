package donnoe.amanda;

import java.util.stream.Collectors;

public abstract class Accessible extends Blob {

    protected Accessible(ClassFile cF) {
        super(cF);
    }
    
    protected int access;
    
    
    protected void readAttributes() {
        cF.readObjects(cf -> {
            cf.skip(2);
            cf.skip(cf.readInt());
            return null;
        }, Collectors.toList());
    }
}
