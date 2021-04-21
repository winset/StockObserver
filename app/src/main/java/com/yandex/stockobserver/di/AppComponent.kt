package com.yandex.stockobserver.di

import com.yandex.stockobserver.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,ViewModelModule::class,NetworkModule::class])
interface AppComponent {
    fun inject(mainFragment: MainFragment)
}