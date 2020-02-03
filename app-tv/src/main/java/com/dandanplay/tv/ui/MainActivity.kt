package com.dandanplay.tv.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.dandanplay.tv.R
import com.dandanplay.tv.util.extensions.hideSoftInput
import com.dandanplay.tv.util.extensions.isSoftInputMethodShowing
import androidx.activity.DispatchKeyEventDispatcher
import androidx.activity.DispatchKeyEventDispatcherOwner
import com.seiko.common.service.TorrentService

class MainActivity : FragmentActivity(R.layout.activity_main),
    DispatchKeyEventDispatcherOwner {

    @SuppressLint("RestrictedApi")
    private val dispatchKeyEventDispatcher = DispatchKeyEventDispatcher { event ->
        // 当软键盘弹出时，关闭软键盘。
        if (event?.action == KeyEvent.ACTION_DOWN
            && event.keyCode == KeyEvent.KEYCODE_BACK
        ) {
            if (isSoftInputMethodShowing()) {
                hideSoftInput()
                return@DispatchKeyEventDispatcher true
            }
        }
        return@DispatchKeyEventDispatcher super@MainActivity.dispatchKeyEvent(event)
    }

    /**
     * PS: Navigation在返回时，Fragment的View会重新绘制。
     */
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController(R.id.nav_controller)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 关闭Torrent下载
        TorrentService.get().shutDown(this)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return getDispatchKeyEventDispatcher().dispatchKeyEvent(event)
    }

    override fun getDispatchKeyEventDispatcher(): DispatchKeyEventDispatcher {
        return dispatchKeyEventDispatcher
    }

}
