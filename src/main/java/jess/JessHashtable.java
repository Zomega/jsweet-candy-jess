package jess;

class JessHashtable {
  private JessHashtableEntry[] m_table;

  private int m_count;

  private int m_threshold;

  private float m_loadFactor = 0.75F;

  JessHashtable() {
    byte b = 101;
    this.m_table = new JessHashtableEntry[b];
    this.m_threshold = (int)(b * this.m_loadFactor);
  }

  synchronized String get(int paramInt) {
    JessHashtableEntry[] arrayOfJessHashtableEntry = this.m_table;
    int i = paramInt;
    int j = (i & Integer.MAX_VALUE) % arrayOfJessHashtableEntry.length;
    for (JessHashtableEntry jessHashtableEntry = arrayOfJessHashtableEntry[j]; jessHashtableEntry != null; jessHashtableEntry = jessHashtableEntry.m_next) {
      if (jessHashtableEntry.m_hash == i && jessHashtableEntry.m_key == paramInt)
        return jessHashtableEntry.m_value;
    }
    return null;
  }

  private void rehash() {
    int i = this.m_table.length;
    JessHashtableEntry[] arrayOfJessHashtableEntry1 = this.m_table;
    int j = i * 2 + 1;
    JessHashtableEntry[] arrayOfJessHashtableEntry2 = new JessHashtableEntry[j];
    this.m_threshold = (int)(j * this.m_loadFactor);
    this.m_table = arrayOfJessHashtableEntry2;
    int k = i;
    while (k-- > 0) {
      JessHashtableEntry jessHashtableEntry = arrayOfJessHashtableEntry1[k];
      while (jessHashtableEntry != null) {
        JessHashtableEntry jessHashtableEntry1 = jessHashtableEntry;
        jessHashtableEntry = jessHashtableEntry.m_next;
        int m = (jessHashtableEntry1.m_hash & Integer.MAX_VALUE) % j;
        jessHashtableEntry1.m_next = arrayOfJessHashtableEntry2[m];
        arrayOfJessHashtableEntry2[m] = jessHashtableEntry1;
      }
    }
  }

  synchronized String put(int paramInt, String paramString) {
    if (paramString == null)
      throw new NullPointerException();
    JessHashtableEntry[] arrayOfJessHashtableEntry = this.m_table;
    int i = paramInt;
    int j = (i & Integer.MAX_VALUE) % arrayOfJessHashtableEntry.length;
    for (JessHashtableEntry jessHashtableEntry1 = arrayOfJessHashtableEntry[j]; jessHashtableEntry1 != null; jessHashtableEntry1 = jessHashtableEntry1.m_next) {
      if (jessHashtableEntry1.m_hash == i && jessHashtableEntry1.m_key == paramInt) {
        String str = jessHashtableEntry1.m_value;
        jessHashtableEntry1.m_value = paramString;
        return str;
      }
    }
    if (this.m_count >= this.m_threshold) {
      rehash();
      return put(paramInt, paramString);
    }
    JessHashtableEntry jessHashtableEntry2 = new JessHashtableEntry();
    jessHashtableEntry2.m_hash = i;
    jessHashtableEntry2.m_key = paramInt;
    jessHashtableEntry2.m_value = paramString;
    jessHashtableEntry2.m_next = arrayOfJessHashtableEntry[j];
    arrayOfJessHashtableEntry[j] = jessHashtableEntry2;
    this.m_count++;
    return null;
  }
}
