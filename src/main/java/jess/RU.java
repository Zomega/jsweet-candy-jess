package jess;

public class RU {
  public static final int CLASS = 0;

  public static final int DESC = 1;

  public static final int ID = 2;

  public static final int FIRST_SLOT = 3;

  public static final int NOT_CE = 1;

  public static final int AND_CE = 2;

  public static final int OR_CE = 3;

  public static final int DT_SLOT_NAME = 0;

  public static final int DT_DFLT_DATA = 1;

  public static final int DT_SLOT_SIZE = 2;

  public static final int NONE = 0;

  public static final int ATOM = 1;

  public static final int STRING = 2;

  public static final int INTEGER = 4;

  public static final int VARIABLE = 8;

  public static final int FACT_ID = 16;

  public static final int FLOAT = 32;

  public static final int FUNCALL = 64;

  public static final int ORDERED_FACT = 128;

  public static final int UNORDERED_FACT = 256;

  public static final int LIST = 512;

  public static final int DESCRIPTOR = 1024;

  public static final int EXTERNAL_ADDRESS = 2048;

  public static final int INTARRAY = 4096;

  public static final int MULTIVARIABLE = 8192;

  public static final int SLOT = 16384;

  public static final int MULTISLOT = 32768;

  static final int ADD = 0;

  static final int REMOVE = 1;

  static final int UPDATE = 2;

  static final int CLEAR = 3;

  static final int PATTERN = -1;

  static final int LOCAL = -2;

  static final int GLOBAL = -3;

  static final int MAXFIELDS = 32;

  private static JessHashtable s_atoms = new JessHashtable();

  static int s_gensymIdx = 0;

  public static synchronized int putAtom(String paramString) {
    int i = paramString.hashCode();
    String str = null;
    boolean bool = true;
    while (bool) {
      str = s_atoms.get(i);
      if (str != null && str.equals(paramString)) return i;
      if (str == null) {
        s_atoms.put(i, paramString);
        return i;
      }
      i++;
    }
    return 0;
  }

  public static String getAtom(int paramInt) {
    return s_atoms.get(paramInt);
  }

  public static synchronized int gensym(String paramString) {
    for (String str = paramString + s_gensymIdx;
        getAtom(str.hashCode()) != null;
        str = paramString + s_gensymIdx) s_gensymIdx++;
    return putAtom(str);
  }
}
