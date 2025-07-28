package jess;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.Stack;

class JessTokenStream {
  private Stack m_stack;

  private StreamTokenizer m_stream;

  private TextInputStream m_source;

  private int m_lineno = 1;

  private StringBuffer m_string = new StringBuffer();

  private Value m_loner = null;

  JessTokenStream(DataInputStream paramDataInputStream) {
    this.m_source = new TextInputStream(paramDataInputStream);
    this.m_stream = prepareStream(this.m_source);
    this.m_stack = new Stack();
  }

  private StreamTokenizer prepareStream(InputStream paramInputStream) {
    StreamTokenizer streamTokenizer = new StreamTokenizer(paramInputStream);
    streamTokenizer.commentChar(59);
    streamTokenizer.wordChars(36, 36);
    streamTokenizer.wordChars(42, 42);
    streamTokenizer.wordChars(61, 61);
    streamTokenizer.wordChars(43, 43);
    streamTokenizer.wordChars(47, 47);
    streamTokenizer.wordChars(60, 60);
    streamTokenizer.wordChars(62, 62);
    streamTokenizer.wordChars(95, 95);
    streamTokenizer.wordChars(63, 63);
    streamTokenizer.wordChars(39, 39);
    streamTokenizer.wordChars(35, 35);
    streamTokenizer.wordChars(33, 33);
    streamTokenizer.wordChars(64, 64);
    streamTokenizer.parseNumbers();
    return streamTokenizer;
  }

  int lineno() {
    return this.m_lineno;
  }

  boolean moreTokens() {
    return this.m_stack.empty();
  }

  boolean eof() throws ReteException {
    return (this.m_stack.empty() && !prepareSexp());
  }

  Value loner() {
    return this.m_loner;
  }

  boolean prepareSexp() throws ReteException {
    byte b = 1;
    this.m_string.setLength(0);
    JessToken jessToken = JessToken.create(this.m_stream, this.m_source);
    if (jessToken.m_ttype != 40) {
      if (jessToken.m_ttype != 0) this.m_loner = jessToken.tokenToValue();
      return false;
    }
    this.m_loner = null;
    Stack stack = new Stack();
    stack.push(jessToken);
    while (!b) {
      jessToken = JessToken.create(this.m_stream, this.m_source);
      stack.push(jessToken);
      if (jessToken.m_ttype == 0) return false;
      if (jessToken.m_ttype == 41) {
        b--;
        continue;
      }
      if (jessToken.m_ttype == 40) b++;
    }
    while (!stack.empty()) this.m_stack.push(stack.pop());
    this.m_source.clear();
    return true;
  }

  JessToken nextToken() throws ReteException {
    if (this.m_stack.empty() && !prepareSexp()) return null;
    JessToken jessToken = this.m_stack.pop();
    this.m_string.append(jessToken.toString() + " ");
    this.m_lineno = jessToken.m_lineno;
    return jessToken;
  }

  void pushBack(JessToken paramJessToken) {
    this.m_lineno = paramJessToken.m_lineno;
    this.m_stack.push(paramJessToken);
    this.m_string.setLength(this.m_string.length() - paramJessToken.toString().length() + 1);
  }

  String head() throws ReteException {
    if (this.m_stack.empty() && !prepareSexp()) return null;
    JessToken jessToken1 = this.m_stack.pop();
    JessToken jessToken2 = this.m_stack.peek();
    this.m_stack.push(jessToken1);
    return (jessToken2.m_ttype != 1)
        ? ((jessToken2.m_ttype == 45)
            ? "-"
            : ((jessToken2.m_ttype == 61)
                ? "="
                : ((jessToken2.m_ttype == 8) ? jessToken2.m_sval : jessToken2.toString())))
        : ((jessToken2.m_sval != null) ? jessToken2.m_sval : jessToken2.toString());
  }

  void clear() {
    this.m_stack = new Stack();
    this.m_string.setLength(0);
    this.m_loner = null;
  }

  public String toString() {
    return this.m_string.toString();
  }

  JessToken getOneToken() {
    return JessToken.create(this.m_stream, this.m_source);
  }
}
