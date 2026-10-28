package review.minerals;

import java.nio.file.*;
import org.objectweb.asm.*;

/** Creates a compiled old-namespace consumer without bundling a second library. */
public final class LegacyFixtureGenerator implements Opcodes {
    public static void main(String[] arguments) throws Exception {
        ClassWriter writer=new ClassWriter(0);
        writer.visit(V1_8,ACC_PUBLIC,"review/minerals/LegacyConsumer",null,"com/mcmoddev/baseminerals/proxy/CommonProxy",null);
        MethodVisitor method=writer.visitMethod(ACC_PUBLIC|ACC_STATIC,"material","()Ljava/lang/Object;",null,null);
        method.visitCode();method.visitLdcInsn("lithium");
        method.visitMethodInsn(INVOKESTATIC,"com/mcmoddev/baseminerals/init/Materials","getMaterialByName","(Ljava/lang/String;)Lcom/mcmoddev/lib/material/MMDMaterial;",false);
        method.visitInsn(ARETURN);method.visitMaxs(1,0);method.visitEnd();
        method=writer.visitMethod(ACC_PUBLIC|ACC_STATIC,"proxy","()Ljava/lang/Object;",null,null);
        method.visitCode();method.visitFieldInsn(GETSTATIC,"com/mcmoddev/baseminerals/BaseMinerals","proxy","Lcom/mcmoddev/baseminerals/proxy/CommonProxy;");
        method.visitInsn(ARETURN);method.visitMaxs(1,0);method.visitEnd();
        method=writer.visitMethod(ACC_PUBLIC|ACC_STATIC,"reflected","()Ljava/lang/Class;",null,new String[]{"java/lang/ClassNotFoundException"});
        method.visitCode();method.visitLdcInsn("com.mcmoddev.baseminerals.BaseMinerals");
        method.visitMethodInsn(INVOKESTATIC,"java/lang/Class","forName","(Ljava/lang/String;)Ljava/lang/Class;",false);
        method.visitInsn(ARETURN);method.visitMaxs(1,0);method.visitEnd();writer.visitEnd();
        Path file=Paths.get(arguments[0],"review/minerals/LegacyConsumer.class");Files.createDirectories(file.getParent());Files.write(file,writer.toByteArray());
        ClassWriter config=new ClassWriter(0);
        config.visit(V1_8,ACC_PUBLIC,"review/minerals/LegacyConfiguration",null,"com/mcmoddev/baseminerals/util/Config",null);config.visitEnd();
        Files.write(file.resolveSibling("LegacyConfiguration.class"),config.toByteArray());
    }
}
