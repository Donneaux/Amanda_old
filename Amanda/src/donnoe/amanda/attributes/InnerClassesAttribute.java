package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.stream.Collectors;

/**
 *
 * @author joshuadonnoe
 */
public class InnerClassesAttribute extends RecognizedAttribute {

    public InnerClassesAttribute(ClassFile cF) {
        super(cF);
        readObjects(InnerClassInfo::new, Collectors.toList());
    }
    
//    innerClasses = classFile.readObjects(
//                cF -> cF,
//                toMap(
//                        ClassFile::readUnsignedShort,
//                        cF -> INSTANCE.queueForResolution(new InnerClassInfo(cF))
//                )
//        );
}
