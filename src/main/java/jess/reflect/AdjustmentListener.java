package jess.reflect;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import jess.Rete;
import jess.ReteException;

public class AdjustmentListener extends JessListener implements AdjustmentListener {
  public AdjustmentListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent) {
    receiveEvent(paramAdjustmentEvent);
  }
}
