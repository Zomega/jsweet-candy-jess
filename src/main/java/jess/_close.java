package jess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class _close implements Userfunction {
  public int name() {
    return RU.putAtom("close");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Rete rete = paramContext.engine();
    if (paramValueVector.size() > 1) {
      for (byte b = 1; b < paramValueVector.size(); b++) {
        String str = paramValueVector.get(b).stringValue();
        try {
          OutputStream outputStream;
          if ((outputStream = rete.getOutputRouter(str)) != null) {
            outputStream.close();
            rete.removeOutputRouter(str);
          }
        } catch (IOException iOException) {
        }
        try {
          InputStream inputStream;
          if ((inputStream = rete.getInputRouter(str)) != null) {
            inputStream.close();
            rete.removeInputRouter(str);
          }
        } catch (IOException iOException) {
        }
      }
    } else {
      throw new ReteException("_close::call", "Must close files by name", "");
    }
    return Funcall.TRUE();
  }
}
