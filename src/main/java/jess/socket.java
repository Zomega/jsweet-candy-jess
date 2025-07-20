package jess;

import java.io.IOException;
import java.net.Socket;

class socket implements Userfunction {
  private int m_name = RU.putAtom("socket");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str1 = paramValueVector.get(1).stringValue();
    int i = paramValueVector.get(2).intValue();
    String str2 = paramValueVector.get(3).stringValue();
    try {
      Socket socket1 = new Socket(str1, i);
      Rete rete = paramContext.engine();
      rete.addInputRouter(str2, socket1.getInputStream());
      rete.addOutputRouter(str2, socket1.getOutputStream());
      return paramValueVector.get(3);
    } catch (IOException iOException) {
      throw new ReteException("socket::call", "I/O Exception", iOException.toString());
    }
  }
}
