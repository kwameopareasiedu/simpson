package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpsonTest {
  @Test
  public void parseObjectJson() {
    String objectJson = """
      {
        "id": "647ceaf3657eade56f8224eb",
        "index": 0,
        "double": -1e+9999,
        "array": [
          1,
          "another",
          true,
          false,
          { "foo": "bar" },
          []
        ],
        "booleanTrue": true,
        "booleanFalse": false,
        "null": null
      }
      """;

    Parser.Node<?> parsedNode = Simpson.parse(objectJson);
    assertTrue(parsedNode.isObject());

    Parser.ObjectNode parsedObject = (Parser.ObjectNode) parsedNode;
    assertTrue(parsedObject.has("id"));
    assertTrue(parsedObject.has("index"));
    assertTrue(parsedObject.has("double"));
    assertTrue(parsedObject.has("array"));
    assertTrue(parsedObject.has("booleanTrue"));
    assertTrue(parsedObject.has("booleanFalse"));
    assertTrue(parsedObject.has("null"));
    assertTrue(parsedObject.get("array").isArray());
    assertTrue(parsedObject.get("array.4.foo").isString());

    Parser.ArrayNode parsedArray = (Parser.ArrayNode) parsedObject.get("array");
    assertEquals(6, parsedArray.getLength());
    assertTrue(parsedArray.get(0).isInteger());
    assertTrue(parsedArray.get(1).isString());
    assertTrue(parsedArray.get(2).isBoolean());
    assertTrue(parsedArray.get(3).isBoolean());
    assertTrue(parsedArray.get(4).isObject());
    assertTrue(parsedArray.get(5).isArray());
  }
}
