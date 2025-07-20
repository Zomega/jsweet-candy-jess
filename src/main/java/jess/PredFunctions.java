package jess;

public class PredFunctions implements Userpackage {
  public void add(Rete paramRete) {
    paramRete.addUserfunction(new evenp());
    paramRete.addUserfunction(new oddp());
    paramRete.addUserfunction(new floatp());
    paramRete.addUserfunction(new integerp());
    paramRete.addUserfunction(new lexemep());
    paramRete.addUserfunction(new multifieldp());
    paramRete.addUserfunction(new numberp());
    paramRete.addUserfunction(new stringp());
    paramRete.addUserfunction(new symbolp());
  }
}
