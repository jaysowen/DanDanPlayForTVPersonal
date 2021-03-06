package com.seiko.torrent.ui.add

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.DispatchKeyEventDispatcher
import androidx.activity.DispatchKeyEventDispatcherOwner
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.seiko.common.eventbus.registerEventBus
import com.seiko.common.eventbus.unRegisterEventBus
import com.seiko.common.router.Navigator
import com.seiko.common.router.Routes
import com.seiko.download.torrent.model.TorrentTask
import com.seiko.torrent.R
import com.seiko.torrent.data.model.PostEvent
import com.seiko.torrent.ui.add.AddTorrentFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


@Route(path = Routes.Torrent.PATH_ADD)
class TorrentAddActivity : FragmentActivity(R.layout.torrent_activiy_add)
    , DispatchKeyEventDispatcherOwner {

    @SuppressLint("RestrictedApi")
    private val dispatchKeyEventDispatcher = DispatchKeyEventDispatcher { event ->
        return@DispatchKeyEventDispatcher super@TorrentAddActivity.dispatchKeyEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")
        checkIntent()
        registerEventBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterEventBus()
        Timber.d("onDestroy")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkIntent()
    }

    private fun checkIntent() {
        val openIntent = intent
        if (openIntent == null) {
            Timber.w("Torrent Add Intent is Null.")
            finish()
            return
        }

        val uri: Uri = when {
            openIntent.data != null -> {
                openIntent.data!!
            }
            else -> {
                Timber.w("Torrent Add Uri is Null.")
                finish()
                return
            }
        }
        openAddFragment(uri)
    }

    private fun openAddFragment(uri: Uri) {
        if (supportFragmentManager.findFragmentByTag(AddTorrentFragment.TAG) == null) {
            supportFragmentManager.commit {
                add(R.id.torrent_container_add,
                    AddTorrentFragment.newInstance(uri),
                    AddTorrentFragment.TAG)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceive(event: PostEvent) {
        when(event) {
            is PostEvent.TorrentAdded -> {
                setTorrentResult(event.torrent)
                Navigator.navToTorrent(this)
                finish()
            }
        }
    }

    private fun setTorrentResult(task: TorrentTask) {
        val data = Intent()
        data.putExtra(Routes.Torrent.RESULT_KEY_ADD_SUCCESS, true)
        data.putExtra(Routes.Torrent.RESULT_KEY_ADD_HASH, task.hash)
        setResult(RESULT_OK, data)
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return getDispatchKeyEventDispatcher().dispatchKeyEvent(event)
    }

    override fun getDispatchKeyEventDispatcher(): DispatchKeyEventDispatcher {
        return dispatchKeyEventDispatcher
    }
}