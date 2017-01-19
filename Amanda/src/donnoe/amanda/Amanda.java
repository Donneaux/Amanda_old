package donnoe.amanda;

import java.io.*;
import java.util.concurrent.*;
import static java.lang.System.*;
import static java.util.concurrent.Executors.newCachedThreadPool;
import java.util.function.Function;

/**
 *
 * @author joshuadonnoe
 */
public enum Amanda {
    INSTANCE;

    public final ExecutorService exec = newCachedThreadPool();

    private static PrintStream stream;

    private static Function<Object, Object> f = i -> i;
    
    public static void main(String[] args) {
        try {
            INSTANCE.setStream(args.length > 1);
            out.println(INSTANCE.decompile(args[0]));
        } finally {
            INSTANCE.exec.shutdownNow();
        }
    }
    
    public void setStream(boolean verbose) {
            stream = verbose
                ? err
                : new PrintStream(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                    }
                });
    }
    
    public String decompile(String clazz) {
        try {
            return queueForResolution(new ClassFile(clazz)).get().toString();
        } catch (ExecutionException | InterruptedException x) {
//            x.printStackTrace(stream);
            throw new IllegalStateException("Decompilation failed");
        }
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

    public <B extends Blob> Future<B> queueForResolution(B b) {
        return exec.submit(
                () -> {
                    try {
                        b.resolve();
                    } catch (ExecutionException | InterruptedException x) {
                        throw new IllegalStateException(x);
                    }
                },
                b
        );
    }
}
