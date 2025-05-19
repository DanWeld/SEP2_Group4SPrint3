package utilities.readerWriterLock;

public class PriorityWriterLockImpl implements ReaderWriterLock {
  private int readers = 0;
  private int writersWaiting = 0;
  private boolean writer = false;

  @Override
  public synchronized void lockRead() throws InterruptedException {
    while (writer || writersWaiting > 0) {
      wait();
    }
    readers++;
  }

  @Override
  public synchronized void unlockRead() {
    readers--;
    if (readers == 0) {
      notifyAll();
    }
  }

  @Override
  public synchronized void lockWrite() throws InterruptedException {
    writersWaiting++;
    while (writer || readers > 0) {
      wait();
    }
    writersWaiting--;
    writer = true;
  }

  @Override
  public synchronized void unlockWrite() {
    writer = false;
    notifyAll();
  }
}