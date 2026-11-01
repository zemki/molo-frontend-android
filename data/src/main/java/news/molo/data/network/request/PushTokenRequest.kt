package news.molo.data.network.request


import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class PushTokenRequest(
    @SerialName("firebase_token")
    val firebaseToken: String,
    @EncodeDefault
    @SerialName("push_collective")
    val pushCollective: Boolean = true,
    @EncodeDefault
    @SerialName("push_news")
    val pushNews: Boolean = true,
    @EncodeDefault
    @SerialName("push_official")
    val pushOfficial: Boolean = true,
)