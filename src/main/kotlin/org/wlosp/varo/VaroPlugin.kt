package org.wlosp.varo

import net.byteflux.libby.BukkitLibraryManager
import org.bukkit.plugin.java.JavaPlugin
import org.wlosp.varo.configuration.Config
import org.wlosp.varo.dependencies.Dependency
import org.wlosp.varo.dependencies.loadDependencies
import kotlin.properties.Delegates

/**
 * Plugin main class.
 */
@Suppress("unused")
class VaroPlugin : JavaPlugin() {
    private var dependencyManager by Delegates.notNull<BukkitLibraryManager>()
    private var config by Delegates.notNull<Config>()

    override fun onEnable() {
        dependencyManager = BukkitLibraryManager(this)
        dependencyManager.loadDependencies(Dependency.SPIGLIN, Dependency.COROUTINES)

        saveDefaultConfig()
        loadConfig()
    }

    private fun loadConfig() {
        config = Config.fromFileConfiguration(getConfig())
        println(config)
    }

}
