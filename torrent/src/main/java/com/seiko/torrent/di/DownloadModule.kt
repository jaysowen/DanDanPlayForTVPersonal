package com.seiko.torrent.di

import com.seiko.torrent.download.DownloadManager
import com.seiko.torrent.download.Downloader
import com.seiko.download.torrent.TorrentEngineOptions
import com.seiko.torrent.data.comments.TorrentRepository
import org.koin.dsl.module

internal val downloadModule = module {
    single { createDownloader(get(), get()) }
}

private fun createDownloader(
    options: TorrentEngineOptions,
    torrentRepo: TorrentRepository
): Downloader {
    return DownloadManager(options, torrentRepo)
}