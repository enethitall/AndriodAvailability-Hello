// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

allprojects {
    val appConfig: com.android.build.gradle.internal.dsl.BaseAppModuleExtension.()->Unit = {
        compileSdk = 33
        defaultConfig {
            versionCode = 100000
            versionName = "1.0.0"
            targetSdk = 33
        }

        signingConfigs {
            create("signing"){
                storeFile= project.file("hello.jks")
                storePassword= "123456"
                keyAlias = "Hello"
                keyPassword = "123456"
            }

        }

        applicationVariants.all {
            outputs.all {
                val ver = defaultConfig.versionName
                val minSdk = project.extensions.getByType(com.android.build.gradle.internal.dsl.BaseAppModuleExtension::class.java).defaultConfig.minSdk
                val abi = filters.find{it.filterType == "ABI"}?.identifier ?:"all"
                (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                    "${project.name}-$ver-${abi}-sdk$minSdk.apk";
            }
        }

        dependencies {

            add("implementation", "org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
            add("implementation", "androidx.core:core-ktx:1.9.0")
            add("implementation", "androidx.appcompat:appcompat:1.6.1")
            add("implementation", "com.google.android.material:material:1.8.0")
            add("implementation", "androidx.constraintlayout:constraintlayout:2.1.4")
            add("implementation", "com.google.code.gson:gson:2.10.1")

            // 添加本地子模块依赖
//            add("implementation", project(mapOf("path" to ":common")))
        }
    }

    // 应用配置,保存配置到 "extra" 中
    extra["appConfig"]= appConfig

    // 定义任务
    task("incVersion"){
        println("===111111 ===")
        group = "android"
        doFirst {
            projectIncVersion(project)
        }
    }

    task("releaseApk") {
        println("===222222 ===")
        dependsOn("assembleRelease","incVersion")
        group = "android"
        doLast {
            // 查找build Apk的文件
            val releaseDir = File(project.buildDir.absolutePath , "/outputs/apk/release/")
            val apkFiles = releaseDir.listFiles()?.filter {
                it.extension == "apk"
            }
            // 移动所有的apk到公共的发布目录。
            val outputDir = File(File(project.rootDir, "release"), project.name)
            if(outputDir.exists()){
                outputDir.deleteRecursively()
            }
            outputDir.mkdirs()
            apkFiles?.forEach {
                val toFile = File(outputDir, it.name)
                it.renameTo(toFile)
                println(toFile.absolutePath)
            }
            println("=== released apk:${project.name} ===")
        }
    }
    task("wei_wei") {
        group = "android"
        println("测试路径===${project.file("hello.jks")}")
    }
}

/**
 * 读取子模块的版本名称。
 */
fun  projectVersionName(project:Project):String{
    val versionFile = project.file("version.properties")
    var ver = "1.0.0"
    if(versionFile.canRead()){
        val prop = java.util.Properties()
        versionFile.inputStream().use {
            prop.load(it)
            if(prop["VERSION"] != null){
                ver = prop["VERSION"] as String
            }
        }
    }
    return ver
}
/**
 * 读取子模块的版本数字
 */
fun  projectVersionCode(project:Project):Int{
    val ver = projectVersionName(project);
    val sp = ver.split(".")
    val v1 = Integer.parseInt(sp[0])
    val v2 = Integer.parseInt(sp[1])
    val v3 = Integer.parseInt(sp[2])

    return v1 * 100000 + v2 * 1000 + v3
}

// 增加子模块的版本号
fun projectIncVersion(project:Project):String{
    val ver = projectVersionName(project);
    val sp = ver.split(".")
    val v1 = Integer.parseInt(sp[0])
    val v2 = Integer.parseInt(sp[1])
    val v3 = Integer.parseInt(sp[2])+1
    val newVer = String.format("%d.%d.%d", v1, v2, v3)
    val prop = java.util.Properties()
    val versionFile = project.file("version.properties")
    versionFile.writer().use {
        prop["VERSION"] = newVer
        prop.store(it,null)
    }
    return newVer
}