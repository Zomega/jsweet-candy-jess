package jess;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class batch implements Userfunction {
  private int m_name = RU.putAtom("batch");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    Value value = Funcall.FALSE();
    InputStream inputStream = null;
    try {
      if (paramContext.engine().display().applet() == null) {
        inputStream = new FileInputStream(str);
      } else {
        URL uRL = new URL(paramContext.engine().display().applet().getDocumentBase(), paramValueVector.get(1).stringValue());
        inputStream = uRL.openStream();
      }
      Jesp jesp = new Jesp(inputStream, paramContext.engine());
      while (true) {
        value = jesp.parse(false);
        if (inputStream.available() <= 0)
          return value;
      }
    } catch (IOException iOException) {
      throw new ReteException("batch", "I/O Exception on file", "");
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        } catch (IOException iOException) {}
    }
  }
}
