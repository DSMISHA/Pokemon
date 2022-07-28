package com.ds.pokemon

import android.app.Application
import com.ds.pokemon.ioc.koin.integrationModule
import com.ds.pokemon.ioc.koin.presentationModule
import com.ds.pokemon.ioc.koin.useCaseModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.BuildConfig.DEBUG
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

class PokemonApp: Application(), CoroutineScope {

    private val scopeLifecycle = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = scopeLifecycle

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidLogger(if (DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@PokemonApp)
            modules(
                module { single<CoroutineScope> { this@PokemonApp } },
                integrationModule,
                useCaseModule,
                presentationModule
            )
        }
    }
}
