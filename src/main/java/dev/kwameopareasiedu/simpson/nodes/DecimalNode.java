package dev.kwameopareasiedu.simpson.nodes;

/** {@link DecimalNode} is a {@link Node} whose value is a double */
public class DecimalNode extends Node<Double> {
  public DecimalNode(Double value) {
    super(Type.DECIMAL, value);
  }
}
