/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package donnoe.amanda.attributes;

import donnoe.amanda.Amanda;
import donnoe.amanda.ClassFile;
import donnoe.amanda.attributes.IgnoredAttribute;

/**
 *
 * @author joshuadonnoe
 */
public class UnrecognizedAttribute extends IgnoredAttribute {

    public UnrecognizedAttribute(ClassFile cF, String name) {
        super(cF);
        Amanda.INSTANCE.println(name);
    }
    
}
