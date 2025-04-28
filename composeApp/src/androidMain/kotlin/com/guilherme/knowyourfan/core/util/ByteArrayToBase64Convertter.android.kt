package com.guilherme.knowyourfan.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
actual fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)
