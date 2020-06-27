package footballdata

import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.{ExecutorService, Executors, ThreadFactory}

import cats.effect.Blocker

import scala.concurrent.ExecutionContext

class ExecutionContexts {

  private val availableProcessors: Int = sys.runtime.availableProcessors()

  lazy val dbConnectionExecutionContext: ExecutionContext =
    createFixedSizeExecutionContext("db-connection", 4)

  lazy val dbTransactionBlocker: Blocker =
    Blocker.liftExecutionContext(ExecutionContext.fromExecutor(createFixedSizeExecutorService("db-transaction", 8)))

  private def createFixedSizeExecutionContext(namePrefix: String, parallelismMultiplier: Int): ExecutionContext =
    ExecutionContext.fromExecutor(createFixedSizeExecutorService(namePrefix, availableProcessors * parallelismMultiplier))

  private def createFixedSizeExecutorService(namePrefix: String, threads: Int): ExecutorService =
    Executors.newFixedThreadPool(threads, createThreadFactory(namePrefix))

  private def createThreadFactory(namePrefix: String): ThreadFactory =
    new ThreadFactory {
      val threadGroup: ThreadGroup =
        Option(System.getSecurityManager).map(_.getThreadGroup).getOrElse(Thread.currentThread().getThreadGroup)

      val counter = new AtomicLong(1L)

      override def newThread(r: Runnable): Thread =
        new Thread(threadGroup, r, s"$namePrefix-${counter.getAndIncrement().toString}")
    }

}
