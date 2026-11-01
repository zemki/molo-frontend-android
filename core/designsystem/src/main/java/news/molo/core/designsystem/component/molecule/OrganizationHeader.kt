package news.molo.core.designsystem.component.molecule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import news.molo.core.designsystem.component.atom.ShapedImage
import news.molo.core.designsystem.component.template.ComponentPreviewTemplate
import news.molo.core.designsystem.theme.Spacing
import news.molo.core.util.preview.ComponentPreview

@Composable
fun OrganizationHeader(
    modifier: Modifier = Modifier,
    imageUrl: String,
    name: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        ShapedImage(
            modifier = Modifier
                .size(80.dp),
            imageUrl = imageUrl
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.Medium)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(top = Spacing.ExtraSmall),
                text = description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 15,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    ComponentPreviewTemplate {
        OrganizationHeader(
            imageUrl = "",
            name = "Name",
            description = "Thats a description and ye idk what to say inringrauiegieuiangerauipgniuaerngiuearngiuearnguianeuigneauipgnuepangepuiangpuiengpiueangpuieangupinaeurgneaupingeupainrgupiaengupenugineiupangruieranhuipenrhuipenrhuipernhipueranahuipeg jergj erj grje gjeagji ea jgejagjeagj erg ekgj ejk agjear gjkea rjgk eajkrg aejkr gkjea rgkeaj rgjk erjk gaejk rgjkae rgjk earjkg eajk gjkea gjkearj gaejk rg"
        )
    }
}