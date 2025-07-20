package jess;

import java.util.Enumeration;
import java.util.Hashtable;

public class Funcall extends ValueVector {
  static Value s_true = null;

  static Value s_false = null;

  static Value s_nil = null;

  static Value s_else = null;

  static Value s_do = null;

  static Value s_eof = null;

  private static Hashtable s_intrinsics = new Hashtable(50);

  private static int[] s_ctrl;

  private Userfunction m_funcall;

  static Enumeration listIntrinsics() {
    return s_intrinsics.keys();
  }

  public Funcall(String paramString, Rete paramRete) throws ReteException {
    add(new Value(RU.putAtom(paramString), 1));
    Userfunction userfunction = paramRete.findDeffunction(paramString);
    if (userfunction == null) {
      userfunction = paramRete.findUserfunction(paramString);
      if (userfunction == null)
        userfunction = (Userfunction)s_intrinsics.get(paramString);
    }
    this.m_funcall = userfunction;
  }

  Funcall(int paramInt) {
    super(paramInt);
  }

  public Object clone() {
    Funcall funcall = new Funcall(this.m_ptr);
    funcall.m_ptr = this.m_ptr;
    System.arraycopy(this.m_v, 0, funcall.m_v, 0, this.m_ptr);
    funcall.m_funcall = this.m_funcall;
    return funcall;
  }

  public Funcall cloneInto(Funcall paramFuncall) {
    paramFuncall.setLength(this.m_ptr);
    System.arraycopy(this.m_v, 0, paramFuncall.m_v, 0, this.m_ptr);
    paramFuncall.m_funcall = this.m_funcall;
    return paramFuncall;
  }

  public static final Value TRUE() {
    return s_true;
  }

  public static final Value FALSE() {
    return s_false;
  }

  public static final Value NIL() {
    return s_nil;
  }

  void addArgument(String paramString, int paramInt) throws ReteException {
    addArgument(RU.putAtom(paramString), paramInt);
  }

  void addArgument(int paramInt1, int paramInt2) throws ReteException {
    add(new Value(paramInt1, paramInt2));
  }

  void addArgument(double paramDouble, int paramInt) throws ReteException {
    add(new Value(paramDouble, paramInt));
  }

  void addArgument(Funcall paramFuncall) throws ReteException {
    add(new Value(paramFuncall, 64));
  }

  void addArgument(Fact paramFact) throws ReteException {
    ValueVector valueVector = paramFact.factData();
    add(new Value(valueVector, valueVector.get(1).descriptorValue()));
  }

  void addArgument(ValueVector paramValueVector, int paramInt) throws ReteException {
    add(new Value(paramValueVector, paramInt));
  }

  static final Value execute(Funcall paramFuncall, Context paramContext) throws ReteException {
    return execute(paramFuncall, paramContext, null, null);
  }

  static final Value execute(Funcall paramFuncall, Context paramContext, EvalCache paramEvalCache) throws ReteException {
    return execute(paramFuncall, paramContext, paramEvalCache, null);
  }

  static Value execute(Funcall paramFuncall, Context paramContext, EvalCache paramEvalCache, Value paramValue) throws ReteException {
    boolean bool = (paramEvalCache != null) ? paramEvalCache.markValue() : false;
    int i = paramFuncall.size();
    int j = paramFuncall.get(0).atomValue();
    if (j != s_ctrl[0] && j != s_ctrl[1] && j != s_ctrl[2] && j != s_ctrl[3])
      for (byte b = 1; b < i && (j != s_ctrl[4] || b <= 2); b++) {
        int k = paramFuncall.get(b).type();
        if (k == 64) {
          Funcall funcall = paramFuncall.get(b).funcallValue();
          Value value1 = (paramEvalCache != null) ? paramEvalCache.getValue() : new Value(0, 4);
          boolean bool1 = (paramEvalCache != null) ? paramEvalCache.markValue() : false;
          Value value2 = execute(funcall, paramContext, paramEvalCache, value1);
          paramFuncall.set(value2, b);
          if (paramEvalCache != null)
            paramEvalCache.restoreValue(bool1);
        } else if (k == 256 || k == 128) {
          ValueVector valueVector = paramFuncall.get(b).factValue();
          for (byte b1 = 3; b1 < valueVector.size(); b1++) {
            if (valueVector.get(b1).type() == 64) {
              Funcall funcall = valueVector.get(b1).funcallValue();
              Value value1 = (paramEvalCache != null) ? paramEvalCache.getValue() : new Value(0, 4);
              boolean bool1 = (paramEvalCache != null) ? paramEvalCache.markValue() : false;
              Value value2 = execute(funcall, paramContext, paramEvalCache, value1);
              valueVector.set(value2, b1);
              if (paramEvalCache != null)
                paramEvalCache.restoreValue(bool1);
            }
          }
        } else if (k == 512) {
          ValueVector valueVector = paramFuncall.get(b).listValue();
          for (byte b1 = 1; b1 < valueVector.size(); b1++) {
            if (valueVector.get(b1).type() == 64) {
              Value value1 = (paramEvalCache != null) ? paramEvalCache.getValue() : new Value(0, 4);
              boolean bool1 = (paramEvalCache != null) ? paramEvalCache.markValue() : false;
              Funcall funcall = valueVector.get(b1).funcallValue();
              Value value2 = execute(funcall, paramContext, paramEvalCache, value1);
              valueVector.set(value2, b1);
              if (paramEvalCache != null)
                paramEvalCache.restoreValue(bool1);
            }
          }
          paramFuncall.set(new Value(Context.flattenList(valueVector), 512), b);
        }
      }
    Value value = simpleExecute(paramFuncall, paramContext, paramValue);
    if (paramEvalCache != null)
      paramEvalCache.restoreValue(bool);
    return value;
  }

  public static Value simpleExecute(Funcall paramFuncall, Context paramContext) throws ReteException {
    return simpleExecute(paramFuncall, paramContext, null);
  }

  static Value simpleExecute(Funcall paramFuncall, Context paramContext, Value paramValue) throws ReteException {
    Userfunction userfunction = paramFuncall.m_funcall;
    if (userfunction == null) {
      Rete rete = paramContext.m_engine;
      String str = paramFuncall.get(0).stringValue();
      userfunction = rete.findDeffunction(str);
      if (userfunction == null) {
        userfunction = rete.findUserfunction(str);
        if (userfunction == null)
          userfunction = (Userfunction)s_intrinsics.get(str);
      }
      if (userfunction == null)
        throw new ReteException("Funcall::simpleExecute", "Unimplemented function", paramFuncall.get(0).stringValue());
    }
    return (userfunction instanceof Fastfunction && paramValue != null) ? ((Fastfunction)userfunction).call(paramFuncall, paramContext, paramValue) : userfunction.call(paramFuncall, paramContext);
  }

  String ppFuncall(Rete paramRete) throws ReteException {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("(");
    stringBuffer.append(get(0).stringValue());
    stringBuffer.append(" ");
    for (byte b = 1; b < size(); b++) {
      stringBuffer.append(ppArgument(b, paramRete));
      stringBuffer.append(" ");
    }
    stringBuffer.append(")");
    return stringBuffer.toString();
  }

  private String ppArgument(int paramInt, Rete paramRete) throws ReteException {
    ValueVector valueVector1;
    Fact fact;
    ValueVector valueVector2;
    String str;
    byte b;
    int i = get(paramInt).type();
    switch (i) {
      case 1:
      case 4:
      case 8:
      case 32:
        return get(paramInt).toString();
      case 2:
        return "\"" + get(paramInt).stringValue() + "\"";
      case 512:
        valueVector2 = get(paramInt).listValue();
        str = "";
        for (b = 1; b < valueVector2.size(); b++)
          str = str + ppArgument(b, paramRete) + " ";
        return str;
      case 64:
        valueVector2 = get(paramInt).funcallValue();
        return valueVector2.ppFuncall(paramRete);
      case 128:
      case 256:
        valueVector1 = get(paramInt).factValue();
        fact = new Fact(valueVector1, paramRete);
        return fact.toString();
    }
    return get(paramInt).toString();
  }

  static {
    try {
      s_true = new Value(RU.putAtom("TRUE"), 1);
      s_false = new Value(RU.putAtom("FALSE"), 1);
      s_nil = new Value(RU.putAtom("nil"), 1);
      s_else = new Value(RU.putAtom("else"), 1);
      s_do = new Value(RU.putAtom("do"), 1);
      s_eof = new Value(RU.putAtom("EOF"), 1);
    } catch (ReteException reteException) {
      System.out.println("*** FATAL ***: Can't instantiate constants");
      System.exit(0);
    }
    String[] arrayOfString1 = {
        "_return", "_assert", "_retract", "_retract_string", "_printout", "_extract_global", "_open", "_close", "_foreach", "_read",
        "_readline", "_gensym_star", "_while", "_if", "_bind", "_modify", "_and", "_or", "_not", "_eq",
        "_eqstar", "_equals", "_not_equals", "_gt", "_lt", "_gt_or_eq", "_lt_or_eq", "_neq", "_mod", "_plus",
        "_times", "_minus", "_divide", "_sym_cat", "_reset", "_run", "_facts", "_rules", "_halt", "_exit",
        "_clear", "_watch", "_unwatch", "_jess_version_string", "_jess_version_number", "_load_facts", "_save_facts", "_assert_string", "_undefrule" };
    try {
      for (byte b1 = 0; b1 < arrayOfString1.length; b1++) {
        Userfunction userfunction = (Userfunction)Class.forName("jess." + arrayOfString1[b1]).newInstance();
        s_intrinsics.put(RU.getAtom(userfunction.name()), userfunction);
      }
    } catch (Throwable throwable) {
      System.out.println("*** FATAL ***: Missing intrinsic function class");
      System.exit(0);
    }
    String[] arrayOfString2 = { "if", "while", "and", "or", "foreach" };
    s_ctrl = new int[arrayOfString2.length];
    for (byte b = 0; b < arrayOfString2.length; b++)
      s_ctrl[b] = RU.putAtom(arrayOfString2[b]);
  }
}
