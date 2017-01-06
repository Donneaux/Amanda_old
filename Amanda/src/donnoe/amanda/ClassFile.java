package donnoe.amanda;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.*;
import static donnoe.amanda.Amanda.INSTANCE;
import static donnoe.util.Functions.ExceptionalFunction;
import static donnoe.util.Futures.*;
import donnoe.util.LookupMap;
import java.util.HashMap;
import static donnoe.util.LookupMap.unmodifiable;
import static java.lang.String.*;
import static java.util.stream.Collectors.*;

/**
 *
 * @author joshuadonnoe
 */
public final class ClassFile extends Blob {

    public ClassFile(String fileName) {
        try {
            in = new DataInputStream(new FileInputStream(fileName));
        } catch (IOException x) {
            throw new IOError(x);
        }
        INSTANCE.println(String.format("0x%X%n%3$s.%s", readInt(), readUnsignedShort(), readUnsignedShort()));
        readConstants();
    }

    private DataInputStream in;

    private Map<Integer, Future<Constant>> constantFutures;

    private Map<Integer, Future<String>> stringFutures;

    public String readUTF() {
        return read(dis -> dis.readUTF());
    }

    public double readDouble() {
        return read(DataInputStream::readDouble);
    }

    public long readLong() {
        return read(DataInputStream::readLong);
    }

    public float readFloat() {
        return read(DataInputStream::readFloat);
    }

    public int readInt() {
        return read(DataInputStream::readInt);
    }

    public int readUnsignedShort() {
        return read(DataInputStream::readUnsignedShort);
    }

    public int readUnsignedByte() {
        return read(DataInputStream::readUnsignedByte);
    }

    public int skip(int n) {
        return read(dis -> dis.skipBytes(n));
    }

    private <T> T read(ExceptionalFunction<DataInputStream, T, IOException> f) {
        try {
            return f.apply(in);
        } catch (IOException x) {
            throw new IOError(x);
        }
    }

    private void readConstants() {
        Map<Integer, BlockingQueue<Future<Constant>>> qs = IntStream.range(1, readUnsignedShort()).boxed().collect(toMap(i -> i, i -> new ArrayBlockingQueue<>(1)));
        constantFutures = qs.entrySet().stream().collect(toMap(
                Map.Entry::getKey,
                e -> Amanda.INSTANCE.exec.submit(() -> e.getValue().take().get())
        ));
        stringFutures = new LookupMap<>(i -> transform(getConstantFuture(i), Object::toString));
        qs.forEach((index, q) -> {
            if (!constantFutures.containsKey(index)) {
                return;
            }
            final Constant constant = Constant.readConstant(this, index);
            if (constant instanceof TwoWordPrimativeConstant) {
                constantFutures.remove(index + 1).cancel(true);
            }
            q.add(INSTANCE.queueForResolution(constant));
        });
    }

    public <C extends Constant> Future<C> readConstantFuture() {
        return getConstantFuture(readUnsignedShort());
    }

    public <C extends Constant> Future<C> getConstantFuture(int index) {
        return cast(constantFutures.get(index));
    }
    
    public Future<String> readStringFuture() {
        return stringFutures.get(readUnsignedShort());
    }
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        constantFutures.forEach(
                (i, f) -> sb.append(
                        String.format(
                                "%d = %s%n",
                                i,
                                getNow(f)
                        )
                )
        );
        sb.append(constantFutures.size());
    }
    
    public static final Map<Character, String> ESCAPE_CHARACTERS;

    static {
        final Map<Character, String> escapeCharacter = new HashMap<>();
        escapeCharacter.put('\b', "\\b");
        escapeCharacter.put('\f', "\\f");
        escapeCharacter.put('\n', "\\n");
        escapeCharacter.put('\r', "\\r");
        escapeCharacter.put('\t', "\\t");
        escapeCharacter.put('\"', "\\\"");
        escapeCharacter.put('\'', "\\\'");
        escapeCharacter.put('\\', "\\\\");
        ESCAPE_CHARACTERS = unmodifiable(
                escapeCharacter,
                c -> c < 0x20 || c > 0x7e ? format("\\u%04x", (int) c) : valueOf(c)
        );
    }
    
    public static String escapeCharacter(final char c) {
        return ESCAPE_CHARACTERS.get(c);
    }
    
    public static String escapeString(final String s) {
        return s.chars().mapToObj(i -> escapeCharacter((char) i)).collect(joining("", "\"", "\""));
    }
}
