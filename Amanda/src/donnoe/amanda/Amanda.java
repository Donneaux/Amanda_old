package donnoe.amanda;

import java.io.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 *
 * @author joshuadonnoe
 */
public enum Amanda {
    INSTANCE;

    public final ExecutorService exec = Executors.newCachedThreadPool();

    private static boolean isVerbose;

    private static PrintStream stream;

    public static void main(String[] args) {
        double i = 1 << (1 << 4);
        try {
            isVerbose = args.length > 1;
            stream = isVerbose ? System.err : new PrintStream(new OutputStream() {
                @Override

                public void write(int b) throws IOException {
                }
            }
            );
            System.out.println(INSTANCE.queueForResolution(new ClassFile(args[0])).get());
        } catch (ExecutionException | InterruptedException x) {
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
