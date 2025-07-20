package jess.reflect;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import jess.Rete;
import jess.ReteException;

public class ItemListener extends JessListener implements ItemListener {
  public ItemListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void itemStateChanged(ItemEvent paramItemEvent) {
    receiveEvent(paramItemEvent);
  }
}
