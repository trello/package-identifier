package com.trello.identifier.plugin

import com.squareup.kotlinpoet.*
import com.trello.identifier.annotation.PackageId

class PackageIdentifierCompiler {
    fun createPackageIdentifierFile(packageName: String,
                                    className: String,
                                    isDebuggable: Boolean): FileSpec {

        val pId = AnnotationSpec.builder(PackageId::class)
                .apply { addMember("isDebug = %L", isDebuggable) }
                .build()
        val id = TypeSpec.classBuilder(ClassName(packageName, className))
                .addModifiers(KModifier.PUBLIC)
                .addAnnotation(pId)
                .build()

        return FileSpec.builder(packageName, "PackageIdentifier")
                .addType(id)
                .addComment("Generated, do not modify!")
                .build()
    }
}