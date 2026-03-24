package dev.kwameopareasiedu.simpson.nodes;

/** {@link NullNode} is a {@link Node} whose value {@code null} */
public class NullNode extends Node<Object> {
  public NullNode() {
    super(Type.NULL, null);
  }
}
