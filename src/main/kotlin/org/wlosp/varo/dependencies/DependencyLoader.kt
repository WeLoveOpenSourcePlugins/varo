package org.wlosp.varo.dependencies

import net.byteflux.libby.BukkitLibraryManager
import net.byteflux.libby.Library

fun BukkitLibraryManager.loadDependencies(vararg dependencies: Dependency) {
    dependencies.forEach { dependency ->
        dependency.repository?.let { addRepository(it) }
        addJCenter()
        val library = Library.builder()
            .groupId(dependency.groupId)
            .artifactId(dependency.artifactId)
            .version(dependency.version)
            .build()
        loadLibrary(library)
    }
}