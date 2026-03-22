package dev.kwameopareasiedu.simpson.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Tokenizer {
  private static final Pattern LITERAL_REGEX = Pattern.compile("\\w");
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

        while (ch != '"') {
          string.append(ch);
          ch = json.charAt(++index);
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

        if (isNumber(parsed))
          tokens.add(new Token(Token.Type.NUMBER, parsed));
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

  private boolean isNumber(String val) {
    try {
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
}
