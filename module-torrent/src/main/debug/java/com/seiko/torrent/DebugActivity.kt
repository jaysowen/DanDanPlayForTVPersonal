package com.seiko.torrent

import androidx.fragment.app.FragmentActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.seiko.torrent.service.TorrentTaskService

/**
 * 与TorrentMainActivity相同，多一个退出时关闭引擎
 */
@Route(path = "/torrent/torrentDebug")
class DebugActivity : FragmentActivity(R.layout.torrent_activity_main) {

    override fun onDestroy() {
        super.onDestroy()
        TorrentTaskService.shutDown(this)
    }

}