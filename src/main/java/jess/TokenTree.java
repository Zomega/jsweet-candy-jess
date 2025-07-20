package jess;

class TokenTree {
  TokenHolder m_root = new TokenHolder(1000);

  synchronized void add(Token paramToken) {
    TokenHolder tokenHolder = findCodeInTree(paramToken, true);
    if (tokenHolder.m_token == null) {
      tokenHolder.m_token = paramToken;
      return;
    }
    Token token = tokenHolder.m_token;
    if (token.dataEquals(paramToken))
      return;
    while (token.m_next != null) {
      token = token.m_next;
      if (token.dataEquals(paramToken))
        return;
    }
    token.m_next = paramToken;
  }

  synchronized void remove(Token paramToken) {
    TokenHolder tokenHolder = findCodeInTree(paramToken, false);
    if (tokenHolder == null || tokenHolder.m_token == null)
      return;
    Token token = tokenHolder.m_token;
    if (token.dataEquals(paramToken)) {
      tokenHolder.m_token = token.m_next;
      return;
    }
    while (token.m_next != null) {
      Token token1 = token;
      token = token.m_next;
      if (token.dataEquals(paramToken)) {
        token1.m_next = token.m_next;
        return;
      }
    }
  }

  private TokenHolder findCodeInTree(Token paramToken, boolean paramBoolean) {
    int i = paramToken.m_sortcode % 101;
    TokenHolder tokenHolder = this.m_root;
    int j = tokenHolder.m_code;
    while (j != i) {
      if (i < j) {
        if (tokenHolder.m_left == null)
          return paramBoolean ? (tokenHolder.m_left = new TokenHolder(i)) : null;
        tokenHolder = tokenHolder.m_left;
        j = tokenHolder.m_code;
        continue;
      }
      if (i > j) {
        if (tokenHolder.m_right == null)
          return paramBoolean ? (tokenHolder.m_right = new TokenHolder(i)) : null;
        tokenHolder = tokenHolder.m_right;
        j = tokenHolder.m_code;
      }
    }
    return tokenHolder;
  }
}
