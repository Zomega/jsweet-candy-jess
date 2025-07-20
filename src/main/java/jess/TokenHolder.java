package jess;

class TokenHolder {
  Token m_token;

  TokenHolder m_right;

  TokenHolder m_left;

  int m_code;

  TokenHolder(int paramInt) {
    this.m_code = paramInt;
  }
}
