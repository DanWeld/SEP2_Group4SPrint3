package utilities.readerWriterLock;

/**
 * Interface for a reader-writer lock.
 * A reader-writer lock allows multiple threads to read shared data concurrently,
 * but only one thread to write at a time.
 */
public class PriorityReaderLockImpl implements ReaderWriterLock
{
  private int readers = 0;
  private boolean writer = false;

  /**
   * Locks the reader for reading.
   * If a writer is currently writing, the thread will wait until the writer is done.
   *
   * @throws InterruptedException if the thread is interrupted while waiting
   */
  public synchronized void lockRead() throws InterruptedException {
    while (writer) {
      wait();
    }
    readers++;
  }

  /**
   * Unlocks the reader after reading.
   * If there are no more readers, it notifies any waiting threads.
   */
  public synchronized void unlockRead() {
    readers--;
    if (readers == 0) {
      notifyAll();
    }
  }

  /**
   * Locks the writer for writing.
   * If there are any readers or another writer, the thread will wait until they are done.
   *
   * @throws InterruptedException if the thread is interrupted while waiting
   */
  public synchronized void lockWrite() throws InterruptedException {
    while (writer || readers > 0) {
      wait();
    }
    writer = true;
  }

  /**
   * Unlocks the writer after writing.
   * It notifies all waiting threads that they can proceed.
   */
  public synchronized void unlockWrite() {
    writer = false;
    notifyAll();
  }
}