package com.example.compiler

import com.example.annotation.Router
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("moduleName")
@SupportedAnnotationTypes("com.example.annotation.Router")
class RouterProcessor : AbstractProcessor() {

    // 打印日志工具类
    private lateinit var mMessage: Messager

    // 文件操作类，我们将通过此类生成kotlin文件
    private lateinit var mFiler: Filer

    // 类型工具类，处理Element的类型
    private lateinit var mTypeTools: Types

    private lateinit var mElementUtils: Elements

    private var mModuleName: String? = null

//    private val mPathMap = mutableMapOf<String, MutableList<RouteBean>>()

    // 生成类的包名
    private val mGeneratePackage = "com.example.router.generate"

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        if (processingEnv == null) return
        mMessage = processingEnv.messager
        mFiler = processingEnv.filer
        mElementUtils = processingEnv.elementUtils
        mTypeTools = processingEnv.typeUtils

//        mModuleName = processingEnv.getOptions().get(ProcessorConfig.OPTIONS);
//
//        mMessage.printMessage(Diagnostic.Kind.NOTE, "processor 初始化完成.....${mModuleName}")
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?
    ): Boolean {

        val elements = roundEnv?.getElementsAnnotatedWith(Router::class.java)
        elements?.forEach { element ->

            val superInter = ClassName("com.example.common.router", "IRouter")

            val activity = ClassName("android.app","Activity")


            val greeterClass = ClassName(mElementUtils.getPackageOf(element).qualifiedName.toString(), "${element.simpleName}")

            val key = element.getAnnotation(Router::class.java).path

            val initFun = FunSpec.builder("load")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("routerMap",
                    HashMap::class.java.asClassName().parameterizedBy(
                        String::class.java.asTypeName().javaToKotlinType(),
                        Class::class.java.asClassName().parameterizedBy(
                            WildcardTypeName.producerOf(activity.javaToKotlinType())).javaToKotlinType()))
                .addStatement("routerMap[\"$key\"]= %T::class.java",greeterClass)
                .build()

            val type =  TypeSpec.classBuilder("${element.simpleName}Router")
                .addSuperinterface(superInter)
                .addFunction(initFun)
                .build()

            FileSpec.builder(mGeneratePackage, "${element.simpleName}Router")
                .addType(type)
                .build()
                .writeTo(mFiler)
        }
        return true
    }

}