package com.example.mob_app_hw2.data

import com.example.mob_app_hw2.constants.BERLIN
import com.example.mob_app_hw2.constants.BERLIN_DESCRIPTION
import com.example.mob_app_hw2.constants.BERLIN_IMAGE_RES
import com.example.mob_app_hw2.constants.LISBON
import com.example.mob_app_hw2.constants.LISBON_DESCRIPTION
import com.example.mob_app_hw2.constants.LISBON_IMAGE_RES
import com.example.mob_app_hw2.constants.LONDON
import com.example.mob_app_hw2.constants.LONDON_DESCRIPTION
import com.example.mob_app_hw2.constants.LONDON_IMAGE_RES
import com.example.mob_app_hw2.constants.MADRID
import com.example.mob_app_hw2.constants.MADRID_DESCRIPTION
import com.example.mob_app_hw2.constants.MADRID_IMAGE_RES
import com.example.mob_app_hw2.constants.MOSCOW
import com.example.mob_app_hw2.constants.MOSCOW_DESCRIPTION
import com.example.mob_app_hw2.constants.MOSCOW_IMAGE_RES
import com.example.mob_app_hw2.constants.WASHINGTON
import com.example.mob_app_hw2.constants.WASHINGTON_DESCRIPTION
import com.example.mob_app_hw2.constants.WASHINGTON_IMAGE_RES
import com.example.mob_app_hw2.constants.YEREVAN
import com.example.mob_app_hw2.constants.YEREVAN_DESCRIPTION
import com.example.mob_app_hw2.constants.YEREVAN_IMAGE_RES

val cityImageMap = mapOf(
    YEREVAN to YEREVAN_IMAGE_RES,
    WASHINGTON to WASHINGTON_IMAGE_RES,
    MADRID to MADRID_IMAGE_RES,
    MOSCOW to MOSCOW_IMAGE_RES,
    LISBON to LISBON_IMAGE_RES,
    LONDON to LONDON_IMAGE_RES,
    BERLIN to BERLIN_IMAGE_RES,

    // Add more city-image mappings as needed
)

val cityDescriptionMap = mapOf(
    YEREVAN to YEREVAN_DESCRIPTION,
    WASHINGTON to WASHINGTON_DESCRIPTION,
    MADRID to MADRID_DESCRIPTION,
    MOSCOW to MOSCOW_DESCRIPTION,
    LISBON to LISBON_DESCRIPTION,
    LONDON to LONDON_DESCRIPTION,
    BERLIN to BERLIN_DESCRIPTION,
    // Add more city-image mappings as needed

)

val cities = listOf(
    YEREVAN,
    WASHINGTON,
    MADRID,
    MOSCOW,
    LISBON,
    LONDON,
    BERLIN,

    // Add more cities here
)