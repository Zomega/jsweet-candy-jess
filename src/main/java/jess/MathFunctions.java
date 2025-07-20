package jess;

public class MathFunctions implements Userpackage {
  public void add(Rete paramRete) {
    paramRete.addUserfunction(new abs());
    paramRete.addUserfunction(new div());
    paramRete.addUserfunction(new _float());
    paramRete.addUserfunction(new _integer());
    paramRete.addUserfunction(new max());
    paramRete.addUserfunction(new min());
    paramRete.addUserfunction(new expt());
    paramRete.addUserfunction(new exp());
    paramRete.addUserfunction(new log());
    paramRete.addUserfunction(new log10());
    paramRete.addUserfunction(new pi());
    paramRete.addUserfunction(new e());
    paramRete.addUserfunction(new round());
    paramRete.addUserfunction(new sqrt());
    paramRete.addUserfunction(new random());
  }
}
