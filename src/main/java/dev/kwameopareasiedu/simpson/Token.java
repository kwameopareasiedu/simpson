package dev.kwameopareasiedu.simpson;

/**
 * {@link Token} represents a valid element of grammar of the JSON specification.
 * <p>
 * It stores the element type the value as well as line and column number for debugging
 */
public record Token(Type type, String value, int line, int column) {
  /** Represents the type of grammar element a {@link Token token} is */
  public enum Type {
    BRACE_OPEN,
    BRACE_CLOSE,
    BRACKET_OPEN,
    BRACKET_CLOSE,
    STRING,
    INTEGER,
    DECIMAL,
    TRUE,
    FALSE,
    NULL,
    COLON,
    COMMA
  }
}
