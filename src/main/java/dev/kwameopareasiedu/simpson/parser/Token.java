package dev.kwameopareasiedu.simpson.parser;

public record Token(Type type, String value) {
  public enum Type {
    BRACE_OPEN,
    BRACE_CLOSE,
    BRACKET_OPEN,
    BRACKET_CLOSE,
    STRING,
    NUMBER,
    TRUE,
    FALSE,
    NULL,
    COLON,
    COMMA
  }
}
