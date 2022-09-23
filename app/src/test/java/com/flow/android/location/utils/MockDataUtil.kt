package com.flow.android.location.utils

import com.flow.android.location.api.model.Category
import com.flow.android.location.api.model.GeoCode
import com.flow.android.location.api.model.Icon
import com.flow.android.location.api.model.Location
import com.flow.android.location.api.model.Main
import com.flow.android.location.api.model.Result

object MockData {

    val mockVenuesList = listOf(
        Result(
            categories = listOf(
                Category(Icon("", ""), name = "categoryOne", id = "1"),
                Category(Icon("", ""), name = "categoryTwo", id = "2")
            ),
            distance = 10,
            geocode = GeoCode(Main(1.0, 1.0)),
            location = Location(
                address = "",
                country = "",
                locality = "",
                neighbourhood = emptyList(),
                postcode = "",
                region = "",
                formatted_address = ""
            ),
            name = "BREN Avalon",
            timezone = "Asia/Kolkata"
        ),
        Result(
            categories = listOf(Category(Icon("", ""), name = "categoryThree", id = "3")),
            distance = 11,
            geocode = GeoCode(Main(2.0, 2.0)),
            location = Location(
                address = "",
                country = "",
                locality = "",
                neighbourhood = emptyList(),
                postcode = "",
                region = "",
                formatted_address = ""
            ),
            name = "Hotel Nandhini",
            timezone = "Asia/Kolkata"
        ),
        Result(
            categories = listOf(Category(Icon("", ""), name = "categoryFour", id = "4")),
            distance = 12,
            geocode = GeoCode(Main(3.0, 3.0)),
            location = Location(
                address = "",
                country = "",
                locality = "",
                neighbourhood = emptyList(),
                postcode = "",
                region = "",
                formatted_address = ""
            ),
            name = "Hubli",
            timezone = "Asia/Kolkata"
        )
    )
}
