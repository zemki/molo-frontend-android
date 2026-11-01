package news.molo.data.network.endpoint

import de.convelop.forge.network.request.Route
import news.molo.data.BuildConfig

sealed class Endpoint(
    parent: Route? = Route(basePath = BuildConfig.BASE_URL),
    segment: String
) : Route(parent, segment) {

    object AI : Endpoint(segment = "AI") {
        object GenerateArticleTags : Endpoint(this, "generate-article-tags")
        object GenerateEventTags : Endpoint(this, "generate-event-tags")
    }

    object User : Endpoint(segment = "users") {
        data class ArticleBookmark(val articleId: Int) : Endpoint(this, "article-bookmarks/$articleId")
        data object ArticleBookmarks : Endpoint(this, "article-bookmarks")

        object EventBookmarks : Endpoint(this, "event-bookmarks")
        object Details : Endpoint(this, "get-user-details")
        object Delete : Endpoint(this, "delete")
        object Summary : Endpoint(this, "summary")
        object ResetPasswordRequest : Endpoint(this, "request-password-reset")
        object ChangePassword : Endpoint(this, "change-password")
        object Push : Endpoint(this, "push")

        object Event : Endpoint(this, "events")
        object Article : Endpoint(this, "articles")

        object UpdateUser : Endpoint(this, "update-user")
        object PictureUpload : Endpoint(this, "picture-upload")

        object ListEventTag : Endpoint(this, "list-tag-preferences-events")
        object SetEventTags : Endpoint(this, "set-tag-preferences-events")
        object ListArticleTag : Endpoint(this, "list-tag-preferences-articles")
        object SetArticleTags : Endpoint(this, "set-tag-preferences-articles")

        object EventsByCategory : Endpoint(this, "list_future_events_by_category")
        object ListArticle : Endpoint(this, "list_articles")
        object ListArticleByOrganization : Endpoint(this, "list_articles_by_organization")
        object ListEventsByOrganization : Endpoint(this, "list_events_by_organization")
        object ListEvents : Endpoint(this, "list_events_by_organization")
        object SetAreas : Endpoint(this, "set_areas_by_location")

        object Organization : Endpoint(this, "organizations")

        object RemoveOrganization : Endpoint(this, "remove-organization-from-user")
        object AddOrganization : Endpoint(this, "add-organization-to-user")
    }

    object Article : Endpoint(segment = "articles") {
        data class PictureUpload(val articleId: Int) : Endpoint(this, "$articleId/picture-upload")
    }

    object Area : Endpoint(segment = "area") {
        object ByDistance : Endpoint(this, "by_distance")
    }

    object Event : Endpoint(segment = "events") {
        data class PictureUpload(val eventId: Int) : Endpoint(this, "$eventId/picture-upload")
    }

    object Organization : Endpoint(segment = "organizations")
    object Token : Endpoint(segment = "token")

    object General : Endpoint(segment = "general") {
        object FlagArticle : Endpoint(this, "flag-article")
        object FlagEvent : Endpoint(this, "flag-event")
    }
}
