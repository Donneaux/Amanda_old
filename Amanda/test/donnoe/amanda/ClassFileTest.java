package donnoe.amanda;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Future;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static donnoe.amanda.Amanda.INSTANCE;

/**
 *
 * @author joshuadonnoe
 */
public class ClassFileTest {
    
    public static final String AMANDA = "build/classes/donnoe/amanda/Amanda.class";
    
    public ClassFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        INSTANCE.setStream(true);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of escapeCharacter method, of class ClassFile.
     */
    @Test
    public void testEscapeCharacter() {
        System.out.println("escapeCharacter");
        char c = ' ';
        String expResult = " ";
        String result = ClassFile.escapeCharacter(c);
        assertEquals(expResult, result);
    }

    /**
     * Test of escapeString method, of class ClassFile.
     */
    @Test
    public void testEscapeString() {
        System.out.println("escapeString");
        String s = "";
        String expResult = "\"\"";
        String result = ClassFile.escapeString(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of getTypes method, of class ClassFile.
     */

    @Test
    public void testGetTypes_String() {
        System.out.println("getTypes");
        String s = "";
        
        ClassFile instance = new ClassFile(AMANDA);
        assertEquals(Arrays.asList("donnoe.amanda.Amanda[]"), instance.getTypes("[Ldonnoe/amanda/Amanda;"));
        assertEquals(Arrays.asList("void", "java.lang.String", "int"), instance.getTypes("(Ljava/lang/String;I)V"));
        assertEquals(Arrays.asList("java.util.concurrent.ExecutorService"), instance.getTypes("Ljava/util/concurrent/ExecutorService;"));
        assertEquals(Arrays.asList("java.io.PrintStream"), instance.getTypes("Ljava/io/PrintStream;"));
/*        
Ljava/io/PrintStream;
()V
(Ljava/lang/String;)Ljava/lang/String;
Ldonnoe/amanda/Amanda;
(Ldonnoe/amanda/Blob;)Ljava/util/concurrent/Future;
(Ljava/lang/Object;)V
()V
(Ljava/lang/Object;)Ljava/lang/Object;
Ljava/util/function/Function;
(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
()Ljava/lang/Object;
(Ljava/io/OutputStream;)V
(Ljava/lang/String;)V
(Ljava/lang/String;)V
Ljava/io/PrintStream;
Ljava/io/PrintStream;
()Ljava/util/concurrent/ExecutorService;
()Ljava/util/List;
(Ljava/lang/Runnable;Ljava/lang/Object;)Ljava/util/concurrent/Future;
()Ljava/lang/Object;
()Ljava/lang/String;
(Ldonnoe/amanda/Blob;)Ljava/lang/Runnable;
(Ljava/lang/Throwable;)V
()V
(Ldonnoe/amanda/Blob;)Ljava/lang/Runnable;
()Ljava/util/function/Function;
(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
(Ldonnoe/amanda/Blob;)V
(Ljava/lang/Object;)Ljava/lang/Object;
()Ljava/util/function/Function;
(Ldonnoe/amanda/Blob;)Ljava/lang/Runnable;
()Ljava/util/function/Function;
(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
(Ljava/lang/Object;)Ljava/lang/Object;
(Ldonnoe/amanda/Blob;)V
*/
        
    }

}
