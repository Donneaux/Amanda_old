package donnoe.amanda;

import donnoe.amanda.constant.Constant;
import java.io.DataInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collector;
import static donnoe.util.concurrent.Futures.toListFuture;
import static java.util.stream.IntStream.*;
import static donnoe.amanda.Amanda.INSTANCE;
import donnoe.util.concurrent.Futures;
import java.util.concurrent.Callable;
import static java.util.stream.Collectors.*;

/**
 *
 * @author joshuadonnoe
 */
public abstract class Blob {

    protected Blob(ClassFile cF) {
        this.cF = cF;
    }

    protected ClassFile cF;
    
    protected final DataInputStream dis() {
        return cF.in;
    }
    
    protected final <T> T read(Callable<T> c) {
        try {
            return c.call();
        } catch (IOException x) {
            throw new IOError(x);
        } catch (Exception x) {
            throw new AssertionError();
        }
    }
    
    public final String readUTF() {
        return read(() -> dis().readUTF());
    }
    
    public final double readDouble() {
        return read(() -> dis().readDouble());
    }
    
    public final long readLong() {
        return read(() -> dis().readLong());
    }
    
    public final float readFloat() {
        return read(() -> dis().readFloat());
    }
    
    public final int readInt() {
        return read(() -> dis().readInt());
    }
    
    public final int readUnsignedShort() {
        return read(() -> dis().readUnsignedShort());
    }
    
    public final int readUnsignedByte() {
        return read(() -> dis().readUnsignedByte());
    }
    
    public final int skip(int n) {
        return read(() -> dis().skipBytes(n));
    }
    
    public final <C extends Constant> Future<C> readConstantFuture() {
        return cF.getConstantFuture(readUnsignedShort());
    }
    
    public final Future<String> readStringFuture() {
        return cF.stringFutures.get(readUnsignedShort());
    }
    
    public final String readString() {
        return cF.strings.get(readUnsignedShort());
    }
    
    public final Future<List<String>> readTypesFuture() {
        return cF.typesFutures.get(readUnsignedShort());
    }
    
    public final Future<String> readShortStringFuture() {
        return cF.shortStringFutures.get(readUnsignedShort());
    }

    public final Future<List<String>> readShortStringsListFuture() {
        return readObjects(ClassFile::readShortStringFuture, toListFuture());
    }
    
    public final <O, T> T readObjects(Function<ClassFile, O> f, Collector<O, ?, T> c) {
        return readObjects(f, c, readUnsignedShort());
    }
    
    public final <O, T> T readObjects(Function<ClassFile, O> f, Collector<O, ?, T> c, int objectCount) {
        return range(0, objectCount).mapToObj(i -> f.apply(cF)).collect(c);
    }
    
    public final <B extends Blob> Future<List<B>> readItemFutureList(Function<ClassFile, B> f, int elementCount) {
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
