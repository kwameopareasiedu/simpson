package dev.kwameopareasiedu.simpson.nodes;

/** {@link StringNode} is a {@link Node} whose value is a string */
public class StringNode extends Node<String> {
  public StringNode(String value) {
    super(Type.STRING, value);
  }
}
