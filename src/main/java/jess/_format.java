package jess;

import java.io.IOException;
import java.io.OutputStream;

class _format implements Userfunction {
  private int m_name = RU.putAtom("format");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    StringBuffer stringBuffer1 = new StringBuffer(20);
    StringBuffer stringBuffer2 = new StringBuffer(100);
    Value value = paramValueVector.get(1);
    String str1 = paramValueVector.get(2).stringValue();
    byte b1 = 0;
    byte b2 = 3;
    char c = Character.MIN_VALUE;
    while (b1 < str1.length()) {
      Value value1;
      while (b1 < str1.length() && (c = str1.charAt(b1++)) != '%')
        stringBuffer2.append(c);
      if (b1 >= str1.length())
        break;
      stringBuffer1.setLength(0);
      stringBuffer1.append(c);
      while (b1 < str1.length() && (c = str1.charAt(b1++)) != '%' && !Character.isLetter(c))
        stringBuffer1.append(c);
      if (c == 'n') {
        stringBuffer2.append('\n');
        break;
      }
      if (c == '%') {
        stringBuffer2.append('%');
        break;
      }
      stringBuffer1.append(c);
      Format format = new Format(stringBuffer1.toString());
      switch (format.fmt) {
        case 'X':
        case 'd':
        case 'i':
        case 'o':
        case 'x':
          value1 = paramValueVector.get(b2++);
          stringBuffer2.append(format.form(value1.intValue()));
          continue;
        case 'E':
        case 'G':
        case 'e':
        case 'f':
        case 'g':
          value1 = paramValueVector.get(b2++);
          stringBuffer2.append(format.form(value1.floatValue()));
          continue;
        case 'c':
          value1 = paramValueVector.get(b2++);
          switch (value1.type()) {
            case 1:
            case 2:
              stringBuffer2.append(format.form(value1.stringValue().charAt(0)));
              continue;
          }
          stringBuffer2.append(format.form((char)value1.intValue()));
          continue;
        case 's':
          value1 = paramValueVector.get(b2++);
          stringBuffer2.append(format.form(value1.stringValue()));
          continue;
      }
      throw new ReteException("_format::call", "Unknown format", stringBuffer1.toString());
    }
    String str2 = stringBuffer2.toString();
    if (!value.equals(Funcall.NIL())) {
      String str = value.stringValue();
      OutputStream outputStream = paramContext.engine().getOutputRouter(str);
      if (outputStream == null)
        throw new ReteException("_format::call", "Bad router", str);
      try {
        for (byte b = 0; b < str2.length(); b++)
          outputStream.write(str2.charAt(b));
        outputStream.flush();
      } catch (IOException iOException) {
        throw new ReteException("_format::call", "I/O Exception", iOException.toString());
      }
    }
    return new Value(str2, 2);
  }
}
