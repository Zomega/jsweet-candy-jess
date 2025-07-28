package jess;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

class _save_facts implements Userfunction {
  public int name() {
    return RU.putAtom("save-facts");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    PrintStream printStream;
    String str = "";
    if (paramContext.engine().display().applet() == null) {
      try {
        printStream = new PrintStream(new FileOutputStream(paramValueVector.get(1).stringValue()));
      } catch (IOException iOException) {
        throw new ReteException("_save_facts::call", "I/O Exception", iOException.toString());
      }
    } else {
      try {
        URL uRL =
            new URL(
                paramContext.engine().display().applet().getDocumentBase(),
                paramValueVector.get(1).stringValue());
        URLConnection uRLConnection = uRL.openConnection();
        uRLConnection.setDoOutput(true);
        printStream = new PrintStream(uRLConnection.getOutputStream());
      } catch (Exception exception) {
        throw new ReteException("_load_facts::call", "Network error", exception.toString());
      }
    }
    if (paramValueVector.size() > 2) {
      for (byte b = 2; b < paramValueVector.size(); b++)
        str = str + paramContext.engine().ppFacts(paramValueVector.get(b).atomValue());
    } else {
      str = paramContext.engine().ppFacts();
    }
    printStream.println(str);
    printStream.close();
    return Funcall.s_true;
  }
}
