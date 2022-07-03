package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDatabase
import com.androiddevs.shoppinglisttestingyt.data.remote.PixabayApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Making sure the application survives the app lifetime
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, "shopping_item_db")
        .build()

    @Provides
    @Singleton
    fun provideShoppingDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Provides
    @Singleton
    fun providePixabayApi() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://pixabay.com")
            .build()
            .create(PixabayApi::class.java)

}