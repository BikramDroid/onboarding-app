package com.bikram.onboardingapp.data

enum class NavItems(val value: Int) {
    Home(0),
    Receipts(1),
    Profile(2);

    companion object {
        fun fromInt(value: Int) = NavItems.values().first { it.value == value }
    }
}