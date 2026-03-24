package dev.kwameopareasiedu.simpson.nodes;

/** {@link Node} forms the base of parsed structures from a valid JSON string */
public abstract class Node<V> {
  protected final Type type;
  protected final V value;

  /** Creates a new {@link Node} of the specified {@link Type type} and value */
  protected Node(Type type, V value) {
    this.type = type;
    this.value = value;
  }

  /** Returns the value of this node */
  public V get() {
    return value;
  }

  /** Returns {@code true} if this node is a {@link StringNode} and false otherwise */
  public boolean isString() {
    return type == Type.STRING;
  }

  /** Returns {@code true} if this node is an {@link IntegerNode} and false otherwise */
  public boolean isInteger() {
    return type == Type.INTEGER;
  }

  /** Returns {@code true} if this node is a {@link DecimalNode} and false otherwise */
  public boolean isDecimal() {
    return type == Type.DECIMAL;
  }

  /** Returns {@code true} if this node is a {@link BooleanNode} and false otherwise */
  public boolean isBoolean() {
    return type == Type.BOOLEAN;
  }

  /** Returns {@code true} if this node is a {@link ObjectNode} and false otherwise */
  public boolean isObject() {
    return type == Type.OBJECT;
  }

  /** Returns {@code true} if this node is a {@link ArrayNode} and false otherwise */
  public boolean isArray() {
    return type == Type.ARRAY;
  }

  /** Returns {@code true} if this node is a {@link NullNode} and false otherwise */
  public boolean isNull() {
    return type == Type.NULL;
  }

  @Override
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), value);
  }

  /** Represents a valid {@link Node} type */
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
