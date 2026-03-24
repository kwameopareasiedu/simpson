package dev.kwameopareasiedu.simpson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/** {@link Tokenizer} splits JSON strings into recognizable parts or tokens for use in later parsing */
public class Tokenizer {
  private static final Pattern LITERAL_REGEX = Pattern.compile("[+\\w.-]");
  private static final Pattern NUMBER_REGEX = Pattern.compile("^-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?$");
  private static final Pattern CONTROL_CHAR_REGEX = Pattern.compile("^\\\\[\"\\\\/bfnrt]$");
  private static final Pattern UNICODE_START_REGEX = Pattern.compile("^\\\\u$");
  private static final Pattern UNICODE_REGEX = Pattern.compile("^\\\\u[0-9A-Fa-f]{4}$");
  private static final Pattern WHITESPACE_REGEX = Pattern.compile("\\s");
  private static final char DELETE_CHAR_CODE = '\u007F';

  /** Parses a JSON string into a list of {@link Token tokens} */
  public Token[] tokenize(String json) {
    List<Token> tokens = new ArrayList<>();
    StringBuilder string = new StringBuilder();
    int index = 0, line = 1, column = 1;

    while (index < json.length()) {
      char ch = json.charAt(index);

      if (ch == '{') {
        tokens.add(new Token(Token.Type.BRACE_OPEN, String.valueOf(ch), line, column));
        column++;
        index++;
      } else if (ch == '}') {
        tokens.add(new Token(Token.Type.BRACE_CLOSE, String.valueOf(ch), line, column));
        column++;
        index++;
      } else if (ch == '[') {
        tokens.add(new Token(Token.Type.BRACKET_OPEN, String.valueOf(ch), line, column));
        column++;
        index++;
      } else if (ch == ']') {
        tokens.add(new Token(Token.Type.BRACKET_CLOSE, String.valueOf(ch), line, column));
        column++;
        index++;
      } else if (ch == ':') {
        tokens.add(new Token(Token.Type.COLON, String.valueOf(ch), line, column));
        column++;
        index++;
      } else if (ch == ',') {
        tokens.add(new Token(Token.Type.COMMA, String.valueOf(ch), line, column));
        column++;
        index++;
      } else if (ch == '"') {
        string.setLength(0);
        ch = json.charAt(++index);
        column++;

        boolean processedEscapeSequence = false;

        while (ch != '"') {
          if (Character.isISOControl(ch) && ch != DELETE_CHAR_CODE)
            throw new TokenizerException("Bad control character", index, line, column);

          if (ch == '\\') {
            StringBuilder escapedString = new StringBuilder();

            while (true) {
              if (escapedString.length() == 2) {
                if (CONTROL_CHAR_REGEX.matcher(escapedString).matches()) {
                  break;
                } else if (!UNICODE_START_REGEX.matcher(escapedString).matches()) {
                  throw new TokenizerException("Bad escaped character", index, line, column);
                }
              }

              if (escapedString.length() == 6) {
                if (UNICODE_REGEX.matcher(escapedString).matches())
                  break;

                throw new TokenizerException("Bad unicode escaped character", index, line, column);
              }

              escapedString.append(ch);
              ch = json.charAt(++index);
              column++;
            }

            string.append(escapedString);
            processedEscapeSequence = true;
          }

          if (!processedEscapeSequence) {
            string.append(ch);

            if (index + 1 >= json.length())
              throw new TokenizerException("Unterminated string", index, line, column);

            ch = json.charAt(++index);
            column++;
          }

          processedEscapeSequence = false;
        }

        tokens.add(new Token(Token.Type.STRING, string.toString(), line, column));
        column++;
        index++;
      } else if (LITERAL_REGEX.matcher(String.valueOf(ch)).matches()) {
        string.setLength(0);

        while (LITERAL_REGEX.matcher(String.valueOf(ch)).matches()) {
          string.append(ch);
          ch = json.charAt(++index);
          column++;
        }

        String parsed = string.toString();

        if (isInt(parsed)) {
          tokens.add(new Token(Token.Type.INTEGER, parsed, line, column));
        } else if (isDouble(parsed)) {
          tokens.add(new Token(Token.Type.DECIMAL, parsed, line, column));
        } else if (isTrue(parsed)) {
          tokens.add(new Token(Token.Type.TRUE, parsed, line, column));
        } else if (isFalse(parsed)) {
          tokens.add(new Token(Token.Type.FALSE, parsed, line, column));
        } else if (isNull(parsed)) {
          tokens.add(new Token(Token.Type.NULL, parsed, line, column));
        } else {
          throw new TokenizerException("Unexpected literal value", index, line, column);
        }
      } else if (Character.isISOControl(ch)) { // Escape sequences outside a string
        if (ch == '\n') {
          line++;
          column = 1;
          index++;
        } else throw new TokenizerException("Unexpected value", index, line, column);
      } else if (WHITESPACE_REGEX.matcher(String.valueOf(ch)).matches()) {
        column++;
        index++;
      } else {
        throw new TokenizerException("Unexpected value", index, line, column);
      }
    }

    return tokens.toArray(Token[]::new);
  }

  /** Checks if a string can be parsed into an integer */
  private boolean isInt(String val) {
    try {
      if (!NUMBER_REGEX.matcher(val).matches() || val.contains("."))
        return false;

      Integer.parseInt(val);
      return true;
    } catch (NumberFormatException ignored) { }

    return false;
  }

  /** Checks if a string can be parsed into an double */
  private boolean isDouble(String val) {
    try {
      if (!NUMBER_REGEX.matcher(val).matches())
        return false;

      Double.parseDouble(val);
      return true;
    } catch (NumberFormatException ignored) {
      return false;
    }
  }

  /** Checks if a string can be parsed to {@code true} */
  private boolean isTrue(String val) {
    return val.equals("true");
  }

  /** Checks if a string can be parsed to {@code false} */
  private boolean isFalse(String val) {
    return val.equals("false");
  }

  /** Checks if a string can be parsed to {@code null} */
  private boolean isNull(String val) {
    return val.equals("null");
  }

  /**
   * {@link TokenizerException} is a custom exception class thrown by {@link Tokenizer} when an error occurs.
   * <p>
   * It requires the index, line and column numbers of the error location to give more context when debugging
   */
  private static class TokenizerException extends RuntimeException {
    public TokenizerException(String message, int index, int line, int column) {
      super(String.format("%s at position %d (line %d, column %d)", message, index + 1, line, column));
    }
  }
}
