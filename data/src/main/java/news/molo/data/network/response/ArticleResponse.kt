package news.molo.data.network.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import news.molo.data.network.serializer.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class ArticleResponse(
    @SerialName("abstract")
    val `abstract`: String = "",
    @SerialName("address")
    val address: String,
    @SerialName("archived")
    val archived: Boolean,
    @SerialName("areas")
    val areas: List<AreaResponse> = emptyList(),
    @SerialName("date")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val date: OffsetDateTime,
    @SerialName("id")
    val id: Int,
    @SerialName("image_source")
    val imageSource: String?,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("is_hot")
    val isHot: Boolean,
    @SerialName("link")
    val link: String,
    @SerialName("organization")
    val organization: Int,
    @SerialName("organization_name")
    val organizationName: String,
    @SerialName("organization_image")
    val organizationImage: String?,
    @SerialName("organization_type")
    val organizationType: String,
    @SerialName("request_count")
    val requestCount: Int? = null,
    @SerialName("tags")
    val tags: List<TagResponse>,
    @SerialName("title")
    val title: String,
    @SerialName("bookmarked_count")
    val bookmarkCount: Int? = null,
    @SerialName("published")
    val published: Boolean = true,
    @SerialName("bookmarked")
    val bookmarked: Boolean = false
)