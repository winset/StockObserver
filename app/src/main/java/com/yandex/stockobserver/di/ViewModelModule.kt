package com.yandex.stockobserver.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yandex.stockobserver.repository.CompanyRepository
import com.yandex.stockobserver.repository.CompanyRepositoryImpl
import com.yandex.stockobserver.repository.HoldingRepository
import com.yandex.stockobserver.repository.HoldingRepositoryImpl
import com.yandex.stockobserver.ui.company.CompanyViewModel
import com.yandex.stockobserver.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
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

    @Binds
    abstract fun bindMainRepository(holdingRepositoryImpl: HoldingRepositoryImpl): HoldingRepository

    @Binds
    abstract fun bindCompanyRepository(companyRepositoryImpl: CompanyRepositoryImpl): CompanyRepository
}