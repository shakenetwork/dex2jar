/*
 * dex2jar - Tools to work with android .dex and java .class files
 * Copyright (c) 2009-2013 Panxiaobo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.d2j.map;


import org.objectweb.asm.*;

public class AsmInheritanceClassVisitor extends ClassVisitor {
    final InheritanceTree tree;
    InheritanceTree.Clz clz;

    public AsmInheritanceClassVisitor(InheritanceTree tree) {
        super(Opcodes.ASM4);
        this.tree = tree;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        clz = tree.addClz(access, "L" + name + ";");
        if (superName != null) {
            clz.relateSuper("L" + superName + ";");
        }
        if (interfaces != null) {
            for (String s : interfaces) {
                clz.relateInterface("L" + s + ";");
            }
        }
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        clz.addField(access, name, desc);
        return null;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Type[] aT = Type.getArgumentTypes(desc);
        String[] aS = new String[aT.length];
        for (int i = 0; i < aT.length; i++) {
            aS[i] = aT[i].getDescriptor();
        }
        clz.addMethod(access, name, aS, Type.getReturnType(desc).getDescriptor());
        return null;
    }
}
