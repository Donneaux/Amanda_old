/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package donnoe.amanda.attributes;

import donnoe.amanda.ClassFile;
import donnoe.amanda.attributes.Attribute;

/**
 *
 * @author joshuadonnoe
 */
public class IgnoredAttribute extends Attribute {

    public IgnoredAttribute(ClassFile cF) {
        cF.skip(cF.readInt());
    }
    
}
