### buildSrc插件作用

1. 自动收集每一个模块中apt生成的辅助类(实现了com.example.jrouterapi.IRouteModule接口)

2. 调用com.example.jrouterapi.core.JRouteHelper的injectRouteModuleByPlugin()方法，注入实例对象

插入后的效果如下：

```
   public static void injectRouteModuleByPlugin() {
        register(new home_module());
        register(new login_module());
    }
```

#### 使用Transform解析class文件的过程

在解析class文件的时候，有3种范围可选择

1. 当前模块    
2. 子模块   项目中所有参与构建的子模块
3. 第三方依赖库

对当前模块中class文件的解析以文件的方式进行处理，生成的

输入路径-》
>srcDir:JRouter/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes

输出路径-》
>JRouter/app/build/intermediates/transforms/RouterTransform/debug/7

对于子模块的解析，输入和输出都是.jar的形式

输入路径-》
>srcDir:JRouter/jrouterapi/build/intermediates/runtime_library_classes/debug/classes.jar

输出路径-》
>JRouter/app/build/intermediates/transforms/RouterTransform/debug/2.jar

对于子模块，一般jar的输入在子模块的intermediates文件夹中，而输出在app的intermediates文件夹中。

#### RouterTransform具体的遍历流程

1. 遍历当前模块的class和所有子模块的jar，找到实现了com.example.jrouterapi.IRouteModule接口的class的路径；同时存储com.example.jrouterapi.core.JRouteHelper的class所在的路径信息，可能是一个文件夹(当前模块)，也可能是在jar中。

2. 根据第一步保存的路径信息的格式，决定第二次遍历当前模块还是所有的jar，如果是jar则要修改jar中com.example.jrouterapi.core.JRouteHelper的class文件，并将整个jar写入到输出目录。




