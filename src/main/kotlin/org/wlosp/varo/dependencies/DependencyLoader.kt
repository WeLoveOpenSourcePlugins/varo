package org.wlosp.varo.dependencies

import net.byteflux.libby.BukkitLibraryManager
import net.byteflux.libby.Library
import net.byteflux.libby.LibraryManager

fun BukkitLibraryManager.loadDependencies(vararg dependencies: Dependency) {
    addJCenter()
    addMavenCentral()
    dependencies.forEach(::loadDependency)
}

fun LibraryManager.loadDependency(dependency: Dependency) {
    dependency.dependencies.forEach(::loadDependency)
    dependency.repository?.let { addRepository(it) }
    val library = Library.builder()
        .groupId(dependency.groupId)
        .artifactId(dependency.artifactId)
        .version(dependency.version)
        .build()
    loadLibrary(library)
}
