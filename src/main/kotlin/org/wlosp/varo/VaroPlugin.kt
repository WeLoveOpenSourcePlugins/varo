package org.wlosp.varo

import net.byteflux.libby.BukkitLibraryManager
import org.bukkit.plugin.java.JavaPlugin
import org.wlosp.varo.configuration.Config
import org.wlosp.varo.dependencies.Dependency
import org.wlosp.varo.dependencies.loadDependencies
import org.wlosp.varo.listeners.registerDamageListener
import org.wlosp.varo.listeners.registerItemBlockListeners
import org.wlosp.varo.listeners.registerShutdownHandler
import kotlin.properties.Delegates

/**
 * Plugin main class.
 */
@Suppress("unused")
class VaroPlugin : JavaPlugin() {
    private var dependencyManager by Delegates.notNull<BukkitLibraryManager>()
    var varoConfig by Delegates.notNull<Config>()

    override fun onEnable() {
        dependencyManager = BukkitLibraryManager(this)
        dependencyManager.loadDependencies(Dependency.SPIGLIN, Dependency.COROUTINES)

        saveDefaultConfig()
        loadConfig()

        // Events
        registerDamageListener()
        registerShutdownHandler()
        registerItemBlockListeners()
    }

    private fun loadConfig() {
        varoConfig = Config.fromFileConfiguration(getConfig())
        println(varoConfig)
    }

}
