import uz.khusinov.karvon.domain.model.Address

data class FullAddress(
    val addresstype: String,
    val boundingbox: List<String>,
    val display_name: String,
    val importance: Double,
    val lat: String,
    val licence: String,
    val lon: String,
    val name: String?,
    val osm_id: Int,
    val osm_type: String,
    val place_id: Int,
    val place_rank: Int,
    val type: String,
    val address: Address?
) {
    fun getAddressText(): String {
        return if (address != null) {
            address.amenity ?: address.road ?: address.city
            ?: if (display_name.length > 32) display_name.substring(
                0, 32
            ) + "..." else display_name
        } else if (name != null) if (name.length > 32) name.substring(
            0,
            32
        ) + "..."
        else name else ""
    }

    fun getHomeTown(): String {
        return if (address != null) {
            address.city ?: address.road
            ?: address.county ?: address.state
            ?: if (display_name.length > 18) display_name.substring(
                0, 18
            ) + "..."
            else display_name
        } else if (display_name.length > 18) display_name.substring(
            0, 18
        ) + "..."
        else display_name
    }
}
