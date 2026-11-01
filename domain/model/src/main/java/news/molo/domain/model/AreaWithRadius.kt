package news.molo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AreaWithRadius(
    val area: Area? = null,
    val radius: Int = 10
)