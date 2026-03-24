package dev.kwameopareasiedu.simpson.nodes;

/** {@link BooleanNode} is a {@link Node} whose value is a boolean */
public class BooleanNode extends Node<Boolean> {
  public BooleanNode(boolean value) {
    super(Type.BOOLEAN, value);
  }
}
