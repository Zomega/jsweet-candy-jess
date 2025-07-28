package jess;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

class _readline implements Userfunction {
  public int name() {
    return RU.putAtom("readline");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = "t";
    if (paramValueVector.size() > 1) str = paramValueVector.get(1).stringValue();
    InputStream inputStream = paramContext.engine().getInputRouter(str);
    if (inputStream == null) throw new ReteException("_readline::call", "bad router", str);
    try {
      DataInputStream dataInputStream;
      if (inputStream instanceof DataInputStream) {
        dataInputStream = (DataInputStream) inputStream;
      } else {
        throw new ReteException("readline", "Input router malformed: ", str);
      }
      String str1 = dataInputStream.readLine();
      return (str1 == null) ? Funcall.s_eof : new Value(str1, 2);
    } catch (IOException iOException) {
      throw new ReteException("readline", iOException.toString(), "");
    }
  }
}
