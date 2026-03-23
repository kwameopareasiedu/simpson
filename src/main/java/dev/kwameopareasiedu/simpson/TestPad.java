package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Parser;

public class TestPad {
  private TestPad() { }

  public static void main(String[] args) {
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

    Parser.ObjectNode parsedObject = (Parser.ObjectNode) Simpson.parse(objectJson);
    System.out.println(parsedObject.get("double"));

    String arrayJson = """
      [
        "animal",
        "country",
        "food",
        "plant",
        "sport"
      ]
      """;

    Parser.ArrayNode parsedArray = (Parser.ArrayNode) Simpson.parse(arrayJson);
    System.out.println(parsedArray.getLength());
    System.out.println(parsedArray.get(4));
    System.out.println(parsedArray.get(4).get());

//    System.out.println(Simpson.parse("[\"\\uD800\\\"]"));;
    System.out.println(Simpson.parse("[\"\\uD800\\\"h]"));;
//    System.out.println(Simpson.parse("[\"\\g\"]"));;
  }
}
