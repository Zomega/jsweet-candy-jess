package jess.reflect;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import jess.Rete;
import jess.ReteException;

public class ComponentListener extends JessListener implements ComponentListener {
  public ComponentListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void componentHidden(ComponentEvent paramComponentEvent) {
    receiveEvent(paramComponentEvent);
  }

  public void componentMoved(ComponentEvent paramComponentEvent) {
    receiveEvent(paramComponentEvent);
  }

  public void componentResized(ComponentEvent paramComponentEvent) {
    receiveEvent(paramComponentEvent);
  }

  public void componentShown(ComponentEvent paramComponentEvent) {
    receiveEvent(paramComponentEvent);
  }
}
