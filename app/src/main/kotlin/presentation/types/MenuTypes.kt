package presentation.types

sealed class MenuTypes(val value: String) {
    class Error(error: String) : MenuTypes(error)
    class Report(s: String) : MenuTypes(s)


    object PushCode : MenuTypes("Pushing code")
    object Home : MenuTypes("Home")
    object Exit : MenuTypes("Exiting app")
    object InteractiveRebase : MenuTypes("Interactive rebase")
}