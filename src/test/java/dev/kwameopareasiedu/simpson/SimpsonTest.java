package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.parser.Parser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  public void parseArrayJson() {
    String arrayJson = """
      [
        "animal",
        "country",
        "food",
        "plant",
        "sport"
      ]
      """;

    Parser.Node<?> parsedNode = Simpson.parse(arrayJson);
    assertTrue(parsedNode.isArray());

    Parser.ArrayNode parsedArray = (Parser.ArrayNode) parsedNode;
    assertEquals(5, parsedArray.getLength());
    assertTrue(parsedArray.get(0).isString());
    assertTrue(parsedArray.get(1).isString());
    assertTrue(parsedArray.get(2).isString());
    assertTrue(parsedArray.get(3).isString());
    assertTrue(parsedArray.get(4).isString());
  }

  @Test
  public void passesRFC8259Tests() throws URISyntaxException, IOException {
    URL url = SimpsonTest.class.getResource("/rfc8259-tests/");
    assertNotNull(url);

    try (Stream<Path> pathStream = Files.list(Paths.get(url.toURI()))) {
      Path[] paths = pathStream
        .sorted(Comparator.comparing(p -> p.toFile().getName()))
        .toArray(Path[]::new);

      for (Path path : paths) {
        String pathFileName = path.toFile().getName();
        Completion completion = pathFileName.startsWith("y")
          ? Completion.PASS
          : pathFileName.startsWith("n")
          ? Completion.FAIL
          : Completion.DONT_CARE;

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
          StringBuilder json = new StringBuilder();
          String line;

          do {
            line = reader.readLine();

            if (line != null)
              json.append(line).append("\n");
          } while (line != null);

          System.out.println("Processing: " + pathFileName);

          switch (completion) {
            case PASS -> assertDoesNotThrow(() -> Simpson.parse(json.toString()));
            case FAIL -> assertThrows(Throwable.class, () -> Simpson.parse(json.toString()));
            case DONT_CARE -> {
              try {
                Simpson.parse(json.toString());
              } catch (Exception ignored) { }
            }
          }
        }
      }
    }
  }

  private enum Completion {
    PASS, FAIL, DONT_CARE
  }
}
