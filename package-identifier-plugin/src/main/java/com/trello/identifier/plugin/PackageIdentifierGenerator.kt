package com.trello.identifier.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class PackageIdentifierGenerator : DefaultTask() {
  @get:OutputDirectory
  var outputDir: File? = null

  @get:Input
  var isDebuggable: Boolean? = null

  @get:Input
  var packageName: String? = null

  @get:Input
  var className: String? = null

  @Suppress("unused") // Invoked by Gradle.
  @TaskAction
  fun brewJava() {
    brewJava(isDebuggable!!, outputDir!!, packageName!!, className!!)
  }
}

fun brewJava(
    isDebuggable: Boolean,
    outputDir: File,
    packageName: String,
    className: String
) {
  val compiler = PackageIdentifierCompiler()
  compiler.createPackageIdentifierFile(packageName, className, isDebuggable).writeTo(outputDir)
}