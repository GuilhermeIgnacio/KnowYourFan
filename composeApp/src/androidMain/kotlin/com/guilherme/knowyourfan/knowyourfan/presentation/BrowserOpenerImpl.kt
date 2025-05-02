package com.guilherme.knowyourfan.knowyourfan.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.guilherme.knowyourfan.knowyourfan.domain.BrowserOpener
import androidx.core.net.toUri

class BrowserOpenerImpl(val context: Context): BrowserOpener {
    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }
}