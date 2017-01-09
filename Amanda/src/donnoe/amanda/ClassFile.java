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
import java.util.HashMap;
import java.util.Queue;
import java.util.function.BiFunction;
import static java.util.stream.Collectors.*;
import static donnoe.amanda.constant.Constant.readConstant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import static java.util.Collections.unmodifiableMap;
import java.util.List;
import java.util.function.Function;
import static java.util.function.Function.identity;
import static java.util.stream.Stream.of;
import static java.util.stream.IntStream.range;

/**
 *
 * @author joshuadonnoe
 */
public final class ClassFile extends Accessible {

    public ClassFile(String fileName) {
        try {
            in = new DataInputStream(new FileInputStream(fileName));
        } catch (IOException x) {
            throw new IOError(x);
        }
        INSTANCE.println(String.format("0x%X%n%3$s.%s", readInt(), readUnsignedShort(), readUnsignedShort()));
        readConstants();
        access = readUnsignedShort();
        int thisClass = readUnsignedShort();
        int superClass = readUnsignedShort();
    }

    private void readConstants() {
        Map<Integer, BlockingQueue<Future<Constant>>> qs = IntStream.range(1, readUnsignedShort()).boxed().collect(toMap(i -> i, i -> new ArrayBlockingQueue<>(1)));
        constantFutures = qs.entrySet().stream().collect(toMap(
                Map.Entry::getKey,
                e -> Amanda.INSTANCE.exec.submit(() -> e.getValue().take().get())
        ));
        stringFutures = new LookupMap<>(i -> transform(constantFutures.get(i), Object::toString));
        typesFutures = new LookupMap<>(i -> transform(stringFutures.get(i), s -> getTypes(s)));
        shortStringFutures = new LookupMap<>(i -> transform(stringFutures.get(i), s -> s.replaceFirst("(.*)\\.class", "$1")));

    //anything that might be read by the constants has to exist at this point
        qs.forEach((index, q) -> {
            if (!constantFutures.containsKey(index)) {
                return;
            }
            Constant constant = readConstant(this, index);
            if (constant instanceof TwoWordPrimativeConstant) {
                constantFutures.remove(index + 1).cancel(true);
            }
            q.add(INSTANCE.queueForResolution(constant));
        });
    }

    //<editor-fold desc="futureMaps">
    private Map<Integer, Future<Constant>> constantFutures;
    
    private Map<Integer, Future<String>> stringFutures;
    
    private Map<Integer, Future<List<String>>> typesFutures;
    
    private Map<Integer, Future<String>> shortStringFutures;
    //</editor-fold>
    
    //<editor-fold desc="input reading">
    private DataInputStream in;

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
    
    public <C extends Constant> Future<C> readConstantFuture() {
        return getConstantFuture(readUnsignedShort());
    }
    
    public Future<String> readStringFuture() {
        return stringFutures.get(readUnsignedShort());
    }
    
    public Future<List<String>> readTypesFuture() {
        return typesFutures.get(readUnsignedShort());
    }
    
    public Future<String> readShortStringFuture() {
        return shortStringFutures.get(readUnsignedShort());
    }
    
    public <O, T> T readObjects(Function<ClassFile, O> f, Collector<O, ?, T> c) {
        return readObjects(f, c, readUnsignedShort());
    }
    
    public <O, T> T readObjects(Function<ClassFile, O> f, Collector<O, ?, T> c, int objectCount) {
        return range(0, objectCount).mapToObj(i -> f.apply(this)).collect(c);
    }
    
    public Future<List<String>> readShortStringListFuture() {
        return readObjects(ClassFile::readShortStringFuture, toListFuture());
    }
//</editor-fold>
    
    //<editor-fold desc="statics">
    public static final Map<Character, String> ESCAPE_CHARACTERS = unmodifiableMap(new LookupMap<Character, String>(c -> c < 0x20 || c > 0x7e ? format("\\u%04x", (int) c) : valueOf(c)) {{
        put('\b', "\\b");
        put('\f', "\\f");
        put('\n', "\\n");
        put('\r', "\\r");
        put('\t', "\\t");
        put('\"', "\\\"");
        put('\'', "\\\'");
        put('\\', "\\\\");
    }});
    
    private static final Map<Character, BiFunction<ClassFile, Queue<Character>, String>> TYPE_FUNCTIONS = unmodifiableMap(new HashMap<Character, BiFunction<ClassFile, Queue<Character>, String>>() {{
        putAll(new HashMap<Character, String>() {{
            put('V', "void");
            put('Z', "boolean");
            put('C', "char");
            put('B', "byte");
            put('S', "short");
            put('I', "int");
            put('J', "long");
            put('F', "float");
            put('D', "double");
            put('*', "?");
        }}.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> (cF, q) -> e.getValue())));
        putAll(new HashMap<Character, String>() {{
            put('+', "extends");
            put('-', "super");
        }}.entrySet().stream().collect(toMap(Map.Entry::getKey, e -> (cF, q) -> "? " + e.getValue() + ' ' + cF.getType(q))));
        putAll(of('(', ')').collect(toMap(identity(), s -> ClassFile::getType)));
        put('L', ClassFile::getClassType);
        put('T', ClassFile::getFormalType);
        put('[', ClassFile::getArrayType);
        put('<', ClassFile::getFormalTypeParameters);
    }});
    
    public static String escapeCharacter(final char c) {
        return ESCAPE_CHARACTERS.get(c);
    }
    
    public static String escapeString(final String s) {
        return s.chars().mapToObj(i -> escapeCharacter((char) i)).collect(joining("", "\"", "\""));
    }
    
    public static Queue<Character> toQueue(String s) {
        return s.chars().mapToObj(i -> (char)i).sequential().collect(toCollection(ArrayDeque::new));
    }
//</editor-fold>
    
    //<editor-fold desc="type parsing">
    private <C extends Constant> Future<C> getConstantFuture(int index) {
        return cast(constantFutures.get(index));
    }
    
    public List<String> getTypes(String s) {
        return getTypes(toQueue(s));
    }
    
    public List<String> getTypes(Queue<Character> q) {
        List<String> types = new ArrayList<>();
        for (; !q.isEmpty(); types.add(getType(q)));
        types.add(0, types.remove(types.size() - 1));
        return types;
    }
    
    public String getType(Queue<Character> q) {
        return TYPE_FUNCTIONS.get(q.remove()).apply(this, q);
    }
    
    public String getClassType(Queue<Character> q) {
        StringBuilder clazz = new StringBuilder();
        //so this loop can terminate in three ways
        while (!q.isEmpty() && q.peek() != ';') {
            //if there is no semi
            //if the class does not have a signature
            if (q.peek() == '/') {
                q.remove();
                clazz.append('.');
            } else if (q.peek() != '<') {
                clazz.append(q.remove());
            } else {//or it does have a signature
                break;
            }
        }
//will worry about inner classes later

//        if (clazz.toString().contains("$")) {
//            try {
//                clazz = new StringBuilder(innerClassNames.get().getOrDefault(clazz.toString(), clazz.toString()));
//            } catch (ExecutionException | InterruptedException x) {
//                throw new IllegalStateException(x);
//            }
//        }
if (!q.isEmpty()) {
    if (q.peek() == '<') {
        for (clazz.append(q.remove()).append(getType(q)); q.peek() != '>'; clazz.append(", ").append(getType(q)));
        clazz.append(q.remove());
    }
    q.remove();
}
return clazz.toString();
    }
    
    public String getFormalType(Queue<Character> q) {
        StringBuilder clazz = new StringBuilder();
        for (; q.peek() != ';'; clazz.append(q.remove()));
        q.remove();
        return clazz.toString();
    }
    
    public String getFormalTypeParameters(Queue<Character> q) {
        StringBuilder parameters = new StringBuilder("<");
        for (;; parameters.append(", ")) {
            for (; q.peek() != ':'; parameters.append(q.remove()));
            q.remove();
            if (q.peek() == ':') {
                q.remove();
            }
            for (parameters.append(" extends ").append(getType(q)); q.peek() == ':'; q.remove(), parameters.append(" & ").append(getType(q)));
            if (q.peek() == '>') {
                break;
            }
        }
        parameters.append(q.remove());
        return parameters.toString();
    }
    
    public String getArrayType(Queue<Character> q) {
        return getType(q) + "[]";
    }
//</editor-fold>
    
    @Override
    public void resolve() throws ExecutionException, InterruptedException {
        constantFutures.forEach(
                (i, f) -> sb.append(
                        String.format(
                                "%d = %s %s%n",
                                i,
                                getNow(f).getClass().getSimpleName(),
                                getNow(f)
                        )
                )
        );
        sb.append(constantFutures.size());
    }
}