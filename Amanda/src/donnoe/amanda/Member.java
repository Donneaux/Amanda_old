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
        super(cF);
        access = cF.readUnsignedShort();
        String name = cF.readString();
        Future<List<String>> types = cF.readTypesFuture();
        readAttributes();
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
