package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Parser;

public class TestPad {
  private TestPad() { }

  public static void main(String[] args) {
    String json = """
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

    Parser.ObjectNode parsed = (Parser.ObjectNode) Simpson.parse(json);

    System.out.println(parsed.get("double"));
  }
}
