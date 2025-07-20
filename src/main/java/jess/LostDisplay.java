package jess;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.InputStream;
import java.io.PrintStream;

public class LostDisplay extends Canvas implements ReteDisplay {
  Color[][] m_colors;

  Rectangle[][] m_locations;

  PrintStream m_os;

  InputStream m_is;

  Applet m_app;

  public static final int COLS = 25;

  public LostDisplay(PrintStream paramPrintStream, InputStream paramInputStream, Applet paramApplet) {
    this.m_app = paramApplet;
    priv_init(paramPrintStream, paramInputStream);
  }

  public LostDisplay(PrintStream paramPrintStream, InputStream paramInputStream) {
    this.m_app = null;
    priv_init(paramPrintStream, paramInputStream);
  }

  private void priv_init(PrintStream paramPrintStream, InputStream paramInputStream) {
    this.m_os = paramPrintStream;
    this.m_is = paramInputStream;
    this.m_colors = new Color[3][25];
    for (byte b1 = 0; b1 < 3; b1++) {
      for (byte b = 0; b < 25; b++)
        this.m_colors[b1][b] = Color.black;
    }
    this.m_locations = new Rectangle[3][25];
    for (byte b2 = 0; b2 < 3; b2++) {
      for (byte b = 0; b < 25; b++)
        this.m_locations[b2][b] = new Rectangle(b * 15 + 1, b2 * 15 + 1, 13, 13);
    }
  }

  public void update(Graphics paramGraphics) {
    paint(paramGraphics);
  }

  private void sleep(int paramInt) {
    try {
      Thread.sleep(paramInt);
    } catch (InterruptedException interruptedException) {}
  }

  public void paint(Graphics paramGraphics) {
    Color color = paramGraphics.getColor();
    for (byte b = 0; b < 3; b++) {
      for (byte b1 = 0; b1 < 25; b1++) {
        Rectangle rectangle = this.m_locations[b][b1];
        paramGraphics.setColor(this.m_colors[b][b1]);
        paramGraphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
      }
    }
    paramGraphics.setColor(color);
  }

  public void assertFact(ValueVector paramValueVector) {
    int i;
    try {
      i = paramValueVector.get(2).factIDValue();
    } catch (ReteException reteException) {
      i = 0;
    }
    i %= 25;
    if (this.m_colors[0][i] == Color.red) {
      this.m_colors[0][i] = Color.green;
    } else {
      this.m_colors[0][i] = Color.red;
    }
    repaint();
    sleep(5);
  }

  public void retractFact(ValueVector paramValueVector) {
    int i;
    try {
      i = paramValueVector.get(2).factIDValue();
    } catch (ReteException reteException) {
      i = 0;
    }
    i %= 25;
    if (this.m_colors[0][i] == Color.green) {
      this.m_colors[0][i] = Color.red;
    } else {
      this.m_colors[0][i] = Color.green;
    }
    repaint();
    sleep(5);
  }

  public void addDeffacts(Deffacts paramDeffacts) {
    int i = (paramDeffacts.name() > 0) ? paramDeffacts.name() : -paramDeffacts.name();
    i %= 25;
    if (this.m_colors[1][i] == Color.yellow) {
      this.m_colors[1][i] = Color.red;
    } else {
      this.m_colors[1][i] = Color.yellow;
    }
    repaint();
    sleep(5);
  }

  public void addDeftemplate(Deftemplate paramDeftemplate) {
    int i = (paramDeftemplate.name() > 0) ? paramDeftemplate.name() : -paramDeftemplate.name();
    i %= 25;
    if (this.m_colors[1][i] == Color.green) {
      this.m_colors[1][i] = Color.yellow;
    } else {
      this.m_colors[1][i] = Color.green;
    }
    repaint();
    sleep(5);
  }

  public void addDefrule(Defrule paramDefrule) {
    int i = (paramDefrule.name() > 0) ? paramDefrule.name() : -paramDefrule.name();
    i %= 25;
    if (this.m_colors[1][i] == Color.blue) {
      this.m_colors[1][i] = Color.red;
    } else {
      this.m_colors[1][i] = Color.blue;
    }
    repaint();
    sleep(5);
  }

  public void activateRule(Defrule paramDefrule) {
    int i = (paramDefrule.name() > 0) ? paramDefrule.name() : -paramDefrule.name();
    i %= 25;
    if (this.m_colors[2][i] == Color.red) {
      this.m_colors[2][i] = Color.blue;
    } else {
      this.m_colors[2][i] = Color.red;
    }
    repaint();
    sleep(5);
  }

  public void deactivateRule(Defrule paramDefrule) {
    int i = (paramDefrule.name() > 0) ? paramDefrule.name() : -paramDefrule.name();
    i %= 25;
    if (this.m_colors[2][i] == Color.green) {
      this.m_colors[2][i] = Color.red;
    } else {
      this.m_colors[2][i] = Color.green;
    }
    repaint();
    sleep(5);
  }

  public void fireRule(Defrule paramDefrule) {
    int i = (paramDefrule.name() > 0) ? paramDefrule.name() : -paramDefrule.name();
    i %= 25;
    if (this.m_colors[2][i] == Color.yellow) {
      this.m_colors[2][i] = Color.blue;
    } else {
      this.m_colors[2][i] = Color.yellow;
    }
    repaint();
    sleep(5);
    System.gc();
  }

  public PrintStream stdout() {
    sleep(5);
    return this.m_os;
  }

  public InputStream stdin() {
    sleep(5);
    return this.m_is;
  }

  public PrintStream stderr() {
    sleep(5);
    return this.m_os;
  }

  public Applet applet() {
    return this.m_app;
  }
}
