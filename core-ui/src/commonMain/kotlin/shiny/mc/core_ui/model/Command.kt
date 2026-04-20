package shiny.mc.core_ui.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest

/**
 * Represents a command that can be executed, typically from a UI component.
 */
sealed interface Command<out T> {
    /**
     * An action command containing data of type [T].
     */
    class Action<out T>(val data: T) : Command<T>

    /**
     * A command to reset the state.
     */
    class Reset<out T> : Command<T>
}

suspend fun <T> MutableSharedFlow<Command<T>>.action(data: T) = emit(Command.Action(data))

/**
 * Executes the [action] when the flow emits a [CommandState.Success] state.
 */
fun <T> Flow<CommandState<Command<T>>>.onSuccess(action: suspend (CommandState<Command<T>>) -> Unit): Flow<CommandState<Command<T>>> = transform { value ->
    if (value is CommandState.Success<*>) {
        action(value)
    }
    return@transform emit(value)
}

/**
 * Executes the [action] when the flow emits a [CommandState.Failure] state.
 */
fun <T> Flow<CommandState<Command<T>>>.onFailure(action: suspend (CommandState<Command<T>>) -> Unit): Flow<CommandState<Command<T>>> = transform { value ->
    if (value is CommandState.Failure<*>) {
        action(value)
    }
    return@transform emit(value)
}

/**
 * Executes the [action] when the flow emits either a [CommandState.Failure] or [CommandState.Success] state,
 * typically used to reset UI state after a command finishes.
 */
fun <T> Flow<CommandState<Command<T>>>.onReset(action: suspend (CommandState<Command<T>>) -> Unit): Flow<CommandState<Command<T>>> = transform { value ->
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
fun <T> Flow<Command<T>>.processCommand(handle: suspend (T) -> Unit): Flow<CommandState<Command<T>>> = transformLatest { command ->
    when (command) {
        is Command.Reset -> emit(CommandState.Idle())
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
