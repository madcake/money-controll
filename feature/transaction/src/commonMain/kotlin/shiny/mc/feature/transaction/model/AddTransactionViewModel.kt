package shiny.mc.feature.transaction.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.annotation.KoinViewModel
import org.koin.core.annotation.Named
import shiny.mc.core.ports.ui.UIModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
class AddTransactionViewModel(
    // @InjectedParam val recordId: String,
    @Named("add_transaction") private val uiModel: UIModel<AddTransactionUIState, AddTransactionCommand>
) : ViewModel(uiModel.scope), UIModel<AddTransactionUIState, AddTransactionCommand> by uiModel

