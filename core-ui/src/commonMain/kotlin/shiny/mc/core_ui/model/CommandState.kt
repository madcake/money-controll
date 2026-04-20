package shiny.mc.core_ui.model

sealed class CommandState<Command>(val command: Command? = null) {
    class Idle<Command> : CommandState<Command>()
    class Processing<Command>(command: Command) : CommandState<Command>(command)
    class Success<Command>(command: Command) : CommandState<Command>(command)
    class Failure<Command>(val err: Throwable, command: Command) : CommandState<Command>(command)
}