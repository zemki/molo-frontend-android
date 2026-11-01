package news.molo.data.mapper

import news.molo.data.network.response.UserResponse
import news.molo.domain.model.User

fun UserResponse.toUser(): User = User(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    displayName = this.displayName,
    imageUrl = this.picture,
    imageSource = this.imageSource,
    zip = this.zip,
    phone = this.phone,
    mail = this.email,
    description = this.description,
    homepage = this.homepage,
    area = this.area.toArea(),
    organizationId = this.organizationId
)