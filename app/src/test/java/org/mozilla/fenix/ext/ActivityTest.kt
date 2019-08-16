package org.mozilla.fenix.ext
import kotlinx.coroutines.ObsoleteCoroutinesApi
import androidx.core.content.ContextCompat
import mozilla.components.support.test.robolectric.testContext
import org.mozilla.fenix.TestApplication
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import io.mockk.mockk
import android.app.Activity
import android.view.View

import android.view.WindowManager
import android.view.Window
import org.robolectric.Shadows.shadowOf

@ObsoleteCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)

class ActivityTest {

    @Test
    fun testEnterImmersiveMode() {
        val activity = Robolectric.buildActivity(Activity::class.java).create().get()
        val window = activity.getWindow()

        // Turn off Keep Screen on Flag if it is on
        if (shadowOf(window).getFlag(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)) window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Make sure that System UI flags are not set before the test
        val flags = arrayOf(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, View.SYSTEM_UI_FLAG_FULLSCREEN, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        for (f in flags) if ((window.getDecorView().getSystemUiVisibility() and f) == f) window.getDecorView().setSystemUiVisibility(0)

        // Run
        activity.enterToImmersiveMode()

        // Test
        for (f in flags) assertEquals(f, (window.getDecorView().getSystemUiVisibility() and f))
        assertEquals(true, shadowOf(window).getFlag(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON))
    }
}