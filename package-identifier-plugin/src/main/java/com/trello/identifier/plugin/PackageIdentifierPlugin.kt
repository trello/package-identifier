package com.trello.identifier.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.FeatureExtension
import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.trello.identifier.VERSION
import groovy.util.XmlSlurper
import org.gradle.api.Action
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.KClass

/**
 * Based on butterknife's multi-module gradle plugin
 */
class PackageIdentifierPlugin : Plugin<Project> {
  private val log by lazy { LoggerFactory.getLogger("PackageIdentifier") }

  override fun apply(project: Project) {
    project.plugins.all {
      when (it) {
        is FeaturePlugin -> {
          project.extensions[FeatureExtension::class].run {
            configurePackageIdentifierGeneration(project, featureVariants)
            configurePackageIdentifierGeneration(project, libraryVariants)
          }
        }
        is LibraryPlugin -> {
          project.extensions[LibraryExtension::class].run {
            configurePackageIdentifierGeneration(project, libraryVariants)
          }
        }
        is AppPlugin -> {
          project.extensions[AppExtension::class].run {
            configurePackageIdentifierGeneration(project, applicationVariants)
          }
        }
      }
    }
  }

  // Parse the variant's main manifest file in order to get the package id which is used to create
  // Sanitizations.kt in the right place.
  private fun getPackageName(variant: BaseVariant): String {
    val slurper = XmlSlurper(false, false)
    val list = variant.sourceSets.map { it.manifestFile }

    // According to the documentation, the earlier files in the list are meant to be overridden by the later ones.
    // So the first file in the sourceSets list should be main.
    val result = slurper.parse(list[0])
    return result.getProperty("@package").toString()
  }

  private fun configurePackageIdentifierGeneration(project: Project, variants: DomainObjectSet<out BaseVariant>) {
    val implDeps = project.configurations.getByName("implementation").dependencies
    implDeps.add(project.dependencies.create("com.trello.identifier:package-identifier-annotations:$VERSION"))
    variants.all { variant ->
      val once = AtomicBoolean()
      variant.outputs.all { output ->
        if (once.compareAndSet(false, true)) {
          val packageName = getPackageName(variant)
          val outputDir = project.buildDir.resolve("generated/source/package-identifier/${variant.name}")
          project.tasks.create("generate${variant.name.capitalize()}PackageInfo", PackageIdentifierGenerator::class.java)
          {
            it.outputDir = outputDir
            it.className = "PackageIdentifier"
            it.packageName = packageName
            it.isDebuggable = variant.buildType.isDebuggable
            variant.registerJavaGeneratingTask(it, outputDir)
            variant.addJavaSourceFoldersToModel(outputDir)
          }
        }
      }
    }
  }

}

private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T {
  return getByType(type.java)!!
}
