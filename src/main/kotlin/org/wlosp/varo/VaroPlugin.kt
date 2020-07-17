package org.wlosp.varo

import net.byteflux.libby.BukkitLibraryManager
import org.bukkit.plugin.java.JavaPlugin
import org.wlosp.varo.commands.registerVaroCommand
import org.wlosp.varo.configuration.Config
import org.wlosp.varo.database.connectToDatabase
import org.wlosp.varo.dependencies.Dependency
import org.wlosp.varo.dependencies.loadDependencies
import org.wlosp.varo.listeners.registerDamageListener
import org.wlosp.varo.listeners.registerItemBlockListeners
import org.wlosp.varo.listeners.registerShutdownHandler
import org.wlosp.varo.listeners.registerTeamHandler
import kotlin.properties.Delegates

/**
 * Plugin main class.
 */
@Suppress("unused")
class VaroPlugin : JavaPlugin() {
    internal var dependencyManager by Delegates.notNull<BukkitLibraryManager>()
    internal var varoConfig by Delegates.notNull<Config>()

    override fun onEnable() {
        dependencyManager = BukkitLibraryManager(this)
        dependencyManager.loadDependencies(
            Dependency.SPIGLIN,
            Dependency.COROUTINES,
            Dependency.EXPOSED,
            Dependency.OKHTTP
        )

        saveDefaultConfig()
        loadConfig()

        connectToDatabase()

        // Events
        registerDamageListener()
        registerShutdownHandler()
        registerItemBlockListeners()
        registerTeamHandler()

        registerVaroCommand()
    }

    private fun loadConfig() {
        varoConfig = Config.fromFileConfiguration(config)
    }

}
