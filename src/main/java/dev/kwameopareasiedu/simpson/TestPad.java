package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Token;
import dev.kwameopareasiedu.simpson.parser.Tokenizer;

public class TestPad {
  private TestPad() { }

  public static void main(String[] args) {
    String json = """
      {
        "id": "647ceaf3657eade56f8224eb",
        "index": 0,
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
        "nullValue": null
      }
      """;

    Tokenizer tokenizer = new Tokenizer();
    Token[] tokens = tokenizer.tokenize(json);

    for (Token token : tokens) {
      System.out.println(token);
    }
  }
}
