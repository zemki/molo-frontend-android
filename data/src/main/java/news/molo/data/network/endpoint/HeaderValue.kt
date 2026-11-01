package news.molo.data.network.endpoint

import de.convelop.forge.network.request.HttpHeader

sealed class HeaderValue(override val key: String, override val value: String) : HttpHeader {
    data class DeviceID(val deviceId: String) : HeaderValue(key = "X-Device-ID", value = deviceId)
    data class Bearer(val bearer: String) : HeaderValue(key = "Authorization", value = bearer)
}