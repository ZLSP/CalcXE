package com.zlsp.calcxe.main

import android.app.Application
import com.zlsp.calcxe.data.helper.AssetJsonHelper
import com.zlsp.calcxe.data.mapper.MainMapper
import com.zlsp.calcxe.data.repository.MainRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class CalcXeApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CalcXeApp)
            modules(
                module {
                    singleOf(::MainRepository)
                    singleOf(::AssetJsonHelper)
                    singleOf(::MainMapper)
                    viewModelOf(::MainViewModel)
                }
            )
        }
    }
}