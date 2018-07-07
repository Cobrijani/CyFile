package at.tugraz.tc.cyfile.async.impl

import at.tugraz.tc.cyfile.async.ThreadExecutor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class JobExecutor : ThreadExecutor {

    val threadPoolExecutor = ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>(), JobThreadFactory())

    override fun execute(command: Runnable) {
        this.threadPoolExecutor.execute(command)
    }


    private class JobThreadFactory : ThreadFactory {
        private var counter = 0

        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, "android_" + counter++)
        }
    }
}