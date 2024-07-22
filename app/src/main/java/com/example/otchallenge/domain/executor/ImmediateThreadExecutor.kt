package com.example.otchallenge.domain.executor

class ImmediateThreadExecutor : ThreadExecutor {
    override fun execute(runnable: Runnable?) {
        runnable?.run()
    }
}