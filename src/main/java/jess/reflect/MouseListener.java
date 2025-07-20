package jess.reflect;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import jess.Rete;
import jess.ReteException;

public class MouseListener extends JessListener implements MouseListener {
  public MouseListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void mouseClicked(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }

  public void mouseEntered(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }

  public void mouseExited(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }

  public void mousePressed(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }

  public void mouseReleased(MouseEvent paramMouseEvent) {
    receiveEvent(paramMouseEvent);
  }
}
