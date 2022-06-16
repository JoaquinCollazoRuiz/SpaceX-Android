package com.joaquincollazoruiz.spacex.di

import android.content.Context
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import coil.ImageLoader
import coil.request.ImageRequest
import com.joaquincollazoruiz.spacex.BuildConfig
import com.joaquincollazoruiz.spacex.R
import com.joaquincollazoruiz.spacex.data.local.AppDB
import com.joaquincollazoruiz.spacex.data.remote.IFetchLaunchesService
import com.joaquincollazoruiz.spacex.domain.interfaces.ILaunchesRepository
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.usecase.IGetLaunchesUseCase
import com.joaquincollazoruiz.spacex.domain.usecase.impl.GetLaunchesUseCase
import com.joaquincollazoruiz.spacex.presentation.LaunchesViewModel
import com.joaquincollazoruiz.spacex.presentation.recyclerview.LaunchesAdapter
import com.joaquincollazoruiz.spacex.presentation.recyclerview.LaunchesDiffUtilCallback
import com.joaquincollazoruiz.spacex.repository.LaunchesRepository
import com.joaquincollazoruiz.spacex.repository.caching.LaunchesCachingConfig
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class KoinModules {
    companion object {

        private const val DB_IDENTIFIER = "spacex_app_db"

        /** 60 second launches cache **/
        private const val LAUNCHES_CACHE_VALIDITY_IN_MILLIS = 60000L

        private fun presentationModule(): Module = module {
            viewModel { LaunchesViewModel(get()) }

            factory { LaunchesAdapter(get(), get(), get(), get()) }
            factory<DiffUtil.ItemCallback<Launch>> { LaunchesDiffUtilCallback() }
            factory<AsyncDifferConfig<Launch>> { AsyncDifferConfig.Builder<Launch>(get()).build() }
            factory { ImageRequest.Builder(get<Context>()) }
            single {
                ImageLoader.Builder(get())
                    .error(R.drawable.icon_rocket)
                    .placeholder(R.drawable.icon_rocket)
                    .fallback(R.drawable.icon_rocket)
                    .crossfade(200)
                    .build()
            }
        }

        private fun domainModule(): Module = module {
            factory<IGetLaunchesUseCase> { GetLaunchesUseCase(get()) }
        }

        private fun repositoryModule(): Module = module {
            factory { LaunchesCachingConfig(launchesCacheValidityInMillis = LAUNCHES_CACHE_VALIDITY_IN_MILLIS) }
            factory<ILaunchesRepository> { LaunchesRepository(get(), get(), get(), get()) }
            factory { com.joaquincollazoruiz.spacex.repository.caching.ICurrentUnixTimeProvider { System.currentTimeMillis() } }
        }

        private fun dataModule(): Module = module {
            single { Room.databaseBuilder(get(), AppDB::class.java, DB_IDENTIFIER).build() }
            factory<IFetchLaunchesService> { get<Retrofit>().create(IFetchLaunchesService::class.java) }

            factory<Moshi> {
                Moshi.Builder()
                    .build()
            }

            factory<MoshiConverterFactory> { MoshiConverterFactory.create(get()) }

            factory {
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.NONE
                }
            }
            factory {
                OkHttpClient.Builder()
                    .addInterceptor(get<HttpLoggingInterceptor>())
                    .build()
            }

            single<Retrofit> {
                Retrofit.Builder()
                    .client(get())
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(get<MoshiConverterFactory>())
                    .build()
            }
        }

        fun all(): List<Module> =
            presentationModule() + domainModule() + repositoryModule() + dataModule()
    }
}