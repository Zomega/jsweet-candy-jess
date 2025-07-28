package jess;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TextInputStream extends InputStream {
  private StringBuffer m_buf = new StringBuffer(256);

  private int m_ptr = 0;

  private DataInputStream m_source;

  private boolean m_markActive = false;

  private boolean m_dontWait = false;

  public TextInputStream() {}

  public TextInputStream(boolean paramBoolean) {
    this.m_dontWait = paramBoolean;
  }

  public TextInputStream(DataInputStream paramDataInputStream) {
    this.m_source = paramDataInputStream;
  }

  public synchronized int read() throws IOException {
    while (this.m_ptr >= this.m_buf.length()) {
      if (this.m_source != null) {
        String str = this.m_source.readLine();
        if (str == null) return -1;
        appendText(str);
        appendText("\n");
        continue;
      }
      if (this.m_dontWait) return -1;
      try {
        wait(100L);
      } catch (InterruptedException interruptedException) {
      }
    }
    char c = this.m_buf.charAt(this.m_ptr++);
    if (this.m_ptr >= this.m_buf.length()) clear();
    return c;
  }

  public int available() {
    return this.m_buf.length() - this.m_ptr;
  }

  public synchronized void appendText(String paramString) {
    this.m_buf.append(paramString);
    notifyAll();
  }

  public synchronized void clear() {
    if (this.m_markActive) return;
    this.m_buf.setLength(0);
    this.m_ptr = 0;
  }

  int mark() {
    this.m_markActive = true;
    return this.m_ptr;
  }

  boolean seenSince(int paramInt, char paramChar) {
    this.m_markActive = false;
    for (int i = paramInt; i < this.m_ptr; i++) {
      if (this.m_buf.charAt(i) == paramChar) return true;
    }
    return false;
  }
}
