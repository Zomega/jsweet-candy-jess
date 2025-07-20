package jess;

public class StringFunctions implements Userpackage {
  public void add(Rete paramRete) {
    paramRete.addUserfunction(new lowcase());
    paramRete.addUserfunction(new strcat());
    paramRete.addUserfunction(new strcompare());
    paramRete.addUserfunction(new strindex());
    paramRete.addUserfunction(new strlength());
    paramRete.addUserfunction(new substring());
    paramRete.addUserfunction(new upcase());
  }
}
