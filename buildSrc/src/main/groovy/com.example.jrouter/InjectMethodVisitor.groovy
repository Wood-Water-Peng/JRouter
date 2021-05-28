package com.example.jrouter

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @Author jacky.peng* @Date 2021/4/16 10:07 AM
 * @Version 1.0
 */
class InjectMethodVisitor extends AdviceAdapter {
    private String className;
    private String methodName;
    String nameDesc

    InjectMethodVisitor(MethodVisitor mv, int access, String name, String desc, String className) {
        super(Opcodes.ASM6, mv, access, name, desc);
        this.className = className;
        this.methodName = name;
        this.nameDesc = name + desc
    }

    //register(new com.example.jrouter.route_modules.JRouter$$RouterModule$$home_module());
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        mv.visitTypeInsn(NEW, className);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
        mv.visitMethodInsn(INVOKESTATIC, "com/example/jrouterapi/core/JRouteHelper", "register", "(Lcom.example.jrouterapi.IRouteModule;)V", false);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

    @Override
    void visitCode() {
        super.visitCode();
    }
}
