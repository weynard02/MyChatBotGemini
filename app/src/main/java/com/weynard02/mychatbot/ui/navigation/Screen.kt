package com.weynard02.mychatbot.ui.navigation

sealed class Screen(val route: String) {
    object Chat: Screen("chat")
    object Profile: Screen("profile")
}