package dev.kwameopareasiedu.simpson.nodes;

import java.util.Arrays;
import java.util.Iterator;

/** {@link ArrayNode} is a {@link Node} whose value is an array other {@link Node nodes} */
public class ArrayNode extends Node<Node<?>[]> implements Iterable<Node<?>> {
  public ArrayNode(Node<?>[] value) {
    super(Type.ARRAY, value);
  }

  /** Returns the length of the node array */
  public int length() {
    return value.length;
  }

  /**
   * Returns the node in the value array at the given index
   *
   * @throws IndexOutOfBoundsException if the index is out of bounds
   */
  public Node<?> get(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= value.length)
      throw new IndexOutOfBoundsException(index);

    return value[index];
  }

  /**
   * Returns the {@link StringNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link StringNode}
   */
  public StringNode getStringNode(int index) throws ClassCastException {
    return (StringNode) get(index);
  }

  /**
   * Returns the {@link IntegerNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link IntegerNode}
   */
  public IntegerNode getIntegerNode(int index) throws ClassCastException {
    return (IntegerNode) get(index);
  }

  /**
   * Returns the {@link DecimalNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link DecimalNode}
   */
  public DecimalNode getDecimalNode(int index) throws ClassCastException {
    return (DecimalNode) get(index);
  }

  /**
   * Returns the {@link BooleanNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link BooleanNode}
   */
  public BooleanNode getBooleanNode(int index) throws ClassCastException {
    return (BooleanNode) get(index);
  }

  /**
   * Returns the {@link ObjectNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link ObjectNode}
   */
  public ObjectNode getObjectNode(int index) throws ClassCastException {
    return (ObjectNode) get(index);
  }

  /**
   * Returns the {@link ArrayNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link ArrayNode}
   */
  public ArrayNode getArrayNode(int index) throws ClassCastException {
    return (ArrayNode) get(index);
  }

  /**
   * Returns the {@link NullNode} at the given index.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link NullNode}
   */
  public NullNode getNullNode(int index) throws ClassCastException {
    return (NullNode) get(index);
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

  @Override
  public Iterator<Node<?>> iterator() {
    return Arrays.stream(value).iterator();
  }
}
