package jess;

public class ReteException extends Exception {
  String m_routine;

  String m_text1;

  String m_text2;

  public ReteException(String paramString1, String paramString2, String paramString3) {
    this.m_routine = paramString1;
    this.m_text1 = paramString2;
    this.m_text2 = paramString3;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("Rete Exception in routine ");
    stringBuffer.append(this.m_routine);
    stringBuffer.append(".\n");
    stringBuffer.append("  Message: ");
    stringBuffer.append(this.m_text1);
    stringBuffer.append(" ");
    stringBuffer.append(this.m_text2);
    stringBuffer.append(".");
    return stringBuffer.toString();
  }
}
