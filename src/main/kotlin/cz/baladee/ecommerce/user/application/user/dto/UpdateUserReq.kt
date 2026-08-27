package cz.baladee.ecommerce.user.application.user.dto

import cz.baladee.ecommerce.user.domain.model.AddressType
import cz.baladee.ecommerce.user.domain.model.Country

data class UpdateUserReq(
    val firstName: String?,
    val lastName: String?,
    val phone: String?,
    val addresses: List<UpdateAddressReq> = emptyList()
)

data class UpdateAddressReq(
    val type: AddressType,
    val street: String,
    val city: String,
    val postalCode: String,
    val country: Country
)