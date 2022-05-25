package errors

sealed class Error : Throwable() {
    class HeroNotFound : Error()
}
