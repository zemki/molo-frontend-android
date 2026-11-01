package news.molo.feature.feed.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import news.molo.core.resource.R

sealed class ArticleFilter(
    @param:StringRes
    val titleRes: Int,
    @param:DrawableRes
    val iconRes: Int,
) {
    data object Media : ArticleFilter(
        titleRes = R.string.article_filter_title_media,
        iconRes = R.drawable.ic_news_small
    )

    data object Official : ArticleFilter(
        titleRes = R.string.article_filter_title_official,
        iconRes = R.drawable.ic_official_small
    )

    data object Community : ArticleFilter(
        titleRes = R.string.article_filter_title_community,
        iconRes = R.drawable.ic_community_small
    )

    companion object {
        fun all(): List<ArticleFilter> = listOf(
            Media,
            Official,
            Community,
        )
    }
}