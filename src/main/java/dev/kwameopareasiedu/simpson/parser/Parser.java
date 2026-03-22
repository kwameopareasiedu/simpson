package dev.kwameopareasiedu.simpson.parser;

import java.util.*;

public class Parser {
  private final Token[] tokens;
  private int index = 0;

  public Parser(Token[] tokens) {
    if (tokens.length == 0)
      throw new IllegalArgumentException("Token list cannot empty");

    this.tokens = tokens;
    index = 0;
  }

  public Node<?> parse() {
    Token currentToken = tokens[index];

    return switch (currentToken.type()) {
      case STRING -> new StringNode(currentToken.value());
      case INTEGER -> new IntegerNode(Integer.parseInt(currentToken.value()));
      case DECIMAL -> new DecimalNode(Double.parseDouble(currentToken.value()));
      case TRUE -> new BooleanNode(true);
      case FALSE -> new BooleanNode(false);
      case NULL -> new NullNode();
      case BRACE_OPEN -> parseObject();
      case BRACKET_OPEN -> parseArray();
      default -> throw new IllegalArgumentException("Unexpected token: " + currentToken.type());
    };
  }

  private ObjectNode parseObject() {
    Map<String, Node<?>> objectNodeValue = new HashMap<>();
    Token token = advance(); // Should be an object key

    while (token.type() != Token.Type.BRACE_CLOSE) {
      if (token.type() == Token.Type.STRING) {
        String key = token.value();

        token = advance(); // Should be a colon

        if (token.type() != Token.Type.COLON)
          throw new IllegalArgumentException("Expected : after key");
        else advance();

        Node<?> value = parse();
        objectNodeValue.put(key, value);
      } else {
        throw new IllegalArgumentException(
          "Expected string key in object. Found " + token.type()
        );
      }

      token = advance(); // Should be a comma or a closing brace

      if (token.type() == Token.Type.COMMA)
        token = advance();
    }

    return new ObjectNode(objectNodeValue);
  }

  private ArrayNode parseArray() {
    List<Node<?>> arrayNodeValue = new ArrayList<>();
    Token token = advance(); // Should be an object key

    while (token.type() != Token.Type.BRACKET_CLOSE) {
      Node<?> value = parse();
      arrayNodeValue.add(value);

      token = advance(); // Should be a comma or a closing bracket

      if (token.type() == Token.Type.COMMA)
        token = advance();
    }

    return new ArrayNode(arrayNodeValue.toArray(Node[]::new));
  }

  private Token advance() {
    return tokens[++index];
  }

  public static abstract class Node<V> {
    public final Type type;
    public final V value;

    protected Node(Type type, V value) {
      this.type = type;
      this.value = value;
    }

    @Override
    public String toString() {
      return String.format("%s[%s]", getClass().getSimpleName(), String.valueOf(value));
    }

    public enum Type {
      STRING,
      INTEGER,
      DECIMAL,
      BOOLEAN,
      OBJECT,
      ARRAY,
      NULL,
    }
  }

  public static class StringNode extends Node<String> {
    protected StringNode(String value) {
      super(Type.STRING, value);
    }
  }

  public static class IntegerNode extends Node<Integer> {
    protected IntegerNode(Integer value) {
      super(Type.INTEGER, value);
    }
  }

  public static class DecimalNode extends Node<Double> {
    protected DecimalNode(Double value) {
      super(Type.DECIMAL, value);
    }
  }

  public static class BooleanNode extends Node<Boolean> {
    protected BooleanNode(boolean value) {
      super(Type.BOOLEAN, value);
    }
  }

  public static class ObjectNode extends Node<Map<String, Node<?>>> {
    protected ObjectNode(Map<String, Node<?>> value) {
      super(Type.OBJECT, value);
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("ObjectNode[");

      Set<Map.Entry<String, Node<?>>> entries = value.entrySet();
      int index = 0;

      for (Map.Entry<String, Node<?>> pair : entries) {
        builder.append(pair.getKey());
        builder.append("=");
        builder.append(pair.getValue().toString());

        if (index < entries.size() - 1)
          builder.append(", ");

        index++;
      }

      builder.append("]");
      return builder.toString();
    }
  }

  public static class ArrayNode extends Node<Node<?>[]> {
    protected ArrayNode(Node<?>[] value) {
      super(Type.ARRAY, value);
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("ArrayNode[");

      for (int index = 0; index < value.length; index++) {
        builder.append(value[index].toString());

        if (index < value.length - 1)
          builder.append(", ");
      }

      builder.append("]");
      return builder.toString();
    }

  }

  public static class NullNode extends Node<Object> {
    protected NullNode() {
      super(Type.NULL, null);
    }
  }
}
