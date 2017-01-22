/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import java.util.concurrent.ExecutionException;

public class ConstantValueAttribute extends RecognizedAttribute {
    /**
     *
     * @param cF
     */
    public ConstantValueAttribute(ClassFile cF) {
        super(cF);
        sb.append(readString());
    }

    @Override
    public void resolve() throws ExecutionException, InterruptedException {}   
}