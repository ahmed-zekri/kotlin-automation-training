package presentation.types

sealed class MenuTypes(val value: String) {
    object PushCode : MenuTypes("Pushing code")
    object Home : MenuTypes("Home")
    object Exit : MenuTypes("Exiting app")
}