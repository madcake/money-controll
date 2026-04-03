import SwiftUI
import shared
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinKt.doInitKoin()
        // AppModuleKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}