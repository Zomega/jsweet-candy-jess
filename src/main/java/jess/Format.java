package jess;

public class Format {
  private int width = 0;

  private int precision = -1;

  private String pre = "";

  private String post = "";

  private boolean leading_zeroes = false;

  private boolean show_plus = false;

  private boolean alternate = false;

  private boolean show_space = false;

  private boolean left_align = false;

  char fmt = ' ';

  public Format(String paramString) throws ReteException {
    boolean bool = false;
    int i = paramString.length();
    byte b1 = 0;
    byte b2;
    for (b2 = 0; !b1; b2++) {
      if (b2 >= i) {
        b1 = 5;
      } else if (paramString.charAt(b2) == '%') {
        if (b2 < i - 1) {
          if (paramString.charAt(b2 + 1) == '%') {
            this.pre += '%';
            b2++;
          } else {
            b1 = 1;
          }
        } else {
          throw new ReteException("Format::<init>", "Invalid format", paramString);
        }
      } else {
        this.pre += paramString.charAt(b2);
      }
    }
    while (b1 == 1) {
      if (b2 >= i) {
        b1 = 5;
      } else if (paramString.charAt(b2) == ' ') {
        this.show_space = true;
      } else if (paramString.charAt(b2) == '-') {
        this.left_align = true;
      } else if (paramString.charAt(b2) == '+') {
        this.show_plus = true;
      } else if (paramString.charAt(b2) == '0') {
        this.leading_zeroes = true;
      } else if (paramString.charAt(b2) == '#') {
        this.alternate = true;
      } else {
        b1 = 2;
        b2--;
      }
      b2++;
    }
    while (b1 == 2) {
      if (b2 >= i) {
        b1 = 5;
        continue;
      }
      if ('0' <= paramString.charAt(b2) && paramString.charAt(b2) <= '9') {
        this.width = this.width * 10 + paramString.charAt(b2) - 48;
        b2++;
        continue;
      }
      if (paramString.charAt(b2) == '.') {
        b1 = 3;
        this.precision = 0;
        b2++;
        continue;
      }
      b1 = 4;
    }
    while (b1 == 3) {
      if (b2 >= i) {
        b1 = 5;
        continue;
      }
      if ('0' <= paramString.charAt(b2) && paramString.charAt(b2) <= '9') {
        this.precision = this.precision * 10 + paramString.charAt(b2) - 48;
        b2++;
        continue;
      }
      b1 = 4;
    }
    if (b1 == 4) {
      if (b2 >= i) {
        b1 = 5;
      } else {
        this.fmt = paramString.charAt(b2);
      }
      b2++;
    }
    if (b2 < i)
      this.post = paramString.substring(b2, i);
  }

  public String form(double paramDouble) {
    String str;
    if (this.precision < 0)
      this.precision = 6;
    byte b = 1;
    if (paramDouble < 0.0D) {
      paramDouble = -paramDouble;
      b = -1;
    }
    if (this.fmt == 'f') {
      str = fixed_format(paramDouble);
    } else if (this.fmt == 'e' || this.fmt == 'E' || this.fmt == 'g' || this.fmt == 'G') {
      str = exp_format(paramDouble);
    } else {
      throw new IllegalArgumentException();
    }
    return pad(sign(b, str));
  }

  public String form(long paramLong) {
    String str;
    byte b = 0;
    if (this.fmt == 'd' || this.fmt == 'i') {
      b = 1;
      if (paramLong < 0L) {
        paramLong = -paramLong;
        b = -1;
      }
      str = "" + paramLong;
    } else if (this.fmt == 'o') {
      str = convert(paramLong, 3, 7, "01234567");
    } else if (this.fmt == 'x') {
      str = convert(paramLong, 4, 15, "0123456789abcdef");
    } else if (this.fmt == 'X') {
      str = convert(paramLong, 4, 15, "0123456789ABCDEF");
    } else {
      throw new IllegalArgumentException();
    }
    return pad(sign(b, str));
  }

  public String form(char paramChar) {
    if (this.fmt != 'c')
      throw new IllegalArgumentException();
    String str = "" + paramChar;
    return pad(str);
  }

  public String form(String paramString) {
    if (this.fmt != 's')
      throw new IllegalArgumentException();
    if (this.precision >= 0)
      paramString = paramString.substring(0, this.precision);
    return pad(paramString);
  }

  private static String repeat(char paramChar, int paramInt) {
    if (paramInt <= 0)
      return "";
    StringBuffer stringBuffer = new StringBuffer(paramInt);
    for (byte b = 0; b < paramInt; b++)
      stringBuffer.append(paramChar);
    return stringBuffer.toString();
  }

  private static String convert(long paramLong, int paramInt1, int paramInt2, String paramString) {
    if (paramLong == 0L)
      return "0";
    String str = "";
    while (paramLong != 0L) {
      str = paramString.charAt((int)(paramLong & paramInt2)) + str;
      paramLong >>>= paramInt1;
    }
    return str;
  }

  private String pad(String paramString) {
    String str = repeat(' ', this.width - paramString.length());
    return this.left_align ? (this.pre + paramString + str + this.post) : (this.pre + str + paramString + this.post);
  }

  private String sign(int paramInt, String paramString) {
    String str = "";
    if (paramInt < 0) {
      str = "-";
    } else if (paramInt > 0) {
      if (this.show_plus) {
        str = "+";
      } else if (this.show_space) {
        str = " ";
      }
    } else if (this.fmt == 'o' && this.alternate && paramString.length() > 0 && paramString.charAt(0) != '0') {
      str = "0";
    } else if (this.fmt == 'x' && this.alternate) {
      str = "0x";
    } else if (this.fmt == 'X' && this.alternate) {
      str = "0X";
    }
    int i = 0;
    if (this.leading_zeroes) {
      i = this.width;
    } else if ((this.fmt == 'd' || this.fmt == 'i' || this.fmt == 'x' || this.fmt == 'X' || this.fmt == 'o') && this.precision > 0) {
      i = this.precision;
    }
    return str + repeat('0', i - str.length() - paramString.length()) + paramString;
  }

  private String fixed_format(double paramDouble) {
    String str = "";
    if (paramDouble > 9.223372036854776E18D)
      return exp_format(paramDouble);
    long l = (long)((this.precision == 0) ? (paramDouble + 0.5D) : paramDouble);
    str = str + l;
    double d = paramDouble - l;
    return (d >= 1.0D || d < 0.0D) ? exp_format(paramDouble) : (str + frac_part(d));
  }

  private String frac_part(double paramDouble) {
    String str = "";
    if (this.precision > 0) {
      double d = 1.0D;
      String str1 = "";
      for (byte b = 1; b <= this.precision && d <= 9.223372036854776E18D; b++) {
        d *= 10.0D;
        str1 = str1 + "0";
      }
      long l = (long)(d * paramDouble + 0.5D);
      str = str1 + l;
      str = str.substring(str.length() - this.precision, str.length());
    }
    if (this.precision > 0 || this.alternate)
      str = "." + str;
    if ((this.fmt == 'G' || this.fmt == 'g') && !this.alternate) {
      int i;
      for (i = str.length() - 1; i >= 0 && str.charAt(i) == '0'; i--);
      if (i >= 0 && str.charAt(i) == '.')
        i--;
      str = str.substring(0, i + 1);
    }
    return str;
  }

  private String exp_format(double paramDouble) {
    String str1 = "";
    byte b = 0;
    double d1 = paramDouble;
    double d2 = 1.0D;
    while (d1 > 10.0D) {
      b++;
      d2 /= 10.0D;
      d1 /= 10.0D;
    }
    while (d1 < 1.0D) {
      b--;
      d2 *= 10.0D;
      d1 *= 10.0D;
    }
    if ((this.fmt == 'g' || this.fmt == 'G') && b >= -4 && b < this.precision)
      return fixed_format(paramDouble);
    paramDouble *= d2;
    str1 = str1 + fixed_format(paramDouble);
    if (this.fmt == 'e' || this.fmt == 'g') {
      str1 = str1 + "e";
    } else {
      str1 = str1 + "E";
    }
    String str2 = "000";
    if (b >= 0) {
      str1 = str1 + "+";
      str2 = str2 + b;
    } else {
      str1 = str1 + "-";
      str2 = str2 + -b;
    }
    return str1 + str2.substring(str2.length() - 3, str2.length());
  }
}
