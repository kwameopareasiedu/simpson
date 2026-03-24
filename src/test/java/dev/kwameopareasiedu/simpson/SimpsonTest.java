package dev.kwameopareasiedu.simpson;

import dev.kwameopareasiedu.simpson.nodes.ArrayNode;
import dev.kwameopareasiedu.simpson.nodes.Node;
import dev.kwameopareasiedu.simpson.nodes.ObjectNode;
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
        "double": 0.13,
        "array": [
          1,
          "another",
          true,
          false,
          { "foo": "bar" },
          [],
          [1, 2.0, "hello"],
          null
        ],
        "booleanTrue": true,
        "booleanFalse": false,
        "null": null
      }
      """;

    Node<?> parsedNode = Simpson.parse(objectJson);
    assertTrue(parsedNode.isObject());

    ObjectNode parsedObject = (ObjectNode) parsedNode;
    assertTrue(parsedObject.has("id"));
    assertTrue(parsedObject.has("index"));
    assertTrue(parsedObject.has("double"));
    assertTrue(parsedObject.has("array"));
    assertTrue(parsedObject.has("booleanTrue"));
    assertTrue(parsedObject.has("booleanFalse"));
    assertTrue(parsedObject.has("null"));
    assertTrue(parsedObject.get("array").isArray());
    assertTrue(parsedObject.get("double").isDecimal());
    assertEquals("647ceaf3657eade56f8224eb", parsedObject.get("id").get());
    assertEquals(0, parsedObject.get("index").get());
    assertEquals(0.13, parsedObject.get("double").get());
    assertEquals(true, parsedObject.get("booleanTrue").get());
    assertEquals(false, parsedObject.get("booleanFalse").get());
    assertNull(parsedObject.get("null").get());
    assertEquals("another", parsedObject.get("array.1").get());
    assertEquals("bar", parsedObject.get("array.4.foo").get());
    assertThrows(Throwable.class, () -> parsedObject.get("array.5.0"));
    assertEquals("hello", parsedObject.get("array.6.2").get());
    assertDoesNotThrow(() -> {
      var ignoredId = parsedObject.getStringNode("id");
      var ignoredIndex = parsedObject.getIntegerNode("index");
      var ignoredDouble = parsedObject.getDecimalNode("double");
      var ignoredArray = parsedObject.getArrayNode("array");
      var ignoredBooleanTrue = parsedObject.getBooleanNode("booleanTrue");
      var ignoredBooleanFalse = parsedObject.getBooleanNode("booleanFalse");
      var ignoredNestedArray = parsedObject.getObjectNode("array.4");
      var ignoredNull = parsedObject.getNullNode("null");
    });
    assertThrows(ClassCastException.class, () -> parsedObject.getIntegerNode("id"));
    assertThrows(ClassCastException.class, () -> parsedObject.getStringNode("index"));
    assertThrows(ClassCastException.class, () -> parsedObject.getNullNode("double"));
    assertThrows(ClassCastException.class, () -> parsedObject.getObjectNode("array"));
    assertThrows(ClassCastException.class, () -> parsedObject.getArrayNode("array.4"));
    assertThrows(ClassCastException.class, () -> parsedObject.getDecimalNode("booleanTrue"));
    assertThrows(ClassCastException.class, () -> parsedObject.getDecimalNode("booleanFalse"));
    assertThrows(ClassCastException.class, () -> parsedObject.getBooleanNode("null"));

    ArrayNode parsedArray = parsedObject.getArrayNode("array");
    assertEquals(8, parsedArray.length());
    assertTrue(parsedArray.get(0).isInteger());
    assertTrue(parsedArray.get(1).isString());
    assertTrue(parsedArray.get(2).isBoolean());
    assertTrue(parsedArray.get(3).isBoolean());
    assertTrue(parsedArray.get(4).isObject());
    assertTrue(parsedArray.get(5).isArray());
    assertDoesNotThrow(() -> {
      var ignoredId = parsedArray.getStringNode(1);
      var ignoredIndex = parsedArray.getIntegerNode(0);
      var ignoredArray = parsedArray.getArrayNode(6);
      var ignoredDouble = parsedArray.getArrayNode(6).getDecimalNode(1);
      var ignoredBooleanTrue = parsedArray.getBooleanNode(2);
      var ignoredBooleanFalse = parsedArray.getBooleanNode(3);
      var ignoredNestedArray = parsedArray.getObjectNode(4);
      var ignoredNull = parsedArray.getNullNode(7);
    });
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

    Node<?> parsedNode = Simpson.parse(arrayJson);
    assertTrue(parsedNode.isArray());

    ArrayNode parsedArray = (ArrayNode) parsedNode;
    assertEquals(5, parsedArray.length());
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
