package donnoe.amanda;

import java.io.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 *
 * @author joshuadonnoe
 */
public enum Amanda implements Function<String, String> {
    INSTANCE;

    public final ExecutorService exec = Executors.newCachedThreadPool();

    private static boolean isVerbose;

    private PrintStream stream;

    public static void main(String[] args) {
        isVerbose = args.length > 1;
        System.out.println(INSTANCE.apply(args[0]));
        INSTANCE.exec.shutdown();
    }

    /**
     * Set by entry point as either stderr or dev/null
     */
    @Override
    public String apply(String t) {

        //better to check the bool once rather than every time
        stream = isVerbose ? System.err : new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        });
        return new ClassFile(t).toString();
    }

    /**
     * delegates to stream to possibly print object. This model allows me to
     * create a message possibly with side-effects and still have the choice on
     * whether it is displayed. Client code does not need to check is verbose
     * flag was set. Although message does not need to be constructed if not in
     * verbose mode but this way side effect still happens and client code does
     * not know/care the mode
     *
     * @param o
     */
    public void println(Object o) {
        stream.println(o);
    }

}
