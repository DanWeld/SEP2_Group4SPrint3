package utilities.readerWriterLock;

/**
 * Interface for a reader-writer lock.
 * A reader-writer lock allows multiple threads to read shared data concurrently,
 * but only one thread to write at a time.
 *
 */
public class PriorityWriterLockImpl implements ReaderWriterLock {
  private int readers = 0;
  private int writersWaiting = 0;
  private boolean writer = false;

  /**
   * Locks for reading. If a writer is currently writing or waiting, this method will block until it can acquire the lock.
   *
   * @throws InterruptedException if the thread is interrupted while waiting for the lock
   */
  @Override
  public synchronized void lockRead() throws InterruptedException {
    while (writer || writersWaiting > 0) {
      wait();
    }
    readers++;
  }

  /**
   * Unlocks the read lock. If there are no more readers, it notifies waiting threads.
   */
  @Override
  public synchronized void unlockRead() {
    readers--;
    if (readers == 0) {
      notifyAll();
    }
  }

  /**
   * Locks for writing. If there are readers or writers waiting, this method will block until it can acquire the lock.
   *
   * @throws InterruptedException if the thread is interrupted while waiting for the lock
   */
  @Override
  public synchronized void lockWrite() throws InterruptedException {
    writersWaiting++;
    while (writer || readers > 0) {
      wait();
    }
    writersWaiting--;
    writer = true;
  }

  /**
   * Unlocks the write lock. It notifies all waiting threads, allowing either readers or writers to proceed.
   */
  @Override
  public synchronized void unlockWrite() {
    writer = false;
    notifyAll();
  }
}