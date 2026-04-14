import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        AppModuleKt.doInitKoinIos()
        // AppModuleKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}