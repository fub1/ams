package com.crty.ams.pda.utils.rfid.di//// File: AppModule.kt
//package com.xyx.seuicpda.utils.rfid.di
//
//import android.app.Application
//import android.content.Context
//import com.crty.ams.pda.utils.rfid.RfidScanManager
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideContext(application: Application): Context {
//        return application.applicationContext
//    }
//
//    @Provides
//    @Singleton
//    fun provideRfidScanManager(context: Context): RfidScanManager {
//        return RfidScanManager()
//    }
//}