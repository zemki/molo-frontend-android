package news.molo.android.di

import android.location.Geocoder
import de.convelop.forge.datastore.DataStore
import de.convelop.forge.event.DefaultEventBus
import de.convelop.forge.event.EventBus
import de.convelop.forge.navigation.NavigationDispatcher
import de.convelop.forge.network.client.HttpClient
import de.convelop.forge.network.ktor.KtorApiClient
import de.convelop.forge.network.logging.HttpLogLevel
import news.molo.android.BuildConfig
import news.molo.android.navigation.ChannelNavigationDispatcher
import news.molo.android.presentation.MainViewModel
import news.molo.core.event.UIEvent
import news.molo.data.database.AppDatabase
import news.molo.data.database.dao.BookmarkedArticleIdDao
import news.molo.data.database.dao.BookmarkedEventDao
import news.molo.data.database.dao.SearchHistoryDao
import news.molo.data.database.dao.TagDao
import news.molo.data.datastore.EncryptedDataStore
import news.molo.data.firebase.FirebaseDeviceIdProvider
import news.molo.data.firebase.FirebasePushTokenProvider
import news.molo.data.repository.AreaRepositoryImpl
import news.molo.data.repository.ArticleRepositoryImpl
import news.molo.data.repository.AuthRepositoryImpl
import news.molo.data.repository.EventRepositoryImpl
import news.molo.data.repository.OrganizationRepositoryImpl
import news.molo.data.repository.SearchHistoryRepositoryImpl
import news.molo.data.repository.SessionRepositoryImpl
import news.molo.data.repository.TagRepositoryImpl
import news.molo.data.repository.UserRepositoryImpl
import news.molo.data.storage.LocalFileProvider
import news.molo.domain.model.TabSelectionType
import news.molo.domain.provider.DeviceIdProvider
import news.molo.domain.provider.FileProvider
import news.molo.domain.provider.PushTokenProvider
import news.molo.domain.repository.AreaRepository
import news.molo.domain.repository.ArticleRepository
import news.molo.domain.repository.AuthRepository
import news.molo.domain.repository.EventRepository
import news.molo.domain.repository.OrganizationRepository
import news.molo.domain.repository.SearchHistoryRepository
import news.molo.domain.repository.SessionRepository
import news.molo.domain.repository.TagRepository
import news.molo.domain.repository.UserRepository
import news.molo.domain.usecase.apperance.ObserveColorSchemaUseCase
import news.molo.domain.usecase.apperance.SetColorSchemaUseCase
import news.molo.domain.usecase.area.GetAreasByDistanceUseCase
import news.molo.domain.usecase.area.GetAreasUseCase
import news.molo.domain.usecase.area.GetSelectedAreaUseCase
import news.molo.domain.usecase.area.GetSelectedAreaWithRadiusUseCase
import news.molo.domain.usecase.area.ObserveAreaAndRadiusSelectionUseCase
import news.molo.domain.usecase.area.SelectAreaUseCase
import news.molo.domain.usecase.article.BookmarkArticleUseCase
import news.molo.domain.usecase.article.CreateArticleUseCase
import news.molo.domain.usecase.article.DeletePostedArticleUseCase
import news.molo.domain.usecase.article.FlagArticleUseCase
import news.molo.domain.usecase.article.GenerateArticleTagsUseCase
import news.molo.domain.usecase.article.GetArticleCreationDataUseCase
import news.molo.domain.usecase.article.GetArticleDetailsUseCase
import news.molo.domain.usecase.article.GetArticleTagsUseCase
import news.molo.domain.usecase.article.GetArticleUpdateDataUseCase
import news.molo.domain.usecase.article.GetMyPaginatedArticlePostsUseCase
import news.molo.domain.usecase.article.GetPaginatedArticleUseCaseIgnoringUserSelection
import news.molo.domain.usecase.article.GetPaginatedArticlesForOrganizationUseCase
import news.molo.domain.usecase.article.GetPaginatedArticlesWithQueryUseCase
import news.molo.domain.usecase.article.GetPaginatedArticlesWithTags
import news.molo.domain.usecase.article.GetPaginatedBookmarkedArticlesUseCase
import news.molo.domain.usecase.article.ObserveArticleBookmarksUseCase
import news.molo.domain.usecase.article.RemoveArticleBookmarkUseCase
import news.molo.domain.usecase.article.UpdateArticleTagSelectionUseCase
import news.molo.domain.usecase.article.UpdateArticleUseCase
import news.molo.domain.usecase.event.BookmarkEventUseCase
import news.molo.domain.usecase.event.CreateEventUseCase
import news.molo.domain.usecase.event.DeletePostedEventUseCase
import news.molo.domain.usecase.event.FlagEventUseCase
import news.molo.domain.usecase.event.GenerateEventTagsUseCase
import news.molo.domain.usecase.event.GetEventCreationDataUseCase
import news.molo.domain.usecase.event.GetEventDetailsUseCase
import news.molo.domain.usecase.event.GetEventTagsUseCase
import news.molo.domain.usecase.event.GetEventUpdateDataUseCase
import news.molo.domain.usecase.event.GetMyPaginatedEventPostsUseCase
import news.molo.domain.usecase.event.GetPaginatedBookmarkedEventsUseCase
import news.molo.domain.usecase.event.GetPaginatedEventsForOrganizationUseCase
import news.molo.domain.usecase.event.GetPaginatedEventsUseCase
import news.molo.domain.usecase.event.GetPaginatedEventsWithQueryUseCase
import news.molo.domain.usecase.event.GetPaginatedUpcomingEventsForOrganizationUseCase
import news.molo.domain.usecase.event.ObserveEventBookmarksUseCase
import news.molo.domain.usecase.event.ObserveEventCategoriesWithEventsUseCase
import news.molo.domain.usecase.event.RemoveEventBookmarkUseCase
import news.molo.domain.usecase.event.UpdateEventTagSelectionUseCase
import news.molo.domain.usecase.event.UpdateEventUseCase
import news.molo.domain.usecase.onboarding.MarkOnboardingAsCompletedUseCase
import news.molo.domain.usecase.onboarding.ObserveOnboardingCompletionUseCase
import news.molo.domain.usecase.organization.DisableOrganizationUseCase
import news.molo.domain.usecase.organization.EnableOrganizationUseCase
import news.molo.domain.usecase.organization.GetOrganizationSummaryUseCase
import news.molo.domain.usecase.organization.ObserveOrganizationUseCase
import news.molo.domain.usecase.posting.ObservePostingReloadTriggerUseCase
import news.molo.domain.usecase.search.DeleteSearchHistoryEntryUseCase
import news.molo.domain.usecase.search.DeleteWholeSearchHistoryUseCase
import news.molo.domain.usecase.search.ObserveSearchHistoryUseCase
import news.molo.domain.usecase.search.StoreSearchHistoryEntryUseCase
import news.molo.domain.usecase.tag.ObserveTagSelectionUseCase
import news.molo.domain.usecase.user.DeleteSelfUserUseCase
import news.molo.domain.usecase.user.LoginUseCase
import news.molo.domain.usecase.user.LogoutUseCase
import news.molo.domain.usecase.user.ObserveIsUserLoggedInUseCase
import news.molo.domain.usecase.user.ObserveProfileReloadTriggerUseCase
import news.molo.domain.usecase.user.ObserveSelfUserUseCase
import news.molo.domain.usecase.user.PostNewPushTokenUseCase
import news.molo.domain.usecase.user.RegisterUserUseCase
import news.molo.domain.usecase.user.RequestPasswordResetUseCase
import news.molo.domain.usecase.user.ResetPasswordUseCase
import news.molo.domain.usecase.user.SetBearerUseCase
import news.molo.domain.usecase.user.UpdateUserProfileUseCase
import news.molo.feature.area.presentation.AreaSelectionViewModel
import news.molo.feature.article.detail.presentation.DetailedArticleViewModel
import news.molo.feature.auth.login.presentation.LoginViewModel
import news.molo.feature.auth.registration.presentation.RegistrationViewModel
import news.molo.feature.auth.reset.presentation.PasswordResetViewModel
import news.molo.feature.auth.reset.request.presentation.RequestPasswordResetViewModel
import news.molo.feature.bookmark.presentation.BookmarksViewModel
import news.molo.feature.event.detail.presentation.DetailedEventViewModel
import news.molo.feature.event.presentation.EventsViewModel
import news.molo.feature.feed.presentation.FeedViewModel
import news.molo.feature.info.presentation.InfoViewModel
import news.molo.feature.location.presentation.LocationViewModel
import news.molo.feature.onboarding.presentation.OnboardingViewModel
import news.molo.feature.organization.presentation.DetailedOrganizationViewModel
import news.molo.feature.posting.article.creation.presentation.ArticleCreationViewModel
import news.molo.feature.posting.event.creation.presentation.EventCreationViewModel
import news.molo.feature.profile.presentation.ProfileViewModel
import news.molo.feature.search.result.presentation.SearchResultViewModel
import news.molo.feature.source.presentation.OrganizationSummaryViewModel
import news.molo.feature.tags.presentation.TagSelectionViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single<HttpClient> {
        KtorApiClient {
            if (BuildConfig.DEBUG) {
                logger(level = HttpLogLevel.All)
            }
        }.client
    }

    single<DataStore> { EncryptedDataStore(get()) }
    single<DeviceIdProvider> { FirebaseDeviceIdProvider(get()) }
    single<AreaRepository> { AreaRepositoryImpl(get()) }
    single<ArticleRepository> { ArticleRepositoryImpl(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<EventRepository> { EventRepositoryImpl(get(), get()) }
    single<OrganizationRepository> { OrganizationRepositoryImpl(get()) }
    single<TagRepository> { TagRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SessionRepository> { SessionRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }

    single<AppDatabase> { AppDatabase.create(get()) }
    single<BookmarkedArticleIdDao> { get<AppDatabase>().bookmarkedArticleDao() }
    single<BookmarkedEventDao> { get<AppDatabase>().bookmarkedEventsDao() }
    single<TagDao> { get<AppDatabase>().tagDao() }
    single<SearchHistoryDao> { get<AppDatabase>().searchHistoryDao() }

    single<FileProvider> { LocalFileProvider(get()) }
    single<PushTokenProvider> { FirebasePushTokenProvider(get()) }

    // Appearance
    factory { ObserveColorSchemaUseCase(get()) }
    factory { SetColorSchemaUseCase(get()) }

    // Areas
    factory { GetAreasByDistanceUseCase(get(), get()) }
    factory { GetAreasUseCase(get(), get()) }
    factory { GetSelectedAreaUseCase(get(), get(), get()) }
    factory { GetSelectedAreaWithRadiusUseCase(get()) }
    factory { ObserveAreaAndRadiusSelectionUseCase(get()) }
    factory { SelectAreaUseCase(get(), get(), get()) }

    // Article
    factory { BookmarkArticleUseCase(get(), get()) }
    factory { CreateArticleUseCase(get(), get(), get(), get()) }
    factory { DeletePostedArticleUseCase(get(), get(), get()) }
    factory { FlagArticleUseCase(get(), get()) }
    factory { GenerateArticleTagsUseCase(get(), get()) }
    factory { GetArticleDetailsUseCase(get(), get()) }
    factory { GetArticleTagsUseCase(get(), get()) }
    factory { GetMyPaginatedArticlePostsUseCase(get(), get(), get()) }
    factory { GetPaginatedArticlesForOrganizationUseCase(get(), get()) }
    factory { GetPaginatedArticleUseCaseIgnoringUserSelection(get(), get()) }
    factory { GetPaginatedBookmarkedArticlesUseCase(get(), get()) }
    factory { ObserveArticleBookmarksUseCase(get()) }
    factory { RemoveArticleBookmarkUseCase(get(), get()) }
    factory { UpdateArticleTagSelectionUseCase(get(), get()) }
    factory { UpdateArticleUseCase(get(), get(), get(), get()) }
    factory { GetPaginatedArticlesWithTags(get(), get()) }
    factory { GetArticleCreationDataUseCase(get(), get(), get(), get()) }
    factory { GetArticleUpdateDataUseCase(get(), get(), get()) }
    factory { GetPaginatedArticlesWithQueryUseCase(get(), get()) }

    // Event
    factory { BookmarkEventUseCase(get(), get()) }
    factory { CreateEventUseCase(get(), get(), get(), get()) }
    factory { DeletePostedEventUseCase(get(), get(), get()) }
    factory { FlagEventUseCase(get(), get()) }
    factory { GenerateEventTagsUseCase(get(), get()) }
    factory { ObserveEventCategoriesWithEventsUseCase(get(), get(), get(), get()) }
    factory { GetEventDetailsUseCase(get(), get()) }
    factory { GetEventTagsUseCase(get(), get()) }
    factory { GetMyPaginatedEventPostsUseCase(get(), get(), get()) }
    factory { GetPaginatedBookmarkedArticlesUseCase(get(), get()) }
    factory { GetPaginatedEventsForOrganizationUseCase(get(), get()) }
    factory { GetPaginatedEventsUseCase(get(), get()) }
    factory { GetPaginatedBookmarkedEventsUseCase(get(), get()) }

    factory { GetPaginatedUpcomingEventsForOrganizationUseCase(get(), get()) }
    factory { ObserveEventBookmarksUseCase(get()) }
    factory { RemoveEventBookmarkUseCase(get(), get()) }
    factory { UpdateEventTagSelectionUseCase(get(), get()) }
    factory { UpdateEventUseCase(get(), get(), get(), get()) }
    factory { GetEventUpdateDataUseCase(get(), get(), get()) }
    factory { GetEventCreationDataUseCase(get(), get(), get(), get()) }
    factory { GetPaginatedEventsWithQueryUseCase(get(), get(), get()) }

    // Onboarding
    factory { MarkOnboardingAsCompletedUseCase(get()) }
    factory { ObserveOnboardingCompletionUseCase(get()) }

    // Organisation
    factory { DisableOrganizationUseCase(get(), get()) }
    factory { EnableOrganizationUseCase(get(), get()) }
    factory { GetOrganizationSummaryUseCase(get(), get()) }
    factory { ObserveOrganizationUseCase(get(), get(), get(), get()) }

    // Tag
    factory { ObserveTagSelectionUseCase(get()) }

    // User
    factory { DeleteSelfUserUseCase(get(), get(), get()) }
    factory { ObserveSelfUserUseCase(get(), get(), get()) }
    factory { LoginUseCase(get(), get(), get()) }
    factory { LogoutUseCase(get()) }
    factory { ObserveIsUserLoggedInUseCase(get()) }
    factory { PostNewPushTokenUseCase(get(), get(), get(), get()) }
    factory { RegisterUserUseCase(get(), get()) }
    factory { RequestPasswordResetUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { SetBearerUseCase(get()) }
    factory { ObserveProfileReloadTriggerUseCase(get()) }
    factory { UpdateUserProfileUseCase(get(), get(), get(), get()) }

    // Posting
    factory { ObservePostingReloadTriggerUseCase(get()) }

    // Search
    factory { ObserveSearchHistoryUseCase(get()) }
    factory { DeleteSearchHistoryEntryUseCase(get()) }
    factory { DeleteWholeSearchHistoryUseCase(get()) }
    factory { StoreSearchHistoryEntryUseCase(get()) }

    viewModelOf(::DetailedArticleViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::PasswordResetViewModel)
    viewModelOf(::RequestPasswordResetViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::BookmarksViewModel)
    viewModelOf(::EventsViewModel)
    viewModelOf(::DetailedEventViewModel)
    viewModelOf(::FeedViewModel)
    viewModel { (organizationId: Int, tabSelection: TabSelectionType) ->
        DetailedOrganizationViewModel(
            organizationId = organizationId,
            tabSelectionType = tabSelection,
            observeOrganizationUseCase = get(),
            getMyPaginatedEventPostsUseCase = get(),
            getMyPaginatedEventsForOrganizationUseCase = get(),
            deletePostedEventUseCase = get(),
            getMyPaginatedArticlePostsUseCase = get(),
            getPaginatedArticlesForOrganizationUseCase = get(),
            deletePostedArticleUseCase = get(),
            observePostingReloadTriggerUseCase = get()
        )
    }
    viewModelOf(::ProfileViewModel)
    viewModelOf(::InfoViewModel)
    viewModelOf(::TagSelectionViewModel)
    viewModelOf(::AreaSelectionViewModel)
    viewModelOf(::LocationViewModel)
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::OrganizationSummaryViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SearchResultViewModel)
    viewModel { (articleId: Int?) ->
        ArticleCreationViewModel(
            articleId = articleId,
            generateArticleTagsUseCase = get(),
            createArticleUseCase = get(),
            updateArticleUseCase = get(),
            getArticleDetailsWithTags = get(),
            getArticleCreationDataUseCase = get(),
        )
    }
    viewModel { (eventId: Int?) ->
        EventCreationViewModel(
            eventId = eventId,
            generateEventTagsUseCase = get(),
            getEventUpdateDataUseCase = get(),
            getEventCreationDataUseCase = get(),
            createEventUseCase = get(),
            updateEventUseCase = get(),
        )
    }

    single { Geocoder(androidApplication()) }
    single<NavigationDispatcher> { ChannelNavigationDispatcher() }
    single<EventBus<UIEvent>> { DefaultEventBus() }
}
