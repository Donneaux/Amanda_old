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
        double i = 1 << (1 << 4);
        try {
            isVerbose = args.length > 1;
            System.out.println(INSTANCE.queueForResolution(new ClassFile(args[0])).get());
        } catch (ExecutionException | InterruptedException x) {
        } finally {
            INSTANCE.exec.shutdownNow();
        }
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
        print(o + "\n");
    }

    public void printf(String format, Object... args) {
        print(String.format(format, args));
    }
    
    public void print(Object o) {
        stream.print(o);
    }
    
    public <B extends Blob> Future<B> queueForResolution(B b) {
        return exec.submit(
                () -> {
                    try {
                        b.resolve();
                    } finally {
                        return;
                    }
                },
                b
        );
    }
    
}
