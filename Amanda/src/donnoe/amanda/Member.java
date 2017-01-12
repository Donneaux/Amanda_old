package donnoe.amanda;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author joshuadonnoe
 */
public class Member extends Accessible {

    
    
    public Member(ClassFile cF) {
        access = cF.readUnsignedShort();
        Future<String> name = cF.readStringFuture();
        Future<List<String>> types = cF.readTypesFuture();
        readAttributes(cF);
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}