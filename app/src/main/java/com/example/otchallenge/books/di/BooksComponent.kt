package com.example.otchallenge.books.di

import com.example.otchallenge.books.data.NYTimesBooksRepository
import com.example.otchallenge.books.domain.BooksRepository
import com.example.otchallenge.books.presentation.BookPresenter
import com.example.otchallenge.books.presentation.BooksActivity
import com.example.otchallenge.books.presentation.BooksPresenterImpl
import com.example.otchallenge.books.presentation.BooksView
import com.example.otchallenge.di.AppComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
internal annotation class BooksScope

@BooksScope
@Component(
    dependencies = [AppComponent::class],
    modules = [BooksModule::class]
)
interface BooksComponent {
    fun inject(activity: BooksActivity)
    fun booksView(): BooksView

    @Component.Builder
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder

        @BindsInstance
        fun booksView(booksView: BooksView): Builder

        fun build(): BooksComponent
    }
}

@Module
interface BooksModule {
    @Binds
    fun bindsRemoteBooksRepository(booksRepository: NYTimesBooksRepository): BooksRepository

    @Binds
    fun bindsBookPresenter(booksPresenter: BooksPresenterImpl): BookPresenter

    @Module
    companion object {
        @Provides
        fun provideBooksService(retrofit: Retrofit): NYTimesBooksRepository.Service =
            retrofit.create(NYTimesBooksRepository.Service::class.java)
    }
}