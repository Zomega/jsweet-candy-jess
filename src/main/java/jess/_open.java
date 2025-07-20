package jess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

class _open implements Userfunction {
  public int name() {
    return RU.putAtom("open");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Rete rete = paramContext.engine();
    String str1 = paramValueVector.get(1).stringValue();
    String str2 = paramValueVector.get(2).stringValue();
    String str3 = "r";
    if (paramValueVector.size() > 3)
      str3 = paramValueVector.get(3).stringValue();
    try {
      if (str3.equals("r")) {
        rete.addInputRouter(str2, new BufferedInputStream(new FileInputStream(str1)));
      } else if (str3.equals("w")) {
        rete.addOutputRouter(str2, new BufferedOutputStream(new FileOutputStream(str1)));
      } else if (str3.equals("a")) {
        RandomAccessFile randomAccessFile = new RandomAccessFile(str1, "rw");
        randomAccessFile.seek(randomAccessFile.length());
        FileOutputStream fileOutputStream = new FileOutputStream(randomAccessFile.getFD());
        rete.addOutputRouter(str2, new BufferedOutputStream(fileOutputStream));
      } else {
        throw new ReteException("_open::call", "Unsupported access mode", str3);
      }
    } catch (IOException iOException) {
      throw new ReteException("_open::call", "I/O Exception", iOException.toString());
    }
    return new Value(str2, 1);
  }
}
