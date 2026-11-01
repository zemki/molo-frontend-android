package news.molo.feature.feed.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import news.molo.core.designsystem.component.atom.FilterItem
import news.molo.core.designsystem.component.molecule.FilterRow
import news.molo.feature.feed.presentation.ArticleFilter

@Composable
internal fun ArticleFilterRow(
    filter: List<ArticleFilter>,
    selectedFilter: ArticleFilter?,
    onSelectedChange: (ArticleFilter) -> Unit
) {
    FilterRow(
        items = filter,
        centerItems = true,
        filterItem = { filter ->
            FilterItem(
                title = stringResource(filter.titleRes),
                leadingIcon = {
                    Icon(
                        painterResource(filter.iconRes),
                        contentDescription = null,
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                },
                selected = filter == selectedFilter,
                onSelectedChange = {
                    onSelectedChange(filter)
                }
            )
        }
    )
}