package com.seiko.download.status

sealed class Status {
    var progress: Progress = Progress()
}

class Normal : Status()

class Started : Status()

class Downloading : Status()

class Paused : Status()

class Completed : Status()

class Failed : Status() {
    var throwable: Throwable = RuntimeException("UNKNOWN ERROR")
}

class Deleted : Status()