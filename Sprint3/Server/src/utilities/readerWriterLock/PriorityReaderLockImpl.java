package utilities.readerWriterLock;

public class PriorityReaderLockImpl implements ReaderWriterLock
{
  private int readers = 0;
  private boolean writer = false;

  public synchronized void lockRead() throws InterruptedException {
    while (writer) {
      wait();
    }
    readers++;
  }

  public synchronized void unlockRead() {
    readers--;
    if (readers == 0) {
      notifyAll();
    }
  }

  public synchronized void lockWrite() throws InterruptedException {
    while (writer || readers > 0) {
      wait();
    }
    writer = true;
  }

  public synchronized void unlockWrite() {
    writer = false;
    notifyAll();
  }
}