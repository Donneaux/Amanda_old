package donnoe.amanda;

import java.io.*;
import java.util.concurrent.*;

/**
 *
 * @author joshuadonnoe
 */
public enum Amanda {
    INSTANCE;

    public final ExecutorService exec = Executors.newCachedThreadPool();

    private static PrintStream stream;

    public static void main(String[] args) {
        double i = 1 << (1 << 4);
        try {
            stream = args.length > 1 ? System.err : new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            });
            System.out.println(INSTANCE.queueForResolution(new ClassFile(args[0])).get());

        } catch (ExecutionException | InterruptedException x) {
            throw new Error(x);
        } finally {
            INSTANCE.exec.shutdownNow();
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
        return exec.submit(() -> b.resolve(), b);
    }

}
