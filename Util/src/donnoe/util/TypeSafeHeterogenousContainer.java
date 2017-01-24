package donnoe.util;

import java.util.Map;
import static java.util.Collections.*;
import java.util.List;
import static java.util.stream.Stream.of;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author joshuadonnoe
 * @param <S>
 */
public class TypeSafeHeterogenousContainer<S> {
    private final Map<Class<? extends S>, List<S>> map; 
    
    public TypeSafeHeterogenousContainer(Map<Class<? extends S>, List<S>> map) {
        this.map = map;
    }
    
    public final <S2 extends S> List<S> put(S2 value) {
        Class<S2> clazz = (Class) value.getClass();
        return map.merge(clazz, singletonList(value), (s1, s2) -> of(s1, s2).flatMap(List::stream).collect(toList()));
    }
    
    public final <S2 extends S> List<S2> get(Class<S2> clazz) {
        List<S2> list = (List) map.get(clazz);
        return list;
    }
}