package jess;

import java.io.DataInputStream;
import java.io.IOException;

class _read implements Userfunction {
  private TextInputStream m_tis = new TextInputStream();

  private JessTokenStream m_jts = new JessTokenStream(new DataInputStream(this.m_tis));

  public int name() {
    return RU.putAtom("read");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = "t";
    if (paramValueVector.size() > 1)
      str = paramValueVector.get(1).stringValue();
    DataInputStream dataInputStream = (DataInputStream)paramContext.engine().getInputRouter(str);
    if (dataInputStream == null)
      throw new ReteException("_read::call", "bad router", str);
    StringBuffer stringBuffer = new StringBuffer(80);
    int i = 0;
    try {
      do {

      } while ((i = dataInputStream.read()) != -1 && Character.isSpace((char)i));
      if (i == -1)
        return Funcall.s_eof;
      stringBuffer.append((char)i);
      stringBuffer.append(dataInputStream.readLine());
    } catch (IOException iOException) {
      throw new ReteException("_read::call", "I/O Exception", iOException.toString());
    }
    stringBuffer.append('\n');
    this.m_tis.appendText(stringBuffer.toString());
    JessToken jessToken = this.m_jts.getOneToken();
    this.m_tis.clear();
    return jessToken.tokenToValue();
  }
}
