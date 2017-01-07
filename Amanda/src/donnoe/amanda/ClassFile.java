package donnoe.amanda;

import donnoe.amanda.constant.TwoWordPrimativeConstant;
import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.*;
import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.constant.Constant;
import static donnoe.util.Functions.ExceptionalFunction;
import static donnoe.util.Futures.*;
import donnoe.util.LookupMap;
import static java.lang.String.*;
import java.util.Collections;
import java.util.List;
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
        
        //anything that might be read by the constants has to exist at this point
        qs.forEach((index, q) -> {
            if (!constantFutures.containsKey(index)) {
                return;
            }
            Constant constant = Constant.readConstant(this, index);
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
    
    public static final Map<Character, String> ESCAPE_CHARACTERS = Collections.unmodifiableMap(
            new LookupMap<Character, String>(c -> c < 0x20 || c > 0x7e ? format("\\u%04x", (int) c) : valueOf(c)) {
                {
                    put('\b', "\\b");
                    put('\f', "\\f");
                    put('\n', "\\n");
                    put('\r', "\\r");
                    put('\t', "\\t");
                    put('\"', "\\\"");
                    put('\'', "\\\'");
                    put('\\', "\\\\");
                }
            }
    );

    public static String escapeCharacter(final char c) {
        return ESCAPE_CHARACTERS.get(c);
    }
    
    public static String escapeString(final String s) {
        return s.chars().mapToObj(i -> escapeCharacter((char) i)).collect(joining("", "\"", "\""));
    }
    
    
    
    public static List<String> getTypes(String s) {
        if (s.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        switch (s.charAt(0)) {
            case '(':
                return getTypes(s.substring(1));
            case '[':
                List<String> types = getTypes(s.substring(1));
                
                types.add(0, "ARRAY");
                return types;
            default:
                return Collections.EMPTY_LIST;
        }
        
    }
}
