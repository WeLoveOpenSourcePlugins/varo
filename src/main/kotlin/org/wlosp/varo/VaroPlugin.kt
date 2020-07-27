package org.wlosp.varo

import net.byteflux.libby.BukkitLibraryManager
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.exposed.sql.transactions.transaction
import org.wlosp.varo.api.VaroApiImpl
import org.wlosp.varo.commands.registerVaroCommand
import org.wlosp.varo.configuration.Config
import org.wlosp.varo.database.connectToDatabase
import org.wlosp.varo.dependencies.Dependency
import org.wlosp.varo.dependencies.loadDependencies
import org.wlosp.varo.entities.Stage
import org.wlosp.varo.entities.VaroStorage
import org.wlosp.varo.listeners.*
import org.wlosp.varo.listeners.registerItemBlockListeners
import org.wlosp.varo.listeners.registerShutdownHandler
import kotlin.properties.Delegates

/**
 * Plugin main class.
 */
@Suppress("unused")
class VaroPlugin : JavaPlugin() {
    internal var dependencyManager by Delegates.notNull<BukkitLibraryManager>()
    internal var varoConfig by Delegates.notNull<Config>()
    internal var stage: Stage by Delegates.observable(Stage.SETUP) { _, _, it ->
        transaction {
            VaroStorage.storage.stage = it
        }
    }

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
        registerStartupSequence()

        registerVaroCommand()

        VaroApiImpl()
    }

    private fun loadConfig() {
        varoConfig = Config.fromFileConfiguration(config)
    }

}
