package donnoe.amanda;

public abstract class Accessible extends Attributable {

    protected Accessible(ClassFile cF) {
        super(cF);
    }
    
    protected int access;//need to consider that this has synthetic attribute
    
}
