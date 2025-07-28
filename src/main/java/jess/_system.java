package jess;

import java.io.IOException;

class _system implements Userfunction {
  private int m_name = RU.putAtom("system");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    try {
      boolean bool = false;
      int i = paramValueVector.size();
      if (paramValueVector.get(i - 1).stringValue().equals("&")) {
        bool = true;
        i--;
      }
      String[] arrayOfString = new String[i - 1];
      for (byte b = 1; b < i; b++) arrayOfString[b - 1] = paramValueVector.get(b).stringValue();
      Process process = Runtime.getRuntime().exec(arrayOfString);
      try {
        if (!bool) process.waitFor();
      } catch (InterruptedException interruptedException) {
        return Funcall.FALSE();
      }
      return Funcall.TRUE();
    } catch (IOException iOException) {
      throw new ReteException("system", "I/O Exception", "");
    } catch (SecurityException securityException) {
      throw new ReteException("system", "Security Exception", "");
    }
  }
}
