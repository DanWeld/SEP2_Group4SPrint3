package utilities.readerWriterLock;

/**
 * Interface for a reader-writer lock.
 * A reader-writer lock allows multiple threads to read shared data concurrently,
 * but only one thread to write at a time.
 */
public interface ReaderWriterLock
{
  void lockRead() throws InterruptedException;
  void unlockRead();
  void lockWrite() throws InterruptedException;
  void unlockWrite();
}
