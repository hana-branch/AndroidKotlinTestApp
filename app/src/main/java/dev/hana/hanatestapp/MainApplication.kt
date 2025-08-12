package dev.hana.hanatestapp

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import io.branch.referral.Branch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainApplication: Application () {
    override fun onCreate() {
        super.onCreate()

        // Enable StrictMode in debug builds
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()          // Detects when your app reads from disk on the main thread
                .detectDiskWrites()         // Detects when your app writes to disk on the main thread
                .detectNetwork()            // Detects when your app performs network access on the main thread
                .detectCustomSlowCalls()    // Detects other slow custom calls
                .penaltyLog()               // Logs a message to Logcat when a violation is found
                .penaltyFlashScreen()       // Flashes the screen red to indicate a violation
                .build()
        )

        Branch.enableLogging()
        Branch.expectDelayedSessionInitialization(true)

        // Initialize the Branch SDK in a background thread
        GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            Branch.getAutoInstance(applicationContext)
        }
    }
}