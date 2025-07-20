package jess;

import java.awt.TextArea;
import java.io.IOException;
import java.io.OutputStream;

public class TextAreaOutputStream extends OutputStream {
  StringBuffer m_str = new StringBuffer(100);

  TextArea m_ta;

  public TextAreaOutputStream(TextArea paramTextArea) {
    this.m_ta = paramTextArea;
  }

  public void close() throws IOException {}

  public void flush() throws IOException {
    this.m_ta.appendText(this.m_str.toString());
    this.m_str.setLength(0);
  }

  public void write(int paramInt) throws IOException {
    this.m_str.append((char)paramInt);
  }

  public void write(byte[] paramArrayOfbyte) throws IOException {
    for (byte b = 0; b < paramArrayOfbyte.length; b++)
      this.m_str.append((char)paramArrayOfbyte[b]);
  }

  public void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
    for (int i = paramInt1; i < paramInt2; i++)
      this.m_str.append((char)paramArrayOfbyte[i]);
  }
}
