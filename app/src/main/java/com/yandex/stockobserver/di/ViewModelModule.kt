package com.yandex.stockobserver.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yandex.stockobserver.CompanyViewModel
import com.yandex.stockobserver.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule{

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompanyViewModel::class)
    abstract fun bindCompanyViewModel(viewModel: CompanyViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}