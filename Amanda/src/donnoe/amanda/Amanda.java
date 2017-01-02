package donnoe.amanda;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author joshuadonnoe
 */
public class Amanda {
    public static void main(String[] args) {
        stream = (args.length > 1 ? System.err : new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        }));
        System.out.println(new ClassFile(args[0]));
    }
    
    /**
     * Set by entry point as either stderr or dev/null
     */
    private static PrintStream stream;
    
    /**
     * delegates to stream to possibly print object. This model allows me to create a message
     * possibly with side-effects and still have the choice on whether it is displayed.
     * Client code does not need to check is verbose flag was set. Although message does not need to be
     * constructed if not in verbose mode but this way side effect still happens and client code
     * does not know/care the mode 
     * 
     * @param o 
     */
    public static void println(Object o) {
        stream.println(o);
    }
}
