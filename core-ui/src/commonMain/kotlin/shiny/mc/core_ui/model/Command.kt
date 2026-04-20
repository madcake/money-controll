package shiny.mc.core_ui.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest

/**
 * Represents a command that can be executed, typically from a UI component.
 */
sealed interface Command {
    /**
     * An action command containing data of type [T].
     */
    class Action<T>(val data: T) : Command

    /**
     * A command to reset the state.
     */
    object Reset : Command
}

/**
 * Executes the [action] when the flow emits a [CommandState.Success] state.
 */
fun Flow<CommandState<Command>>.onSuccess(action: suspend (CommandState<Command>) -> Unit): Flow<CommandState<Command>> = transform { value ->
    if (value is CommandState.Success<*>) {
        action(value)
    }
    return@transform emit(value)
}

/**
 * Executes the [action] when the flow emits a [CommandState.Failure] state.
 */
fun Flow<CommandState<Command>>.onFailure(action: suspend (CommandState<Command>) -> Unit): Flow<CommandState<Command>> = transform { value ->
    if (value is CommandState.Failure<*>) {
        action(value)
    }
    return@transform emit(value)
}

/**
 * Executes the [action] when the flow emits either a [CommandState.Failure] or [CommandState.Success] state,
 * typically used to reset UI state after a command finishes.
 */
fun Flow<CommandState<Command>>.onReset(action: suspend (CommandState<Command>) -> Unit): Flow<CommandState<Command>> = transform { value ->
    if (value is CommandState.Failure<*> || value is CommandState.Success<*>) {
        action(value)
    }
    return@transform emit(value)
}

/**
 * Processes [Command]s by invoking the [handle] function for [Command.Action] commands.
 * [Command.Reset] commands emit an [CommandState.Idle] state.
 *
 * @param T The expected type of data in the [Command.Action].
 * @param handle The suspend function that processes the action data.
 * @return A [Flow] of [CommandState] representing the progress and result of processing.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<Command>.processCommand(handle: suspend (T) -> Unit): Flow<CommandState<Command>> = transformLatest { command ->
    when (command) {
        Command.Reset -> emit(CommandState.Idle())
        is Command.Action<*> -> {
            try {
                handle((command.data as? T) ?: throw IllegalArgumentException())
                emit(CommandState.Success(command))
            } catch (err: Throwable) {
                emit(CommandState.Failure(err, command))
            }
        }
    }
}
