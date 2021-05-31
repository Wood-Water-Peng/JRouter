package com.example.jrouter

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @Author jacky.peng* @Date 2021/4/16 10:07 AM
 * @Version 1.0
 */
class InjectMethodVisitor extends AdviceAdapter {
    private String methodName;
    String nameDesc
    String className

    InjectMethodVisitor(MethodVisitor mv, int access, String name, String desc, String className) {
        super(Opcodes.ASM6, mv, access, name, desc);
        this.methodName = name
        this.className = className
        this.nameDesc = name + desc
    }

    //register(new com.example.jrouter.route_modules.JRouter$$RouterModule$$home_module());
    // mv.visitTypeInsn(NEW, "com/example/jrouterapi/Test");
    //            mv.visitInsn(DUP);
    //            mv.visitMethodInsn(INVOKESPECIAL, "com/example/jrouterapi/Test", "<init>", "()V", false);
    //            mv.visitMethodInsn(INVOKESTATIC, "com/example/jrouterapi/core/JRouteHelper", "register", "(Lcom/example/jrouterapi/IRouteModule;)V", false);
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        List<String> paths = Repository.getClassPathList()
        for (int i = 0; i < paths.size(); i++) {
            String clazz = paths.get(i);
            mv.visitTypeInsn(NEW, clazz);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, clazz, "<init>", "()V", false);
            mv.visitMethodInsn(INVOKESTATIC, "com/example/jrouterapi/core/JRouteHelper", "register", "(Lcom/example/jrouterapi/IRouteModule;)V", false);
        }
        System.out.println("onMethodEnter className:"+this.className+"--method:"+this.methodName)
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
