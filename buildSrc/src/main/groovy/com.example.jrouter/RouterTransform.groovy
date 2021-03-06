import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.example.jrouter.InjectClassVisitor
import com.example.jrouter.Logger
import com.example.jrouter.Repository
import com.example.jrouter.TrackClassVisitor
import com.example.jrouter.TrackUtil
import com.google.common.collect.Sets
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class RouterTransform extends Transform {
    public static final String VERSION = "1.0.0"

    @Override
    String getName() {
        return "RouterTransform"

    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS)
    }

    @Override
    boolean isIncremental() {
        return false
    }

    private void beforeTransform(TransformInvocation transformInvocation) {
        Logger.printCopyright()
    }
    Context mContext

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        beforeTransform(transformInvocation)
        Collection<TransformInput> transformInputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        mContext = transformInvocation.context
        transformInputs.each { TransformInput input ->

            // ?????? jar
            input.jarInputs.each { JarInput jarInput ->
                forEachJar(jarInput, outputProvider, transformInvocation.context)
            }
            //????????????
            input.directoryInputs.each { DirectoryInput directoryInput ->
                forEachDirectory(directoryInput, outputProvider)
            }
        }


        //???????????????
        //1.?????????????????????????????????class???????????????
        //2.??????
//        System.out.println("input jar path->" + Repository.injectJarInputPath.getAbsolutePath())
//        System.out.println("out jar path->" + Repository.injectJarOutPath.getAbsolutePath())

//        modifyClassInJar(Repository.injectJarOutPath, Repository.ROUTER_HELPER_CLASS)

        if (Repository.injectBean != null) {
            String srcDir = Repository.injectBean.srcFilePath
            if (srcDir.endsWith(".jar")) {
                //?????????jar
                System.out.println("modifyClass ???????????? className:" + Repository.injectBean.injectClassName + "---des:" + Repository.injectBean.desFilePath)
                injectJar(Repository.injectBean.desFilePath, Repository.injectBean.injectClassName)
            } else {

            }
        }
    }


    private void injectJar(String desPath, String className) {
        File desFile = new File(desPath)
        def outputJar = new File(desFile.getParent(), "tmp_" + desFile.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
        JarFile jarFile = new JarFile(desFile);
        Enumeration enumeration = jarFile.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            InputStream inputStream
            try {
                inputStream = jarFile.getInputStream(jarEntry)
            } catch (Exception e) {
                IOUtils.closeQuietly(inputStream)
                e.printStackTrace()
            }
            String entryName = jarEntry.getName()
            JarEntry entry = new JarEntry(entryName)
            byte[] modifiedClassBytes = null
            byte[] sourceClassBytes
            try {
                jarOutputStream.putNextEntry(entry)
                sourceClassBytes = TrackUtil.toByteArrayAndAutoCloseStream(inputStream)
            } catch (Exception e) {
                System.out.println("Exception encountered while processing jar: " + jarFile.getAbsolutePath())
                IOUtils.closeQuietly(file)
                IOUtils.closeQuietly(jarOutputStream)
                e.printStackTrace()
            }
            if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                System.out.println("injectJar entryName:" + entryName)
                modifiedClassBytes = injectClass(sourceClassBytes)
            }
            if (modifiedClassBytes == null) {
                jarOutputStream.write(sourceClassBytes)
            } else {
                jarOutputStream.write(modifiedClassBytes)
            }
            jarOutputStream.closeEntry()
        }
        jarOutputStream.close()
        jarFile.close()
        outputJar.renameTo(desFile.getAbsolutePath())
    }

    private byte[] injectClass(byte[] srcClass){
        try {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
            ClassVisitor injectVisitor = new InjectClassVisitor(classWriter)
            ClassReader cr = new ClassReader(srcClass)
            cr.accept(injectVisitor, ClassReader.EXPAND_FRAMES + ClassReader.SKIP_FRAMES)
            return classWriter.toByteArray()
        } catch (Exception ex) {
            ex.printStackTrace()
            return srcClass
        }
    }

    void forEachDirectory(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File dir = directoryInput.file
        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(),
                Format.DIRECTORY)
        println "srcDir:${dir}, desDir:${dest}"
        //????????????????????????.class??????
        if (dir) {
            dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->

                ClassReader classReader = new ClassReader(file.bytes)

                // ???class???????????????
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                // ??????class???????????????????????????????????????????????????????????????ClassVisitor???????????????
                ClassVisitor classVisitor = new TrackClassVisitor(classWriter, dir.absolutePath, dest.absolutePath)
                // ????????????ClassVisitor?????????????????????
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                // toByteArray???????????????????????????????????????byte??????????????????
                byte[] bytes = classWriter.toByteArray()
                // ????????????????????????????????????????????????????????????class???????????????
                FileOutputStream outputStream = new FileOutputStream(file.path)
                outputStream.write(bytes)
                outputStream.close()
            }
        }
        // ?????????????????????????????????????????????????????????
        FileUtils.copyDirectory(dir, dest)
    }

    void forEachJar(JarInput jarInput, TransformOutputProvider outputProvider, Context context) {
        String destName = jarInput.file.name
        //????????????????????? md5 ?????????????????????????????????????????????????????????
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8)
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4)
        }
        //??????????????????
        File destFile = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        transformJar(destFile, jarInput, context)
    }

    void transformJar(File dest, JarInput jarInput, Context context) {
        def modifiedJar = null
//        println("???????????? jar???" + jarInput.file.absolutePath)
        modifiedJar = modifyJarFile(jarInput.file, context.getTemporaryDir(), dest)
        if (modifiedJar == null) {
            modifiedJar = jarInput.file
        }
//        println("???????????? jar  dest???" + dest.toPath()
//                .toString())
        FileUtils.copyFile(modifiedJar, dest)
    }


    /**
     * ?????? jar ????????????????????????
     */
    private File modifyJarFile(File jarFile, File tempDir, File des) {
        if (jarFile) {
            return modifyJar(jarFile, tempDir, true, des)

        }
        return null
    }


//    private void modifyClassInJar(File jarFile, String target) {
//
//        //?????? jar, verify ????????? false, ????????? jar ????????????????????????
//        def file = new JarFile(jarFile, false)
//        //?????????????????? jar
//        def outputJar = new File(jarFile.getParent(), jarFile.name + ".tmp")
//        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
//        Enumeration enumeration = file.entries()
//
//        while (enumeration.hasMoreElements()) {
//            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
//            InputStream inputStream
//            try {
//                inputStream = file.getInputStream(jarEntry)
//            } catch (Exception e) {
//                IOUtils.closeQuietly(inputStream)
//                e.printStackTrace()
//            }
//            String entryName = jarEntry.getName()
//            JarEntry entry = new JarEntry(entryName)
//            byte[] modifiedClassBytes = null
//            byte[] sourceClassBytes
//            try {
//                jarOutputStream.putNextEntry(entry)
//                sourceClassBytes = TrackUtil.toByteArrayAndAutoCloseStream(inputStream)
//            } catch (Exception e) {
//                System.out.println("Exception encountered while processing jar: " + jarFile.getAbsolutePath())
//                IOUtils.closeQuietly(file)
//                IOUtils.closeQuietly(jarOutputStream)
//                e.printStackTrace()
//            }
//            if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
//                System.out.println("jarEntry---" + jarEntry.name)
//                if (entryName == target) {
//                    modifiedClassBytes = modifyClass(sourceClassBytes, target, false)
//                }
//            }
//            if (modifiedClassBytes == null) {
//                jarOutputStream.write(sourceClassBytes)
//            } else {
//                jarOutputStream.write(modifiedClassBytes)
//            }
//            jarOutputStream.closeEntry()
//        }
//        jarOutputStream.close()
//        file.close()
//        System.out.println("modifyClassInJar- tmp:" + outputJar.toPath().toString() + "---des:" + jarFile.toPath().toString())
//        outputJar.renameTo(jarFile)
//    }


    private File modifyJar(File jarFile, File tempDir, boolean isNameHex, File des) {
        //FIX: ZipException: zip file is empty
        if (jarFile == null || jarFile.length() == 0) {
            return null
        }
        //?????? jar, verify ????????? false, ????????? jar ????????????????????????
        def file = new JarFile(jarFile, false)
        //?????????????????? jar
        def tmpNameHex = ""
        if (isNameHex) {
            tmpNameHex = DigestUtils.md5Hex(jarFile.absolutePath).substring(0, 8)
        }
        def outputJar = new File(tempDir, tmpNameHex + jarFile.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
        Enumeration enumeration = file.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            InputStream inputStream
            try {
                inputStream = file.getInputStream(jarEntry)
            } catch (Exception e) {
                IOUtils.closeQuietly(inputStream)
                e.printStackTrace()
                return null
            }
            String entryName = jarEntry.getName()
            if (entryName.endsWith(".DSA") || entryName.endsWith(".SF")) {
                //ignore
            } else {
                String className
                JarEntry entry = new JarEntry(entryName)
//                if (isTrack && entry.toString().endsWith(".class") && entry.toString() == Repository.ROUTER_HELPER_CLASS) {
//                    Repository.injectJarInputPath = jarFile
//                    Repository.injectJarOutPath = des
//
//                }
                byte[] modifiedClassBytes = null
                byte[] sourceClassBytes
                try {
                    jarOutputStream.putNextEntry(entry)
                    sourceClassBytes = TrackUtil.toByteArrayAndAutoCloseStream(inputStream)
                } catch (Exception e) {
                    System.out.println("Exception encountered while processing jar: " + jarFile.getAbsolutePath())
                    IOUtils.closeQuietly(file)
                    IOUtils.closeQuietly(jarOutputStream)
                    e.printStackTrace()
                    return null
                }
                if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                    className = entryName.replace("/", ".").replace(".class", "")
                    modifiedClassBytes = modifyClass(sourceClassBytes, className, jarFile.absolutePath, des.absolutePath)
                }
                if (modifiedClassBytes == null) {
                    jarOutputStream.write(sourceClassBytes)
                } else {
                    jarOutputStream.write(modifiedClassBytes)
                }
                jarOutputStream.closeEntry()
            }
        }
        jarOutputStream.close()
        file.close()
        return outputJar
    }
    /**
     * ?????????????????????????????????
     */
    private byte[] modifyClass(byte[] srcClass, String className, String srcDir, String desDir) {
        try {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
            ClassVisitor firstVisitor = new TrackClassVisitor(classWriter, srcDir, desDir)
            ClassReader cr = new ClassReader(srcClass)
            cr.accept(firstVisitor, ClassReader.EXPAND_FRAMES + ClassReader.SKIP_FRAMES)
            return classWriter.toByteArray()
        } catch (Exception ex) {
            System.out.println("$className ????????? modifyClass ??????????????????")
            ex.printStackTrace()
            return srcClass
        }
    }
}