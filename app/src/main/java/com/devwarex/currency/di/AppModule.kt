package com.devwarex.currency.di

import android.content.Context
import com.devwarex.currency.R
import com.devwarex.currency.api.CurrencyClient
import com.devwarex.currency.api.CurrencyService
import com.devwarex.currency.datastore.DatastoreImpl
import com.devwarex.currency.db.AppDao
import com.devwarex.currency.db.AppRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun getInstanceApisClientService(): CurrencyService = CurrencyClient.create()

    @Provides
    @Singleton
    fun getInstanceRoomDBDao(@ApplicationContext context: Context): AppDao = AppRoomDatabase.getAppDatabase(context = context).appDao()

    @Provides
    @Singleton
    fun getInstanceDatastore(@ApplicationContext context: Context): DatastoreImpl = DatastoreImpl.create(context = context)

    @Provides
    @Singleton
    @NamedApiKey
    fun getCurrencyApiKey(@ApplicationContext context: Context): String = context.getString(R.string.api_key)

}