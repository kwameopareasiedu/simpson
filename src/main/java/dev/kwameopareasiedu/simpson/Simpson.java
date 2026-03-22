package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Parser;
import dev.kwameopareasiedu.simpson.parser.Tokenizer;

public class Simpson {
  private Simpson() { }

  public static Parser.Node<?> parse(String json) {
    Tokenizer tokenizer = new Tokenizer();
    Tokenizer.Token[] tokens = tokenizer.tokenize(json);
    Parser parser = new Parser(tokens);
    return parser.parse();
  }
}
