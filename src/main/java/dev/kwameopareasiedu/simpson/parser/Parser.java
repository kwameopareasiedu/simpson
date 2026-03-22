package dev.kwameopareasiedu.simpson.parser;

import java.util.*;

public class Parser {
  private final Tokenizer.Token[] tokens;
  private int index = 0;

  public Parser(Tokenizer.Token[] tokens) {
    if (tokens.length == 0)
      throw new IllegalArgumentException("Token list cannot empty");

    this.tokens = tokens;
    index = 0;
  }

  public Node<?> parse() {
    Tokenizer.Token currentToken = tokens[index];

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
    Tokenizer.Token token = advance(); // Should be an object key

    while (token.type() != Tokenizer.Token.Type.BRACE_CLOSE) {
      if (token.type() == Tokenizer.Token.Type.STRING) {
        String key = token.value();

        token = advance(); // Should be a colon

        if (token.type() != Tokenizer.Token.Type.COLON)
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

      if (token.type() == Tokenizer.Token.Type.COMMA)
        token = advance();
    }

    return new ObjectNode(objectNodeValue);
  }

  private ArrayNode parseArray() {
    List<Node<?>> arrayNodeValue = new ArrayList<>();
    Tokenizer.Token token = advance(); // Should be an object key

    while (token.type() != Tokenizer.Token.Type.BRACKET_CLOSE) {
      Node<?> value = parse();
      arrayNodeValue.add(value);

      token = advance(); // Should be a comma or a closing bracket

      if (token.type() == Tokenizer.Token.Type.COMMA)
        token = advance();
    }

    return new ArrayNode(arrayNodeValue.toArray(Node[]::new));
  }

  private Tokenizer.Token advance() {
    return tokens[++index];
  }

  public static abstract class Node<V> {
    protected final Type type;
    protected final V value;

    protected Node(Type type, V value) {
      this.type = type;
      this.value = value;
    }

    public V get() {
      return value;
    }

    public boolean isString() {
      return type == Node.Type.STRING;
    }

    public boolean isInteger() {
      return type == Node.Type.INTEGER;
    }

    public boolean isDecimal() {
      return type == Node.Type.DECIMAL;
    }

    public boolean isBoolean() {
      return type == Node.Type.BOOLEAN;
    }

    public boolean isObject() {
      return type == Node.Type.OBJECT;
    }

    public boolean isArray() {
      return type == Node.Type.ARRAY;
    }

    public boolean isNull() {
      return type == Node.Type.NULL;
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

    public boolean has(String key) {
      return value.containsKey(key);
    }

    public Node<?> get(String keyOrPath) {
      Node<?> node = value.get(keyOrPath);
      if (node != null) return node;

      // Parse as path string (E.g. "nested.items.0.attribute")
      String[] parts = keyOrPath.split("\\.");
      Node<?> nextNode = null;
      int index = 0;

      while (index < parts.length) {
        String nextKey = parts[index];

        if (nextNode == null) {
          nextNode = value.get(nextKey);
        } else if (nextNode instanceof ObjectNode) {
          nextNode = ((ObjectNode) nextNode).get(nextKey);
        } else if (nextNode instanceof ArrayNode) {
          nextNode = ((ArrayNode) nextNode).get(Integer.parseInt(nextKey));
        }

        index++;
      }

      return nextNode;
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

    public int getLength() {
      return value.length;
    }

    public Node<?> get(int index) {
      if (index >= value.length)
        throw new IndexOutOfBoundsException(index);

      return value[index];
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
