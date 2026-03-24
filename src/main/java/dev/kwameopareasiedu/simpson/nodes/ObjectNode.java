package dev.kwameopareasiedu.simpson.nodes;

import java.util.Map;
import java.util.Set;

/** {@link ObjectNode} is a {@link Node} whose value is a map of string keys to other {@link Node nodes} */
public class ObjectNode extends Node<Map<String, Node<?>>> {
  public ObjectNode(Map<String, Node<?>> value) {
    super(Type.OBJECT, value);
  }

  /** Checks if this node's map has the specified key */
  public boolean has(String key) {
    return value.containsKey(key);
  }

  /**
   * Returns the node in the value map matching the given key.
   * <p>
   * The key can be a simple key or a dot-notation representing a path within the value map.
   * <p>
   * As an example, let's say this {@link ObjectNode} is parsed from the following JSON string:
   * <pre>
   * {@code
   * {
   *   "id": "647ceaf3657eade56f8224eb",
   *   "index": 0,
   *   "double": 0.13,
   *   "array": [
   *     1,
   *     "another",
   *     true,
   *     false,
   *     { "foo": "bar" },
   *     [],
   *     [1, 2, "hello"]
   *   ],
   *   "booleanTrue": true,
   *   "booleanFalse": false,
   *   "null": null
   * }
   * }
   * </pre>
   * <p>
   * The output of the following would result in:
   * <pre>
   * {@code
   * Parser.ObjectNode parsed = (Parser.ObjectNode) Simpson.parse(objectJson);
   *
   * System.out.println(parsed.get("id"));                   // StringNode[647ceaf3657eade56f8224eb]
   * System.out.println(parsed.get("id").get());             // 647ceaf3657eade56f8224eb
   * System.out.println(parsed.get("index"));                // IntegerNode[0]
   * System.out.println(parsed.get("index").isInteger());    // true
   * System.out.println(parsed.get("double"));               // DoubleNode[0.13]
   * System.out.println(parsed.get("array.4.foo"));          // StringNode[bar]
   * System.out.println(parsed.get("array.5.2"));            // StringNode[hello]
   * }
   * </pre>
   * <p>
   * As shows in the last two outputs, to retrieve an inner member, you pass the dot-notation path to this method.
   * Each part of the dot-notation path is either an object key or an array index.
   */
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

  /**
   * Returns the {@link StringNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link StringNode}
   */
  public StringNode getStringNode(String keyOrPath) throws ClassCastException {
    return (StringNode) get(keyOrPath);
  }

  /**
   * Returns the {@link IntegerNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link IntegerNode}
   */
  public IntegerNode getIntegerNode(String keyOrPath) throws ClassCastException {
    return (IntegerNode) get(keyOrPath);
  }

  /**
   * Returns the {@link DecimalNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link DecimalNode}
   */
  public DecimalNode getDecimalNode(String keyOrPath) throws ClassCastException {
    return (DecimalNode) get(keyOrPath);
  }

  /**
   * Returns the {@link BooleanNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link BooleanNode}
   */
  public BooleanNode getBooleanNode(String keyOrPath) throws ClassCastException {
    return (BooleanNode) get(keyOrPath);
  }

  /**
   * Returns the {@link ObjectNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link ObjectNode}
   */
  public ObjectNode getObjectNode(String keyOrPath) throws ClassCastException {
    return (ObjectNode) get(keyOrPath);
  }

  /**
   * Returns the {@link ArrayNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link ArrayNode}
   */
  public ArrayNode getArrayNode(String keyOrPath) throws ClassCastException {
    return (ArrayNode) get(keyOrPath);
  }

  /**
   * Returns the {@link NullNode} with the specified key.
   *
   * @throws ClassCastException if the target node cannot be cast into a {@link NullNode}
   */
  public NullNode getNullNode(String keyOrPath) throws ClassCastException {
    return (NullNode) get(keyOrPath);
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
