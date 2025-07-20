package jess;

import java.io.IOException;
import java.io.StreamTokenizer;

final class JessToken {
  String m_sval;

  double m_nval;

  int m_lineno;

  int m_ttype;

  static final JessToken create(StreamTokenizer paramStreamTokenizer, TextInputStream paramTextInputStream) {
    try {
      int i = paramTextInputStream.mark();
      paramStreamTokenizer.nextToken();
      JessToken jessToken = new JessToken();
      switch (paramStreamTokenizer.ttype) {
        case -3:
          jessToken.m_ttype = 1;
          break;
        case -2:
          if (paramTextInputStream.seenSince(i, '.')) {
            jessToken.m_ttype = 32;
            break;
          }
          jessToken.m_ttype = 4;
          break;
        case 34:
          jessToken.m_ttype = 2;
          break;
        case -1:
        case 0:
          jessToken.m_ttype = 0;
          break;
        default:
          jessToken.m_ttype = paramStreamTokenizer.ttype;
          break;
      }
      jessToken.m_sval = paramStreamTokenizer.sval;
      jessToken.m_nval = paramStreamTokenizer.nval;
      jessToken.m_lineno = paramStreamTokenizer.lineno();
      if (jessToken.m_ttype == 1)
        if (jessToken.m_sval.charAt(0) == '?') {
          jessToken.m_ttype = 8;
          if (jessToken.m_sval.length() > 1) {
            jessToken.m_sval = jessToken.m_sval.substring(1);
          } else {
            jessToken.m_sval = RU.getAtom(RU.gensym("_blank_"));
          }
        } else if (jessToken.m_sval.charAt(0) == '$' && jessToken.m_sval.charAt(1) == '?') {
          jessToken.m_ttype = 8192;
          if (jessToken.m_sval.length() > 2) {
            jessToken.m_sval = jessToken.m_sval.substring(2);
          } else {
            jessToken.m_sval = RU.getAtom(RU.gensym("_blank$_"));
          }
        } else if (jessToken.m_sval.equals("=")) {
          jessToken.m_ttype = 61;
        }
      return jessToken;
    } catch (IOException iOException) {
      return null;
    }
  }

  Value tokenToValue() throws ReteException {
    switch (this.m_ttype) {
      case 1:
        return new Value(this.m_sval, 1);
      case 32:
        return new Value(this.m_nval, 32);
      case 4:
        return new Value(this.m_nval, 4);
      case 2:
        return new Value(this.m_sval, 2);
    }
    return new Value(RU.putAtom("" + (char)this.m_ttype), 2);
  }

  boolean isBlankVariable() {
    return (this.m_sval != null && this.m_sval.startsWith("_blank"));
  }

  public String toString() {
    return (this.m_ttype == 8) ? ("?" + this.m_sval) : ((this.m_ttype == 8192) ? ("$?" + this.m_sval) : ((this.m_ttype == 2) ? ("\"" + this.m_sval + "\"") : ((this.m_sval != null) ? this.m_sval : ((this.m_ttype == 32) ? ("" + this.m_nval) : ((this.m_ttype == 4) ? ("" + (int)this.m_nval) : ("" + (char)this.m_ttype))))));
  }
}
