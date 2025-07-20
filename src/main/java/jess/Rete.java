package jess;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Rete {
  private GlobalContext m_globalContext;

  private ReteDisplay m_display;

  private boolean m_watchRules = false;

  private boolean m_watchFacts = false;

  private boolean m_watchCompilations = false;

  private boolean m_watchActivations = false;

  private int m_nextFactId = 0;

  private int m_nextRuleId = 0;

  private Hashtable m_deftemplates = new Hashtable(101);

  private Vector m_deffacts = new Vector();

  private Vector m_defglobals = new Vector();

  private Hashtable m_deffunctions = new Hashtable(101);

  private Hashtable m_userfunctions = new Hashtable(101);

  private Hashtable m_outRouters = new Hashtable(13);

  private Hashtable m_inRouters = new Hashtable(13);

  private Hashtable m_inWrappers = new Hashtable(13);

  private Vector m_facts = new Vector();

  private Hashtable m_rules = new Hashtable();

  private Vector m_activations = new Vector();

  private ReteCompiler m_compiler = new ReteCompiler(this);

  private boolean m_halt;

  private TextInputStream m_tis;

  private Jesp m_jesp;

  private Vector m_clearables = new Vector();

  private Vector m_resetables = new Vector();

  public final GlobalContext globalContext() {
    return this.m_globalContext;
  }

  public final synchronized ReteDisplay display() {
    return this.m_display;
  }

  public final synchronized void watchRules(boolean paramBoolean) {
    this.m_watchRules = paramBoolean;
  }

  public final synchronized boolean watchRules() {
    return this.m_watchRules;
  }

  public final synchronized void watchFacts(boolean paramBoolean) {
    this.m_watchFacts = paramBoolean;
  }

  public final boolean watchFacts() {
    return this.m_watchFacts;
  }

  public final synchronized void watchCompilations(boolean paramBoolean) {
    this.m_watchCompilations = paramBoolean;
  }

  public final boolean watchCompilations() {
    return this.m_watchCompilations;
  }

  public final synchronized void watchActivations(boolean paramBoolean) {
    this.m_watchActivations = paramBoolean;
  }

  public final synchronized boolean watchActivations() {
    return this.m_watchActivations;
  }

  synchronized int nextFactId() {
    return this.m_nextFactId++;
  }

  synchronized int nextRuleId() {
    return this.m_nextRuleId++;
  }

  public synchronized void addInputRouter(String paramString, InputStream paramInputStream) {
    DataInputStream dataInputStream;
    if (!(paramInputStream instanceof DataInputStream)) {
      dataInputStream = new DataInputStream(paramInputStream);
    } else {
      dataInputStream = (DataInputStream)paramInputStream;
    }
    this.m_inRouters.put(paramString, dataInputStream);
    this.m_inWrappers.put(paramInputStream, dataInputStream);
  }

  public synchronized void addOutputRouter(String paramString, OutputStream paramOutputStream) {
    this.m_outRouters.put(paramString, paramOutputStream);
  }

  public synchronized void removeInputRouter(String paramString) {
    this.m_inRouters.remove(paramString);
  }

  public synchronized void removeOutputRouter(String paramString) {
    this.m_outRouters.remove(paramString);
  }

  public synchronized InputStream getInputRouter(String paramString) {
    return (InputStream)this.m_inRouters.get(paramString);
  }

  public synchronized DataInputStream getInputWrapper(InputStream paramInputStream) {
    return (DataInputStream)this.m_inWrappers.get(paramInputStream);
  }

  public synchronized OutputStream getOutputRouter(String paramString) {
    return (OutputStream)this.m_outRouters.get(paramString);
  }

  public final synchronized ReteCompiler compiler() {
    return this.m_compiler;
  }

  public Rete(ReteDisplay paramReteDisplay) {
    this.m_display = paramReteDisplay;
    this.m_globalContext = new GlobalContext(this);
    addInputRouter("t", paramReteDisplay.stdin());
    addOutputRouter("t", paramReteDisplay.stdout());
    addInputRouter("WSTDIN", getInputRouter("t"));
    addOutputRouter("WSTDOUT", getOutputRouter("t"));
    addOutputRouter("WSTDERR", getOutputRouter("t"));
    this.m_tis = new TextInputStream(true);
    this.m_jesp = new Jesp(this.m_tis, this);
  }

  public synchronized PrintStream errStream() {
    PrintStream printStream;
    OutputStream outputStream = getOutputRouter("WSTDERR");
    if (outputStream instanceof PrintStream) {
      printStream = (PrintStream)outputStream;
    } else {
      printStream = new PrintStream(outputStream);
      addOutputRouter("WSTDERR", printStream);
    }
    return printStream;
  }

  public synchronized PrintStream outStream() {
    PrintStream printStream;
    OutputStream outputStream = getOutputRouter("WSTDOUT");
    if (outputStream instanceof PrintStream) {
      printStream = (PrintStream)outputStream;
    } else {
      printStream = new PrintStream(outputStream);
      addOutputRouter("WSTDOUT", printStream);
    }
    return printStream;
  }

  public synchronized void addClearable(Clearable paramClearable) {
    this.m_clearables.addElement(paramClearable);
  }

  public synchronized void removeClearable(Clearable paramClearable) {
    this.m_clearables.removeElement(paramClearable);
  }

  public synchronized void clear() throws ReteException {
    watchRules(false);
    watchFacts(false);
    watchCompilations(false);
    watchActivations(false);
    this.m_halt = false;
    this.m_nextFactId = this.m_nextRuleId = 0;
    this.m_deftemplates.clear();
    this.m_deffacts.removeAllElements();
    this.m_defglobals.removeAllElements();
    this.m_deffunctions.clear();
    Enumeration enumeration = this.m_clearables.elements();
    while (enumeration.hasMoreElements())
      ((Clearable)enumeration.nextElement()).clear();
    this.m_facts.removeAllElements();
    this.m_compiler = new ReteCompiler(this);
    if (this.m_rules.size() != 0)
      this.m_display.addDefrule(this.m_rules.elements().nextElement());
    this.m_rules.clear();
    this.m_activations.removeAllElements();
    System.gc();
  }

  public synchronized void addResetable(Resetable paramResetable) {
    this.m_resetables.addElement(paramResetable);
  }

  public synchronized void removeResetable(Clearable paramClearable) {
    this.m_resetables.removeElement(paramClearable);
  }

  void removeFacts() throws ReteException {
    synchronized (this.m_compiler) {
      Fact fact = new Fact("*CLEAR*", 128, this);
      ValueVector valueVector = fact.factData();
      Token token = new Token(3, valueVector);
      processToken(token);
      System.gc();
      this.m_facts = new Vector();
      this.m_display.retractFact(valueVector);
    }
  }

  public void reset() throws ReteException {
    synchronized (this.m_compiler) {
      removeFacts();
      int i = this.m_defglobals.size();
      for (byte b1 = 0; b1 < i; b1++) {
        Defglobal defglobal = this.m_defglobals.elementAt(b1);
        int j = defglobal.m_bindings.size();
        for (byte b = 0; b < j; b++) {
          Binding binding = defglobal.m_bindings.elementAt(b);
          this.m_globalContext.addGlobalBinding(binding.m_name, binding.m_val);
        }
      }
      this.m_nextFactId = 0;
      Fact fact = new Fact("initial-fact", 128, this);
      assert(fact.factData());
      i = this.m_deffacts.size();
      for (byte b2 = 0; b2 < i; b2++) {
        Deffacts deffacts = this.m_deffacts.elementAt(b2);
        int j = deffacts.m_facts.size();
        for (byte b = 0; b < j; b++) {
          ValueVector valueVector = deffacts.m_facts.elementAt(b);
          assert(valueVector);
        }
      }
      Enumeration enumeration = this.m_resetables.elements();
      while (enumeration.hasMoreElements())
        ((Resetable)enumeration.nextElement()).reset();
    }
  }

  public int assertString(String paramString) throws ReteException {
    try {
      this.m_tis.clear();
      this.m_jesp.clear();
      this.m_tis.appendText(paramString);
      Fact fact = this.m_jesp.parseFact();
      return assert(fact.factData());
    } catch (Exception exception) {
      throw new ReteException("Rete::AssertString", exception.toString(), paramString);
    }
  }

  public int assert(ValueVector paramValueVector) throws ReteException {
    synchronized (this.m_compiler) {
      if (paramValueVector.get(1).descriptorValue() == 128)
        for (byte b = 0; b < paramValueVector.size(); b++) {
          if (paramValueVector.get(b).type() == 512) {
            ValueVector valueVector1 = new ValueVector(paramValueVector.size());
            for (byte b1 = 0; b1 < paramValueVector.size(); b1++) {
              Value value = paramValueVector.get(b1);
              if (value.type() != 512) {
                valueVector1.add(value);
              } else {
                ValueVector valueVector2 = value.listValue();
                for (byte b2 = 0; b2 < valueVector2.size(); b2++)
                  valueVector1.add(valueVector2.get(b2));
              }
            }
            paramValueVector = valueVector1;
            break;
          }
        }
      ValueVector valueVector = findFact(paramValueVector);
      if (valueVector != null)
        return -1;
      paramValueVector.set(new Value(this.m_nextFactId++, 16), 2);
      if (paramValueVector.get(1).descriptorValue() == 256) {
        ValueVector valueVector1 = null;
        for (byte b = 3; b < paramValueVector.size(); b++) {
          if (paramValueVector.get(b).type() == 0) {
            if (valueVector1 == null)
              valueVector1 = findDeftemplate(paramValueVector.get(0).atomValue());
            int i = (b - 3) * 2 + 3 + 1;
            Value value = valueVector1.get(i);
            if (value.type() == 0) {
              paramValueVector.set(Funcall.NIL(), b);
            } else {
              paramValueVector.set(value, b);
            }
          }
        }
      }
      this.m_facts.addElement(paramValueVector);
      Token token = new Token(0, paramValueVector);
      processToken(token);
      this.m_display.assertFact(paramValueVector);
      if (this.m_watchFacts)
        outStream().println(" ==> " + (new Fact(paramValueVector, this)).toString());
      return paramValueVector.get(2).factIDValue();
    }
  }

  public void retractString(String paramString) throws ReteException {
    try {
      this.m_tis.clear();
      this.m_jesp.clear();
      this.m_tis.appendText(paramString);
      Fact fact = this.m_jesp.parseFact();
      retract(fact.factData());
    } catch (Exception exception) {
      throw new ReteException("Rete::retractString", exception.toString(), paramString);
    }
  }

  public void retract(ValueVector paramValueVector) throws ReteException {
    synchronized (this.m_compiler) {
      ValueVector valueVector;
      if ((valueVector = findFact(paramValueVector)) != null)
        _retract(valueVector);
    }
  }

  public void retract(int paramInt) throws ReteException {
    synchronized (this.m_compiler) {
      ValueVector valueVector;
      if ((valueVector = findFactByID(paramInt)) != null)
        _retract(valueVector);
    }
  }

  void _retract(ValueVector paramValueVector) throws ReteException {
    synchronized (this.m_compiler) {
      this.m_facts.removeElement(paramValueVector);
      Token token = new Token(1, paramValueVector);
      processToken(token);
      if (this.m_watchFacts)
        outStream().println(" <==  " + (new Fact(paramValueVector, this)).toString());
      this.m_display.retractFact(paramValueVector);
    }
  }

  ValueVector findFactByID(int paramInt) throws ReteException {
    int i = this.m_facts.size();
    for (byte b = 0; b < i; b++) {
      ValueVector valueVector = this.m_facts.elementAt(b);
      if (valueVector.get(2).factIDValue() == paramInt)
        return valueVector;
    }
    return null;
  }

  private ValueVector findFact(ValueVector paramValueVector) throws ReteException {
    int i = paramValueVector.get(2).factIDValue();
    int j = this.m_facts.size();
    int k = paramValueVector.size();
    for (byte b = 0; b < j; b++) {
      ValueVector valueVector = this.m_facts.elementAt(b);
      if (k == valueVector.size() && paramValueVector.get(0).equals(valueVector.get(0)))
        for (byte b1 = 3;; b1++) {
          if (b1 >= k)
            return valueVector;
          if (!paramValueVector.get(b1).equals(valueVector.get(b1)))
            break;
        }
    }
    return null;
  }

  public synchronized void showRules() {
    Enumeration enumeration = this.m_rules.elements();
    while (enumeration.hasMoreElements()) {
      Defrule defrule = enumeration.nextElement();
      outStream().println(defrule.toString());
    }
    outStream().println("For a total of " + this.m_rules.size() + " rules.");
  }

  public synchronized String ppFacts(int paramInt) {
    int i = this.m_facts.size();
    StringBuffer stringBuffer = new StringBuffer(100);
    Fact fact = null;
    for (byte b = 0; b < i; b++) {
      ValueVector valueVector = this.m_facts.elementAt(b);
      try {
        if (valueVector.get(0).atomValue() == paramInt) {
          fact = new Fact(valueVector, this);
          stringBuffer.append(fact);
          stringBuffer.append("\n");
        }
      } catch (ReteException reteException) {}
    }
    return stringBuffer.toString();
  }

  public synchronized String ppFacts() throws ReteException {
    int i = this.m_facts.size();
    StringBuffer stringBuffer = new StringBuffer(1024);
    Fact fact = null;
    for (byte b = 0; b < i; b++) {
      ValueVector valueVector = this.m_facts.elementAt(b);
      fact = new Fact(valueVector, this);
      stringBuffer.append(fact);
      stringBuffer.append("\n");
    }
    return stringBuffer.toString();
  }

  public void showFacts() throws ReteException {
    synchronized (this.m_compiler) {
      int i = this.m_facts.size();
      for (byte b = 0; b < i; b++) {
        ValueVector valueVector = this.m_facts.elementAt(b);
        Fact fact = new Fact(valueVector, this);
        outStream().print("f-");
        outStream().print(valueVector.get(2).factIDValue() + "   ");
        outStream().println(fact.toString());
      }
      outStream().println("For a total of " + i + " facts.");
    }
  }

  public synchronized Enumeration listDeffacts() {
    return this.m_deffacts.elements();
  }

  public synchronized Enumeration listDeftemplates() {
    return this.m_deftemplates.elements();
  }

  public synchronized Enumeration listDefrules() {
    return this.m_rules.elements();
  }

  public synchronized Enumeration listFacts() {
    return this.m_facts.elements();
  }

  public synchronized Enumeration listActivations() {
    return this.m_activations.elements();
  }

  public synchronized Enumeration listDefglobals() {
    return this.m_defglobals.elements();
  }

  public synchronized Enumeration listDeffunctions() {
    return this.m_deffunctions.elements();
  }

  public synchronized Enumeration listUserfunctions() {
    return this.m_userfunctions.elements();
  }

  private boolean processTokenOneNode(Token paramToken, Node paramNode) throws ReteException {
    synchronized (this.m_compiler) {
      return paramNode.callNode(paramToken, 2);
    }
  }

  private boolean processToken(Token paramToken) throws ReteException {
    synchronized (this.m_compiler) {
      Vector vector = this.m_compiler.roots();
      this.m_compiler.freeze();
      int i = vector.size();
      for (byte b = 0; b < i; b++) {
        Node1 node1 = (Node1)((Successor)vector.elementAt(b)).m_node;
        if (processTokenOneNode(paramToken, node1))
          return true;
      }
      return false;
    }
  }

  void updateNode(Node paramNode) throws ReteException {
    for (byte b = 0; b < this.m_facts.size(); b++) {
      Token token = new Token(2, this.m_facts.elementAt(b));
      processTokenOneNode(token, paramNode);
    }
  }

  void updateNodes(Node[] paramArrayOfNode) throws ReteException {
    this.m_compiler.freeze();
    for (byte b = 0; b < this.m_facts.size(); b++) {
      Token token = new Token(2, this.m_facts.elementAt(b));
      for (byte b1 = 0; b1 < paramArrayOfNode.length; b1++)
        processTokenOneNode(token, paramArrayOfNode[b1]);
    }
  }

  public final Defrule findDefrule(String paramString) {
    return (Defrule)this.m_rules.get(paramString);
  }

  public final Defrule findDefrule(int paramInt) {
    String str = RU.getAtom(paramInt);
    return findDefrule(str);
  }

  public ValueVector findDeftemplate(String paramString) {
    Deftemplate deftemplate = (Deftemplate)this.m_deftemplates.get(paramString);
    return (deftemplate != null) ? deftemplate.deftemplateData() : null;
  }

  Deftemplate findFullDeftemplate(String paramString) {
    return (Deftemplate)this.m_deftemplates.get(paramString);
  }

  public ValueVector findDeftemplate(int paramInt) {
    String str = RU.getAtom(paramInt);
    return findDeftemplate(str);
  }

  public synchronized ValueVector addDeftemplate(Deftemplate paramDeftemplate) throws ReteException {
    ValueVector valueVector = paramDeftemplate.deftemplateData();
    String str = valueVector.get(0).stringValue();
    if (this.m_deftemplates.get(str) == null) {
      this.m_deftemplates.put(str, paramDeftemplate);
      this.m_display.addDeftemplate(paramDeftemplate);
    }
    return valueVector;
  }

  public synchronized Deffacts addDeffacts(Deffacts paramDeffacts) throws ReteException {
    try {
      for (byte b = 0; b < this.m_deffacts.size(); b++) {
        if (paramDeffacts.m_name == ((Deffacts)this.m_deffacts.elementAt(b)).m_name) {
          this.m_deffacts.setElementAt(paramDeffacts, b);
          return paramDeffacts;
        }
      }
      this.m_deffacts.addElement(paramDeffacts);
      return paramDeffacts;
    } finally {
      this.m_display.addDeffacts(paramDeffacts);
    }
  }

  public synchronized Defglobal addDefglobal(Defglobal paramDefglobal) throws ReteException {
    this.m_defglobals.addElement(paramDefglobal);
    int i = paramDefglobal.m_bindings.size();
    for (byte b = 0; b < i; b++) {
      Binding binding = paramDefglobal.m_bindings.elementAt(b);
      this.m_globalContext.addGlobalBinding(binding.m_name, binding.m_val);
    }
    return paramDefglobal;
  }

  public synchronized Deffunction addDeffunction(Deffunction paramDeffunction) throws ReteException {
    String str = RU.getAtom(paramDeffunction.name());
    this.m_deffunctions.put(str, paramDeffunction);
    return paramDeffunction;
  }

  public Deffunction findDeffunction(String paramString) {
    return (Deffunction)this.m_deffunctions.get(paramString);
  }

  public Deffunction findDeffunction(int paramInt) {
    String str = RU.getAtom(paramInt);
    return findDeffunction(str);
  }

  public synchronized Userfunction addUserfunction(Userfunction paramUserfunction) {
    String str = RU.getAtom(paramUserfunction.name());
    this.m_userfunctions.put(str, paramUserfunction);
    return paramUserfunction;
  }

  public synchronized Userpackage addUserpackage(Userpackage paramUserpackage) {
    paramUserpackage.add(this);
    return paramUserpackage;
  }

  public Userfunction findUserfunction(String paramString) {
    return (Userfunction)this.m_userfunctions.get(paramString);
  }

  public Userfunction findUserfunction(int paramInt) {
    String str = RU.getAtom(paramInt);
    return findUserfunction(str);
  }

  public final Defrule addDefrule(Defrule paramDefrule) throws ReteException {
    synchronized (this.m_compiler) {
      unDefrule(paramDefrule.name());
      this.m_compiler.addRule(paramDefrule);
      this.m_rules.put(RU.getAtom(paramDefrule.name()), paramDefrule);
      this.m_display.addDefrule(paramDefrule);
      return paramDefrule;
    }
  }

  public final Value unDefrule(int paramInt) throws ReteException {
    synchronized (this.m_compiler) {
      Defrule defrule = findDefrule(paramInt);
      if (defrule != null) {
        defrule.remove(this.m_compiler.roots());
        this.m_rules.remove(RU.getAtom(paramInt));
        for (byte b = 0; b < this.m_activations.size(); b++) {
          Activation activation = this.m_activations.elementAt(b);
          if (activation.m_rule == defrule) {
            standDown(activation);
            b--;
          }
        }
        this.m_display.addDefrule(defrule);
        return Funcall.TRUE();
      }
    }
    return Funcall.FALSE();
  }

  void addActivation(Activation paramActivation) throws ReteException {
    this.m_display.activateRule(paramActivation.m_rule);
    this.m_activations.addElement(paramActivation);
    if (this.m_watchActivations)
      outStream().println("==> Activation: " + RU.getAtom(paramActivation.m_rule.m_name) + " : " + factList(paramActivation.m_token));
  }

  void standDown(Activation paramActivation) throws ReteException {
    ruleFired(paramActivation);
    if (this.m_watchActivations)
      outStream().println("<== Activation: " + RU.getAtom(paramActivation.m_rule.m_name) + " : " + factList(paramActivation.m_token));
  }

  void ruleFired(Activation paramActivation) {
    this.m_display.deactivateRule(paramActivation.m_rule);
    this.m_activations.removeElement(paramActivation);
  }

  static String factList(Token paramToken) throws ReteException {
    StringBuffer stringBuffer = new StringBuffer(100);
    boolean bool = true;
    for (byte b = 0; b < paramToken.size(); b++) {
      if (!bool)
        stringBuffer.append(", ");
      int i = paramToken.fact(b).get(2).factIDValue();
      if (i != -1) {
        stringBuffer.append("f-");
        stringBuffer.append(i);
      }
      bool = false;
    }
    return stringBuffer.toString();
  }

  public int run() throws ReteException {
    int j;
    int i = 0;
    do {
      j = run(2147483647);
      i += j;
    } while (j > 0 && !this.m_halt);
    return i;
  }

  public int run(int paramInt) throws ReteException {
    byte b = 0;
    int i = 0;
    this.m_halt = false;
    synchronized (this) {
      i = this.m_activations.size();
    }
    while (i > 0 && !this.m_halt && b < paramInt) {
      synchronized (this) {
        int j = 0;
        int k = Integer.MIN_VALUE;
        for (int m = i - 1; m > -1; m--) {
          int n = ((Activation)this.m_activations.elementAt(m)).m_rule.m_salience;
          if (n > k) {
            k = n;
            j = m;
          }
        }
        Activation activation = this.m_activations.elementAt(j);
        activation.fire();
        b++;
        i = this.m_activations.size();
      }
    }
    return b;
  }

  public synchronized Value executeCommand(String paramString) throws ReteException {
    this.m_tis.clear();
    this.m_jesp.clear();
    this.m_tis.appendText(paramString);
    return this.m_jesp.parse(false);
  }

  public synchronized void halt() {
    this.m_halt = true;
  }
}
