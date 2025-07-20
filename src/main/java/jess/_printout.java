package jess;

import java.io.IOException;
import java.io.OutputStream;

class _printout implements Userfunction {
  public int name() {
    return RU.putAtom("printout");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    OutputStream outputStream = paramContext.engine().getOutputRouter(str);
    if (outputStream == null)
      throw new ReteException("_printout::call", "printout: bad router", str);
    StringBuffer stringBuffer = new StringBuffer(100);
    for (byte b = 2; b < paramValueVector.size(); b++) {
      Value value = paramValueVector.get(b);
      switch (value.type()) {
        case 1:
          if (value.stringValue().equals("crlf")) {
            stringBuffer.append("\n");
            break;
          }
          stringBuffer.append(value.stringValue());
          break;
        case 4:
          stringBuffer.append(value.intValue());
          break;
        case 32:
          stringBuffer.append(value.floatValue());
          break;
        case 16:
          stringBuffer.append("<Fact-");
          stringBuffer.append(value.factIDValue());
          stringBuffer.append(">");
          break;
        case 2:
          stringBuffer.append(value.stringValue());
          break;
        case 512:
          stringBuffer.append(value.toString());
          break;
        case 2048:
          stringBuffer.append(value.toString());
          break;
        default:
          throw new ReteException("_printout::call", "Bad data type", "type =" + value.type());
      }
    }
    try {
      for (byte b1 = 0; b1 < stringBuffer.length(); b1++)
        outputStream.write(stringBuffer.charAt(b1));
      outputStream.flush();
    } catch (IOException iOException) {
      throw new ReteException("_printout::call", "I/O Exception", iOException.toString());
    }
    return Funcall.s_nil;
  }
}
