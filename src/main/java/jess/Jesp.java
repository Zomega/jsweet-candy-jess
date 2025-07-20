package jess;

import java.io.DataInputStream;
import java.io.InputStream;

public class Jesp {
  private final String JAVACALL = "call";

  private JessTokenStream m_jts;

  private Rete m_engine;

  public Jesp(InputStream paramInputStream, Rete paramRete) {
    DataInputStream dataInputStream = paramRete.getInputWrapper(paramInputStream);
    if (dataInputStream == null)
      dataInputStream = new DataInputStream(paramInputStream);
    this.m_jts = new JessTokenStream(dataInputStream);
    this.m_engine = paramRete;
  }

  public synchronized Value parse(boolean paramBoolean) throws ReteException {
    Value value = Funcall.TRUE();
    if (paramBoolean) {
      this.m_engine.outStream().print("Jess> ");
      this.m_engine.outStream().flush();
    }
    while (!this.m_jts.eof() && value != null) {
      value = parseSexp();
      if (paramBoolean) {
        if (!value.equals(Funcall.NIL())) {
          if (value.type() == 512)
            this.m_engine.outStream().print('(');
          this.m_engine.outStream().print(value);
          if (value.type() == 512)
            this.m_engine.outStream().print(')');
          this.m_engine.outStream().println();
        }
        this.m_engine.outStream().print("Jess> ");
        this.m_engine.outStream().flush();
      }
    }
    if (this.m_jts.eof() && this.m_jts.loner() != null)
      value = this.m_jts.loner();
    return value;
  }

  public void clear() {
    this.m_jts.clear();
  }

  Value loadFacts() throws ReteException {
    Value value = Funcall.TRUE();
    while (!this.m_jts.eof()) {
      Fact fact = parseFact();
      if (fact == null)
        break;
      this.m_engine.assert(fact.factData());
    }
    return value;
  }

  private Value parseSexp() throws ReteException {
    try {
      String str = this.m_jts.head();
      if (str.equals("defrule"))
        return parseDefrule();
      if (str.equals("deffacts"))
        return parseDeffacts();
      if (str.equals("deftemplate"))
        return parseDeftemplate();
      if (str.equals("deffunction"))
        return parseDeffunction();
      if (str.equals("defglobal"))
        return parseDefglobal();
      Funcall funcall1 = parseFuncall();
      this.m_engine.globalContext().push();
      Funcall funcall2 = this.m_engine.globalContext().expandAction(funcall1);
      Value value = Funcall.execute(funcall2, this.m_engine.globalContext());
      this.m_engine.globalContext().pop();
      return value;
    } catch (ReteException reteException) {
      if (reteException instanceof ParseException)
        throw reteException;
      reteException.m_text1 += " " + reteException.m_text2 + " at line " + this.m_jts.lineno() + ": ";
      reteException.m_text2 = this.m_jts.toString();
      this.m_jts.clear();
      throw reteException;
    }
  }

  private Value parseDefglobal() throws ReteException {
    Defglobal defglobal = new Defglobal();
    if ((this.m_jts.nextToken()).m_ttype != 40 || !(this.m_jts.nextToken()).m_sval.equals("defglobal"))
      parseError("parseDefglobal", "Expected (defglobal...");
    JessToken jessToken;
    while ((jessToken = this.m_jts.nextToken()).m_ttype != 41) {
      Funcall funcall1;
      Funcall funcall2;
      Value value;
      if (jessToken.m_ttype != 8)
        parseError("parseDefglobal", "Expected a variable name");
      if (jessToken.m_sval.charAt(0) != '*' || jessToken.m_sval.charAt(jessToken.m_sval.length() - 1) != '*')
        parseError("parseDefglobal", "Defglobal names must start and end with an asterisk!");
      if ((this.m_jts.nextToken()).m_ttype != 61)
        parseError("parseDefglobal", "Expected =");
      JessToken jessToken1 = this.m_jts.nextToken();
      switch (jessToken1.m_ttype) {
        case 1:
        case 2:
        case 8:
          defglobal.addGlobal(jessToken.m_sval, new Value(jessToken1.m_sval, jessToken1.m_ttype));
          continue;
        case 4:
        case 32:
          defglobal.addGlobal(jessToken.m_sval, new Value(jessToken1.m_nval, jessToken1.m_ttype));
          continue;
        case 40:
          this.m_jts.pushBack(jessToken1);
          funcall1 = parseFuncall();
          this.m_engine.globalContext().push();
          funcall2 = this.m_engine.globalContext().expandAction(funcall1);
          value = Funcall.execute(funcall2, this.m_engine.globalContext());
          this.m_engine.globalContext().pop();
          defglobal.addGlobal(jessToken.m_sval, value);
          continue;
      }
      parseError("parseDefglobal", "Bad value");
    }
    this.m_engine.addDefglobal(defglobal);
    return Funcall.TRUE();
  }

  private Funcall parseFuncall() throws ReteException {
    String str = null;
    if ((this.m_jts.nextToken()).m_ttype != 40)
      parseError("parseFuncall", "Expected '('");
    JessToken jessToken = this.m_jts.nextToken();
    switch (jessToken.m_ttype) {
      case 1:
        str = jessToken.m_sval;
        break;
      case 45:
      case 47:
      case 61:
        str = "" + (char)jessToken.m_ttype;
        break;
      case 8:
        str = "call";
        break;
      default:
        parseError("parseFuncall", "Bad functor");
        break;
    }
    Funcall funcall = new Funcall(str, this.m_engine);
    if (jessToken.m_ttype == 8)
      funcall.addArgument(jessToken.m_sval, 8);
    for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype != 41; jessToken = this.m_jts.nextToken()) {
      Funcall funcall1;
      switch (jessToken.m_ttype) {
        case 1:
        case 2:
        case 8:
        case 8192:
          funcall.addArgument(jessToken.m_sval, jessToken.m_ttype);
          break;
        case 4:
        case 32:
          funcall.addArgument(jessToken.m_nval, jessToken.m_ttype);
          break;
        case 40:
          this.m_jts.pushBack(jessToken);
          if (str.equals("assert")) {
            Fact fact = parseFact();
            funcall.addArgument(fact);
            break;
          }
          if (str.equals("modify")) {
            ValueVector valueVector = parseValuePair();
            funcall.addArgument(valueVector, 512);
            break;
          }
          funcall1 = parseFuncall();
          funcall.addArgument(funcall1);
          break;
        default:
          funcall.addArgument(String.valueOf((char)jessToken.m_ttype), 2);
          break;
      }
    }
    return funcall;
  }

  private ValueVector parseValuePair() throws ReteException {
    ValueVector valueVector1 = new ValueVector(2);
    JessToken jessToken = null;
    if ((this.m_jts.nextToken()).m_ttype != 40 || (jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parseValuePair", "Expected '( <atom>'");
    valueVector1.add(new Value(jessToken.m_sval, 1));
    Value value = null;
    ValueVector valueVector2 = null;
    while (true) {
      Funcall funcall;
      if (value != null) {
        if (valueVector2 == null)
          valueVector2 = new ValueVector();
        valueVector2.add(value);
      }
      switch ((jessToken = this.m_jts.nextToken()).m_ttype) {
        case 1:
        case 2:
        case 8:
        case 8192:
          value = new Value(jessToken.m_sval, jessToken.m_ttype);
          break;
        case 4:
        case 32:
          value = new Value(jessToken.m_nval, jessToken.m_ttype);
          break;
        case 40:
          this.m_jts.pushBack(jessToken);
          funcall = parseFuncall();
          value = new Value(funcall, 64);
          break;
        case 41:
          break;
        default:
          parseError("parseValuePair", "Bad argument");
          break;
      }
      if (jessToken.m_ttype == 41) {
        if (valueVector2 != null) {
          valueVector1.add(new Value(valueVector2, 512));
        } else {
          valueVector1.add(value);
        }
        return valueVector1;
      }
    }
  }

  private Value parseDeffacts() throws ReteException {
    Deffacts deffacts = null;
    JessToken jessToken = null;
    if ((this.m_jts.nextToken()).m_ttype != 40 || (jessToken = this.m_jts.nextToken()).m_ttype != 1 || !jessToken.m_sval.equals("deffacts"))
      parseError("parseDeffacts", "Expected '( deffacts'");
    if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parseDeffacts", "Expected deffacts name");
    deffacts = new Deffacts(jessToken.m_sval);
    jessToken = this.m_jts.nextToken();
    if (jessToken.m_ttype == 2) {
      deffacts.m_docstring = jessToken.m_sval;
      jessToken = this.m_jts.nextToken();
    }
    while (jessToken.m_ttype == 40) {
      this.m_jts.pushBack(jessToken);
      Fact fact = parseFact();
      deffacts.addFact(this.m_engine.globalContext().expandFact(fact.factData()));
      jessToken = this.m_jts.nextToken();
    }
    if (jessToken.m_ttype != 41)
      parseError("parseDeffacts", "Expected ')'");
    this.m_engine.addDeffacts(deffacts);
    return Funcall.TRUE();
  }

  Fact parseFact() throws ReteException {
    Fact fact;
    int i;
    JessToken jessToken = null;
    if ((this.m_jts.nextToken()).m_ttype != 40 || (jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parseFact", "Expected '( <atom>'");
    String str = jessToken.m_sval;
    ValueVector valueVector = this.m_engine.findDeftemplate(str);
    if (valueVector == null) {
      i = 128;
    } else {
      i = valueVector.get(1).descriptorValue();
    }
    if (i == 256) {
      fact = new Fact(str, 256, this.m_engine);
      for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype != 41; jessToken = this.m_jts.nextToken()) {
        Funcall funcall;
        ValueVector valueVector1;
        if (jessToken.m_ttype != 40)
          parseError("parseFact", "Expected '('");
        if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
          parseError("parseFact", "Bad slot name");
        String str1 = jessToken.m_sval;
        int j = Deftemplate.slotType(valueVector, str1);
        switch (j) {
          case 16384:
            switch ((jessToken = this.m_jts.nextToken()).m_ttype) {
              case 1:
              case 2:
              case 8:
              case 8192:
                fact.addValue(str1, jessToken.m_sval, jessToken.m_ttype);
                break;
              case 4:
              case 32:
                fact.addValue(str1, jessToken.m_nval, jessToken.m_ttype);
                break;
              case 61:
                jessToken = this.m_jts.nextToken();
                if (jessToken.m_ttype != 40)
                  throw new ReteException("Jesp::parseFact", "'=' cannot appear as an atom within a fact", "");
              case 40:
                this.m_jts.pushBack(jessToken);
                funcall = parseFuncall();
                fact.addValue(str1, funcall, 64);
                break;
              default:
                parseError("parseFact", "Bad slot value");
                break;
            }
            if ((jessToken = this.m_jts.nextToken()).m_ttype != 41)
              parseError("parseFact", "Expected ')'");
            break;
          case 32768:
            valueVector1 = new ValueVector();
            for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype != 41; jessToken = this.m_jts.nextToken()) {
              Funcall funcall1;
              switch (jessToken.m_ttype) {
                case 1:
                case 2:
                case 8:
                case 8192:
                  valueVector1.add(new Value(jessToken.m_sval, jessToken.m_ttype));
                  break;
                case 4:
                case 32:
                  valueVector1.add(new Value(jessToken.m_nval, jessToken.m_ttype));
                  break;
                case 61:
                  jessToken = this.m_jts.nextToken();
                  if (jessToken.m_ttype != 40)
                    throw new ReteException("Jesp::parseFact", "'=' cannot appear as an atom within a fact", "");
                case 40:
                  this.m_jts.pushBack(jessToken);
                  funcall1 = parseFuncall();
                  valueVector1.add(new Value(funcall1, 64));
                  break;
                default:
                  parseError("parseFact", "Bad slot value");
                  break;
              }
            }
            fact.addValue(str1, new Value(valueVector1, 512));
            break;
          default:
            parseError("parseFact", "No such slot in deftemplate");
            break;
        }
      }
    } else {
      fact = new Fact(str, 128, this.m_engine);
      for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype != 41; jessToken = this.m_jts.nextToken()) {
        Funcall funcall;
        switch (jessToken.m_ttype) {
          case 1:
          case 2:
          case 8:
          case 8192:
            fact.addValue(jessToken.m_sval, jessToken.m_ttype);
            break;
          case 4:
          case 32:
            fact.addValue(jessToken.m_nval, jessToken.m_ttype);
            break;
          case 61:
            jessToken = this.m_jts.nextToken();
            if (jessToken.m_ttype != 40)
              throw new ReteException("Jesp::parseFact", "'=' cannot appear as an atom within a fact", "");
          case 40:
            this.m_jts.pushBack(jessToken);
            funcall = parseFuncall();
            fact.addValue(funcall, 64);
            break;
          default:
            fact.addValue(jessToken.toString(), 1);
            break;
        }
      }
    }
    if (jessToken.m_ttype != 41)
      parseError("parseFact", "Expected ')'");
    return fact;
  }

  private Value parseDeftemplate() throws ReteException {
    char c = '䀀';
    Value value = null;
    if ((this.m_jts.nextToken()).m_ttype != 40 || !(this.m_jts.nextToken()).m_sval.equals("deftemplate"))
      parseError("parseDeftemplate", "Expected (deftemplate...");
    JessToken jessToken;
    if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parseDeftemplate", "Expected deftemplate name");
    String str = jessToken.m_sval;
    Deftemplate deftemplate = new Deftemplate(jessToken.m_sval, 256);
    if ((jessToken = this.m_jts.nextToken()).m_ttype == 2) {
      deftemplate.m_docstring = jessToken.m_sval;
      jessToken = this.m_jts.nextToken();
    }
    while (jessToken.m_ttype == 40) {
      if ((jessToken = this.m_jts.nextToken()).m_ttype != 1 || (!jessToken.m_sval.equals("slot") && !jessToken.m_sval.equals("multislot")))
        parseError("parseDeftemplate", "Bad slot type");
      c = jessToken.m_sval.equals("slot") ? '䀀' : '耀';
      if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
        parseError("parseDeftemplate", "Bad slot name");
      str = jessToken.m_sval;
      value = new Value(0, 0);
      for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype == 40; jessToken = this.m_jts.nextToken()) {
        if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
          parseError("parseDeftemplate", "Slot qualifier must be atom");
        if (jessToken.m_sval.equalsIgnoreCase("default")) {
          jessToken = this.m_jts.nextToken();
          switch (jessToken.m_ttype) {
            case 1:
            case 2:
              value = new Value(jessToken.m_sval, jessToken.m_ttype);
              break;
            case 4:
            case 32:
              value = new Value(jessToken.m_nval, jessToken.m_ttype);
              break;
            default:
              parseError("parseDeftemplate", "Illegal default slot value");
              break;
          }
        } else if (jessToken.m_sval.equalsIgnoreCase("type")) {
          jessToken = this.m_jts.nextToken();
        } else {
          parseError("parseDeftemplate", "Unimplemented slot qualifier");
        }
        if ((jessToken = this.m_jts.nextToken()).m_ttype != 41)
          parseError("parseDeftemplate", "Expected ')'");
      }
      if (jessToken.m_ttype != 41)
        parseError("parseDeftemplate", "Expected ')'");
      if (c == '䀀') {
        deftemplate.addSlot(str, value);
      } else {
        deftemplate.addMultiSlot(str, new Value(new ValueVector(), 512));
      }
      jessToken = this.m_jts.nextToken();
    }
    if (jessToken.m_ttype != 41)
      parseError("parseDeftemplate", "Expected ')'");
    this.m_engine.addDeftemplate(deftemplate);
    return Funcall.TRUE();
  }

  private Value parseDefrule() throws ReteException {
    return doParseDefrule();
  }

  private Value doParseDefrule() throws ReteException {
    if ((this.m_jts.nextToken()).m_ttype != 40 || !(this.m_jts.nextToken()).m_sval.equals("defrule"))
      parseError("parseDefrule", "Expected (defrule...");
    JessToken jessToken;
    if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parseDefrule", "Expected defrule name");
    Defrule defrule = new Defrule(jessToken.m_sval, this.m_engine);
    if ((jessToken = this.m_jts.nextToken()).m_ttype == 2) {
      defrule.m_docstring = jessToken.m_sval;
      jessToken = this.m_jts.nextToken();
    }
    byte b = 0;
    if (jessToken.m_ttype == 40) {
      JessToken jessToken1;
      if ((jessToken1 = this.m_jts.nextToken()).m_ttype == 1 && jessToken1.m_sval.equals("declare")) {
        if ((jessToken1 = this.m_jts.nextToken()).m_ttype != 40 || (jessToken1 = this.m_jts.nextToken()).m_ttype != 1 || !jessToken1.m_sval.equals("salience"))
          parseError("parseDefrule", "Expected (salience ...");
        if ((jessToken1 = this.m_jts.nextToken()).m_ttype != 4)
          parseError("parseDefrule", "Expected <integer>");
        defrule.m_salience = (int)jessToken1.m_nval;
        if ((this.m_jts.nextToken()).m_ttype != 41 || (this.m_jts.nextToken()).m_ttype != 41)
          parseError("parseDefrule", "Expected '))('");
        jessToken = this.m_jts.nextToken();
      } else {
        this.m_jts.pushBack(jessToken1);
      }
    }
    while (true) {
      Pattern pattern;
      String str;
      if (jessToken.m_ttype != 40 && jessToken.m_ttype != 8) {
        if (!b) {
          Pattern pattern1 = new Pattern("initial-fact", 128, this.m_engine, 0);
          defrule.addPattern(pattern1);
        }
        if (jessToken.m_ttype != 1 || !jessToken.m_sval.equals("=>"))
          parseError("parseDefrule", "Expected '=>'");
        for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype == 40; jessToken = this.m_jts.nextToken()) {
          this.m_jts.pushBack(jessToken);
          Funcall funcall = parseFuncall();
          defrule.addAction(funcall);
        }
        if (jessToken.m_ttype != 41)
          parseError("parseDefrule", "Expected ')'");
        this.m_engine.addDefrule(defrule);
        return Funcall.TRUE();
      }
      switch (jessToken.m_ttype) {
        case 40:
          this.m_jts.pushBack(jessToken);
          pattern = parsePattern(0);
          if (pattern.negated() > 1)
            parseError("parseDefrule", "Nested not CEs are not allowed yet.");
          if (!b && (pattern.negated() != 0 || pattern.name() == RU.putAtom("test")))
            if (pattern.m_hasVariables) {
              parseError("parseDefrule", "First pattern in rule is a 'not' or 'test' CE containing variables");
            } else {
              defrule.addPattern(new Pattern("initial-fact", 128, this.m_engine, 0));
              b++;
            }
          defrule.addPattern(pattern);
          b++;
          break;
        case 8:
          defrule.addBinding(RU.putAtom(jessToken.m_sval), b, -1, -1);
          if ((jessToken = this.m_jts.nextToken()).m_ttype != 1 || !jessToken.m_sval.equals("<-"))
            parseError("parseDefrule", "Expected '<-'");
          pattern = parsePattern(0);
          str = (this.m_engine.findFullDeftemplate(RU.getAtom(pattern.name()))).m_docstring;
          if (pattern.negated() != 0)
            parseError("parseDefrule", "'not' and 'test' CE's cannot be bound to variables");
          defrule.addPattern(pattern);
          b++;
          break;
      }
      jessToken = this.m_jts.nextToken();
    }
  }

  private Pattern parsePattern(int paramInt) throws ReteException {
    JessToken jessToken = null;
    if ((this.m_jts.nextToken()).m_ttype != 40 || (jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parsePattern", "Expected '( <atom>'");
    String str = jessToken.m_sval;
    if (str.equals("not")) {
      Pattern pattern1 = parsePattern(paramInt + 1);
      if ((this.m_jts.nextToken()).m_ttype != 41)
        parseError("parsePattern", "Expected ')'");
      return pattern1;
    }
    if (str.equals("test")) {
      Pattern pattern1 = new Pattern(str, 128, this.m_engine, 0);
      Funcall funcall = parseFuncall();
      pattern1.addTest(new Value(funcall, 64), (paramInt % 2 == 1));
      pattern1.advance();
      if ((this.m_jts.nextToken()).m_ttype != 41)
        parseError("parsePattern", "Expected ')'");
      return pattern1;
    }
    ValueVector valueVector = this.m_engine.findDeftemplate(str);
    boolean bool = (valueVector == null) ? true : valueVector.get(1).descriptorValue();
    if (bool == 'Ā') {
      Pattern pattern1 = new Pattern(str, 256, this.m_engine, paramInt);
      for (jessToken = this.m_jts.nextToken(); jessToken.m_ttype == 40; jessToken = this.m_jts.nextToken()) {
        if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
          parseError("parsePattern", "Bad slot name");
        String str1 = jessToken.m_sval;
        boolean bool1 = (Deftemplate.slotType(valueVector, str1) == 32768) ? true : false;
        jessToken = this.m_jts.nextToken();
        byte b;
        for (b = bool1 ? 0 : -1; jessToken.m_ttype != 41; b++) {
          Funcall funcall1;
          Funcall funcall2;
          int i;
          boolean bool2 = false;
          if (jessToken.m_ttype == 126) {
            bool2 = true;
            jessToken = this.m_jts.nextToken();
          }
          switch (jessToken.m_ttype) {
            case 8:
            case 8192:
              if (!jessToken.isBlankVariable())
                pattern1.m_hasVariables = true;
            case 1:
            case 2:
              pattern1.addTest(str1, new Value(jessToken.m_sval, jessToken.m_ttype), b, bool2);
              break;
            case 4:
            case 32:
              pattern1.addTest(str1, new Value(jessToken.m_nval, jessToken.m_ttype), b, bool2);
              break;
            case 58:
              funcall1 = parseFuncall();
              pattern1.addTest(str1, new Value(funcall1, 64), b, bool2);
              break;
            case 61:
              funcall1 = parseFuncall();
              funcall2 = new Funcall("eq*", this.m_engine);
              i = RU.gensym("_=_");
              pattern1.addTest(str1, new Value(i, 8), b, false);
              funcall2.addArgument(i, 8);
              funcall2.addArgument(funcall1, 64);
              pattern1.addTest(str1, new Value(funcall2, 64), b, bool2);
              break;
            default:
              parseError("parsePattern", "Bad slot value");
              break;
          }
          jessToken = this.m_jts.nextToken();
          if (jessToken.m_ttype == 38) {
            jessToken = this.m_jts.nextToken();
            continue;
          }
          if (!bool1 && jessToken.m_ttype != 41) {
            parseError("parsePattern", str1 + " is not a multislot");
            continue;
          }
        }
        if (bool1)
          pattern1.setMultislotLength(str1, b);
      }
      return pattern1;
    }
    Pattern pattern = new Pattern(str, 128, this.m_engine, paramInt);
    jessToken = this.m_jts.nextToken();
    while (jessToken.m_ttype != 41) {
      while (true) {
        Funcall funcall1;
        Funcall funcall2;
        Funcall funcall3;
        int i;
        boolean bool1 = false;
        if (jessToken.m_ttype == 38)
          jessToken = this.m_jts.nextToken();
        if (jessToken.m_ttype == 126) {
          bool1 = true;
          jessToken = this.m_jts.nextToken();
        }
        switch (jessToken.m_ttype) {
          case 8:
          case 8192:
            if (!jessToken.isBlankVariable())
              pattern.m_hasVariables = true;
          case 1:
          case 2:
            pattern.addTest(new Value(jessToken.m_sval, jessToken.m_ttype), bool1);
            break;
          case 4:
          case 32:
            pattern.addTest(new Value(jessToken.m_nval, jessToken.m_ttype), bool1);
            break;
          case 58:
            funcall1 = parseFuncall();
            pattern.addTest(new Value(funcall1, 64), bool1);
            break;
          case 61:
            funcall2 = parseFuncall();
            funcall3 = new Funcall("eq*", this.m_engine);
            i = RU.gensym("_=_");
            pattern.addTest(new Value(i, 8), false);
            funcall3.addArgument(i, 8);
            funcall3.addArgument(funcall2, 64);
            pattern.addTest(new Value(funcall3, 64), bool1);
            break;
          default:
            parseError("parsePattern", "Badslot value");
            break;
        }
        jessToken = this.m_jts.nextToken();
        if (jessToken.m_ttype != 38)
          pattern.advance();
      }
    }
    return pattern;
  }

  private Value parseDeffunction() throws ReteException {
    if ((this.m_jts.nextToken()).m_ttype != 40 || !(this.m_jts.nextToken()).m_sval.equals("deffunction"))
      parseError("parseDeffunction", "Expected (deffunction...");
    JessToken jessToken;
    if ((jessToken = this.m_jts.nextToken()).m_ttype != 1)
      parseError("parseDeffunction", "Expected deffunction name");
    Deffunction deffunction = new Deffunction(jessToken.m_sval, this.m_engine);
    if ((jessToken = this.m_jts.nextToken()).m_ttype == 2) {
      deffunction.m_docstring = jessToken.m_sval;
      jessToken = this.m_jts.nextToken();
    }
    if (jessToken.m_ttype != 40)
      parseError("parseDeffunction", "Expected '('");
    while (true) {
      if ((jessToken = this.m_jts.nextToken()).m_ttype != 8 && jessToken.m_ttype != 8192) {
        if (jessToken.m_ttype != 41)
          parseError("parseDeffunction", "Expected ')'");
        if ((jessToken = this.m_jts.nextToken()).m_ttype == 2) {
          deffunction.m_docstring = jessToken.m_sval;
          jessToken = this.m_jts.nextToken();
        }
        while (jessToken.m_ttype != 41) {
          if (jessToken.m_ttype == 40) {
            this.m_jts.pushBack(jessToken);
            Funcall funcall = parseFuncall();
            deffunction.addAction(funcall);
            continue;
          }
          switch (jessToken.m_ttype) {
            case 1:
            case 2:
            case 8:
            case 8192:
              deffunction.addValue(new Value(jessToken.m_sval, jessToken.m_ttype));
              break;
            case 4:
            case 32:
              deffunction.addValue(new Value(jessToken.m_nval, jessToken.m_ttype));
              break;
            default:
              parseError("parseDeffunction", "Unexpected character");
              break;
          }
          continue;
          jessToken = this.m_jts.nextToken();
        }
        this.m_engine.addDeffunction(deffunction);
        return Funcall.TRUE();
      }
      deffunction.addArgument(jessToken.m_sval);
    }
    switch (jessToken.m_ttype) {
      case 1:
      case 2:
      case 8:
      case 8192:
        deffunction.addValue(new Value(jessToken.m_sval, jessToken.m_ttype));
        break;
      case 4:
      case 32:
        deffunction.addValue(new Value(jessToken.m_nval, jessToken.m_ttype));
        break;
      default:
        parseError("parseDeffunction", "Unexpected character");
        break;
    }
    continue;
  }

  private void parseError(String paramString1, String paramString2) throws ReteException {
    try {
      throw new ParseException("Jesp::" + paramString1, paramString2 + " at line " + this.m_jts.lineno() + ": ", this.m_jts.toString());
    } finally {
      this.m_jts.clear();
    }
  }
}
