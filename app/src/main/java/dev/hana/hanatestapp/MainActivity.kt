package dev.hana.hanatestapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.branch.referral.Branch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        Thread {
            // Background thread
            Handler(Looper.getMainLooper()).post {
                // Now on main thread
                Branch.sessionBuilder(this@MainActivity).withCallback { buo, lp, error ->
                    if (error != null) {
                        Log.e("BranchSDK", "Init failed: ${error.message}")
                    } else {
                        Log.i("BranchSDK", "Init success!")
                    }
                }.withData(intent?.data).init()
            }
        }.start()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.setIntent(intent)
        if (intent != null && intent.hasExtra("branch_force_new_session") && intent.getBooleanExtra("branch_force_new_session",false)) {
            Branch.sessionBuilder(this).withCallback { referringParams, error ->
                if (error != null) {
                    Log.e("BranchSDK", error.message)
                } else if (referringParams != null) {
                    Log.i("BranchSDK", referringParams.toString())
                }
            }.reInit()
        }
    }
}