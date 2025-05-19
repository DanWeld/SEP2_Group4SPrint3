package utilities.readerWriterLock;

public interface ReaderWriterLock
{
  void lockRead() throws InterruptedException;
  void unlockRead();
  void lockWrite() throws InterruptedException;
  void unlockWrite();
}
