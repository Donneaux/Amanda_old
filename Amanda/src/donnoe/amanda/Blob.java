package donnoe.amanda;

import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.amanda.constant.Constant;
import donnoe.util.Functions;
import donnoe.util.Futures;
import static donnoe.util.Futures.toListFuture;
import java.io.DataInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collector;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Blob {

    protected Blob(ClassFile cF) {
        this.cF = cF;
    }

    protected ClassFile cF;
    
    //<editor-fold desc="input reading">

    protected final DataInputStream dis() {
        return cF.in;
    }
    
    protected final <T> T read(Functions.ExceptionalFunction<DataInputStream, T> f) {
        try {
            return f.apply(dis());
        } catch (IOException x) {
            throw new IOError(x);
        } catch (Exception x) {
            throw new AssertionError();
        }
    }
    
    public final String readUTF() {
        return read(dis -> dis.readUTF());
    }
    
    public final double readDouble() {
        return read(DataInputStream::readDouble);
    }
    
    public final long readLong() {
        return read(DataInputStream::readLong);
    }
    
    public final float readFloat() {
        return read(DataInputStream::readFloat);
    }
    
    public final int readInt() {
        return read(DataInputStream::readInt);
    }
    
    public final int readUnsignedShort() {
        return read(DataInputStream::readUnsignedShort);
    }
    
    public final int readUnsignedByte() {
        return read(DataInputStream::readUnsignedByte);
    }
    
    public final int skip(int n) {
        return read(dis -> dis.skipBytes(n));
    }
    
    public <C extends Constant> Future<C> readConstantFuture() {
        return cF.getConstantFuture(readUnsignedShort());
    }
    
    public Future<String> readStringFuture() {
        return cF.stringFutures.get(readUnsignedShort());
    }
    
    public String readString() {
        return cF.strings.get(readUnsignedShort());
    }
    
    public Future<List<String>> readTypesFuture() {
        return cF.typesFutures.get(readUnsignedShort());
    }
    
    public Future<String> readShortStringFuture() {
        return cF.shortStringFutures.get(readUnsignedShort());
    }

    public Future<List<String>> readShortStringsListFuture() {
        return cF.readObjects(ClassFile::readShortStringFuture, toListFuture());
    }
    
    public <O, T> T readObjects(Function<ClassFile, O> f, Collector<O, ?, T> c) {
        return cF.readObjects(f, c, readUnsignedShort());
    }
    
    public <O, T> T readObjects(Function<ClassFile, O> f, Collector<O, ?, T> c, int objectCount) {
        return range(0, objectCount).mapToObj(i -> f.apply(cF)).collect(c);
    }
    
    public <B extends Blob> Future<List<B>> readItemFutureList(Function<ClassFile, B> f, int elementCount) {
        return readObjects(f.andThen(INSTANCE::queueForResolution), collectingAndThen(toList(), Futures::transformList), elementCount);
    }
    //</editor-fold>

    protected final StringBuilder sb = new StringBuilder();

    public abstract void resolve() throws ExecutionException, InterruptedException;

    @Override
    public final String toString() {
        return sb.toString();
    }

}
