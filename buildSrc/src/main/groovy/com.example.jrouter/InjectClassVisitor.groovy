package com.example.jrouter

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @Author jacky.peng* @Date 2021/4/16 10:06 AM
 * @Version 1.0
 */
class InjectClassVisitor extends ClassVisitor {
    private String mClassName;
    private String mSuperName;
    private String[] interfaces;


    InjectClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);
    }

    //类被访问
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.mClassName = name;
        this.mSuperName = superName;
        //获取类所实现的所有接口
        this.interfaces = interfaces
    }

    String nameDesc;


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        nameDesc = name + descriptor
        if (name == "injectRouteModuleByPlugin") {
            System.out.println("InjectClassVisitor injectRouteModuleByPlugin")
            mv = new InjectMethodVisitor(mv, access, name, descriptor, mClassName)
        }
        return mv
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
