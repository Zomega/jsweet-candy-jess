package jess.reflect;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import jess.Rete;
import jess.ReteException;

public class MouseMotionListener extends JessListener implements MouseMotionListener {
  public MouseMotionListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void mouseDragged(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }

  public void mouseMoved(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }
}
