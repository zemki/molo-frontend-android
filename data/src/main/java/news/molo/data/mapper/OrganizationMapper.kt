package news.molo.data.mapper

import news.molo.data.network.response.OrganizationResponse
import news.molo.data.network.response.OrganizationSummaryResponse
import news.molo.domain.model.Category
import news.molo.domain.model.Organization
import news.molo.domain.model.OrganizationSummary

fun OrganizationSummaryResponse.toOrganizationSummary(): OrganizationSummary = OrganizationSummary(
    allTags = this.allTags,
    byTag = this.byTag,
    deselected = this.deselected,
    name = this.name,
    push = this.push,
    category = Category.fromQueryValue(this.type),
    organizations = emptyList()
)

fun OrganizationResponse.toOrganization(): Organization = Organization(
    id = this.id,
    name = this.name,
    imageURL = this.image,
    address = this.address ?: "",
    description = this.description ?: "",
    homepage = this.homepage ?: "",
    isSelected = this.isSelected ?: false,
    title = this.title ?: "",
    category = Category.fromQueryValue(this.type),
    isOwnedBySelfUser = false
)