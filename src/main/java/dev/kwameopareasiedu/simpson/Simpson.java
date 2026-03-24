package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Parser;
import dev.kwameopareasiedu.simpson.parser.Tokenizer;

/** {@link Simpson} is the utility class for JSON serialization */
public class Simpson {
  private Simpson() { }

  /** Parses a JSON string and returns the root {@link Parser.Node} of the parsed node tree */
  public static Parser.Node<?> parse(String json) {
    Tokenizer tokenizer = new Tokenizer();
    Tokenizer.Token[] tokens = tokenizer.tokenize(json);
    Parser parser = new Parser(tokens);
    return parser.parse();
  }
}
