package donnoe.amanda;

import java.io.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.*;
import static donnoe.amanda.Amanda.INSTANCE;
import static donnoe.util.Functions.ExceptionalFunction;
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
    
    /**
     * this allows me to reference the constant map "before i finish making it"
     */
    private final BlockingQueue<Map<Integer, Future<Constant>>> constantsMap = new ArrayBlockingQueue<>(1);
    
    public Map<Integer, Future<Constant>> constantsMap() {
        return constantsMap.peek();
    }
    
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
        Map<Integer, BlockingQueue<Future<Constant>>> qs = IntStream.range(1, readUnsignedShort()).boxed().collect(Collectors.toMap(i -> i, i -> new ArrayBlockingQueue<>(1)));
        constantsMap.add(qs.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                e -> Amanda.INSTANCE.exec.submit(
                                        () -> e.getValue().take().get()
                                )
                        )
                )
        );
        qs.forEach((index, q) -> {
            if (!constantsMap().containsKey(index)) {
                return;
            }
            final Constant constant = Constant.readConstant(this, index);
            if (constant instanceof TwoWordPrimativeConstant) {
                constantsMap().remove(index + 1).cancel(true);
            }
            q.add(INSTANCE.queueForResolution(constant));
        });
    }

    @Override
    public void resolve() {
        sb.append(constantsMap().size());
    }
    
    
}
