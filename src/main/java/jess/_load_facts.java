package jess;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class _load_facts implements Userfunction {
  public int name() {
    return RU.putAtom("load-facts");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    InputStream inputStream;
    String str = "ERROR";
    if (paramContext.engine().display().applet() == null) {
      try {
        inputStream = new FileInputStream(paramValueVector.get(1).stringValue());
      } catch (IOException iOException) {
        throw new ReteException("_load_facts::call", "I/O Exception", iOException.toString());
      }
    } else {
      try {
        URL uRL = new URL(paramContext.engine().display().applet().getDocumentBase(), paramValueVector.get(1).stringValue());
        inputStream = uRL.openStream();
      } catch (Exception exception) {
        throw new ReteException("_load_facts::call", "Network error", exception.toString());
      }
    }
    Jesp jesp = new Jesp(inputStream, paramContext.engine());
    return jesp.loadFacts();
  }
}
