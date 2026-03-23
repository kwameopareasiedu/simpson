package dev.kwameopareasiedu.simpson.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tokenizer {
  private static final Pattern LITERAL_REGEX = Pattern.compile("[+\\w.-]");
  private static final Pattern NUMBER_REGEX = Pattern.compile("^-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?$");
  private static final Pattern CONTROL_CHAR_REGEX = Pattern.compile("^\\\\[\"\\\\\\/bfnrt]$");
  private static final Pattern UNICODE_START_REGEX = Pattern.compile("^\\\\u$");
  private static final Pattern UNICODE_REGEX = Pattern.compile("^\\\\u[0-9A-Fa-f]{4}$");
  private static final Pattern WHITESPACE_REGEX = Pattern.compile("\\s");

  public Token[] tokenize(String json) {
    List<Token> tokens = new ArrayList<>();
    StringBuilder string = new StringBuilder();
    int index = 0;

    while (index < json.length()) {
      char ch = json.charAt(index);

      if (ch == '{') {
        tokens.add(new Token(Token.Type.BRACE_OPEN, String.valueOf(ch)));
        index++;
      } else if (ch == '}') {
        tokens.add(new Token(Token.Type.BRACE_CLOSE, String.valueOf(ch)));
        index++;
      } else if (ch == '[') {
        tokens.add(new Token(Token.Type.BRACKET_OPEN, String.valueOf(ch)));
        index++;
      } else if (ch == ']') {
        tokens.add(new Token(Token.Type.BRACKET_CLOSE, String.valueOf(ch)));
        index++;
      } else if (ch == ':') {
        tokens.add(new Token(Token.Type.COLON, String.valueOf(ch)));
        index++;
      } else if (ch == ',') {
        tokens.add(new Token(Token.Type.COMMA, String.valueOf(ch)));
        index++;
      } else if (ch == '"') {
        string.setLength(0);
        ch = json.charAt(++index);

        boolean processedControlChar = false;

        while (ch != '"') {
          if (ch == '\\') {
            StringBuilder controlString = new StringBuilder();

            while (true) {
              if (controlString.length() == 2) {
                if (CONTROL_CHAR_REGEX.matcher(controlString).matches())
                  break;
                else if (!UNICODE_START_REGEX.matcher(controlString).matches())
                  throw new IllegalArgumentException("Bad escaped character: " + controlString);
              }

              if (controlString.length() == 6) {
                if (UNICODE_REGEX.matcher(controlString).matches())
                  break;

                throw new IllegalArgumentException("Bad control character");
              }

              controlString.append(ch);
              ch = json.charAt(++index);
            }

            string.append(controlString);
            processedControlChar = true;
          }

          if (!processedControlChar) {
            string.append(ch);

            if (index + 1 >= json.length())
              throw new IllegalArgumentException("Unterminated string");

            ch = json.charAt(++index);
          }

          processedControlChar = false;
        }

        tokens.add(new Token(Token.Type.STRING, string.toString()));
        index++;
      } else if (LITERAL_REGEX.matcher(String.valueOf(ch)).matches()) {
        string.setLength(0);

        while (LITERAL_REGEX.matcher(String.valueOf(ch)).matches()) {
          string.append(ch);
          ch = json.charAt(++index);
        }

        String parsed = string.toString();

        if (isInt(parsed))
          tokens.add(new Token(Token.Type.INTEGER, parsed));
        else if (isDouble(parsed))
          tokens.add(new Token(Token.Type.DECIMAL, parsed));
        else if (isTrue(parsed))
          tokens.add(new Token(Token.Type.TRUE, parsed));
        else if (isFalse(parsed))
          tokens.add(new Token(Token.Type.FALSE, parsed));
        else if (isNull(parsed))
          tokens.add(new Token(Token.Type.NULL, parsed));
        else
          throw new IllegalArgumentException("Unexpected value: " + parsed);
      } else if (WHITESPACE_REGEX.matcher(String.valueOf(ch)).matches()) {
        index++;
      } else {
        throw new IllegalArgumentException("Unexpected value: " + ch);
      }
    }

    return tokens.toArray(Token[]::new);
  }

  private boolean isInt(String val) {
    try {
      if (!NUMBER_REGEX.matcher(val).matches() || val.contains("."))
        return false;

      Integer.parseInt(val);
      return true;
    } catch (NumberFormatException ignored) { }

    return false;
  }

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

  private boolean isTrue(String val) {
    return val.equals("true");
  }

  private boolean isFalse(String val) {
    return val.equals("false");
  }

  private boolean isNull(String val) {
    return val.equals("null");
  }

  public record Token(Type type, String value) {
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
}
