package jess;

public class MiscFunctions implements Userpackage {
  public void add(Rete paramRete) {
    paramRete.addUserfunction(new socket());
    paramRete.addUserfunction(new _format());
    paramRete.addUserfunction(new batch());
    paramRete.addUserfunction(new _system());
    paramRete.addUserfunction(new ppdefrule());
    paramRete.addUserfunction(new loadpkg());
    paramRete.addUserfunction(new loadfn());
    paramRete.addUserfunction(new time());
    paramRete.addUserfunction(new build());
    paramRete.addUserfunction(new eval());
    paramRete.addUserfunction(new listfunctions());
    paramRete.addUserfunction(new setgen());
  }
}
