package com.example.otchallenge.di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.care.sdk.utils.StringResourceProxy
import com.care.sdk.utils.StringResources
import com.example.otchallenge.Database
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Locale
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface AppComponent {

	@NYTimesApiKey
	fun nyTimesApiKey(): String

	@BaseUrl
	fun baseUrl(): String

	fun retrofit(): Retrofit

	fun locale(): Locale

	fun glide(): RequestManager

	fun stringResources(): StringResources

	fun sqlDatabase(): Database

	fun scheduler(): Scheduler

	@Component.Builder
	interface Builder {
		@BindsInstance
		fun baseUrl(@BaseUrl baseUrl: String): Builder

		@BindsInstance
		fun context(context: Context): Builder

		@BindsInstance
		fun nyTimesApiKey(@NYTimesApiKey apiKey: String): Builder

		fun build(): AppComponent
	}
}

@Module
interface ApplicationModule {

	@Binds
	fun bindsStringResource(stringResources: StringResourceProxy): StringResources

	@Module
	companion object {
		@Provides
		fun providesRetrofit(@BaseUrl baseUrl: String): Retrofit {
			return Retrofit.Builder()
				.baseUrl(baseUrl)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(MoshiConverterFactory.create())
				.build()
		}

		@Provides
		fun providesLocale(): Locale = Locale.getDefault()

		@Provides
		fun providesGlide(context: Context) = Glide.with(context)

		@Provides
		fun provideSQLDatabase(context: Context): Database = Database(
			AndroidSqliteDriver(Database.Schema, context, "books.db")
		)

		@Provides
		fun providesMainScheduler(): Scheduler = AndroidSchedulers.mainThread()
	}
}
