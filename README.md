# Simpson

Simpson (**Simp**le J**son** ) is a simple [RFC8259-compliant](https://datatracker.ietf.org/doc/html/rfc8259) JSON
**deserializer** for Java.

It deserializes a JSON string into a walkable node and provides methods for the following:

1. Checking types
2. Checking the existence of keys in JSON objects
3. Checking the length of JSON arrays
4. Extracting **typed** values.

## Usage

```java
import dev.kwameopareasiedu.simpson.parser.Parser;

public class JsonTest {
  public static void main(String[] args) {
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
          []
        ],
        "booleanTrue": true,
        "booleanFalse": false,
        "null": null
      }
      """;

    Parser.ObjectNode parsed = (Parser.ObjectNode) Simpson.parse(objectJson);

    System.out.println(parsed.get("id"));                   // StringNode[647ceaf3657eade56f8224eb]
    System.out.println(parsed.get("id").get());             // 647ceaf3657eade56f8224eb
    System.out.println(parsed.get("index"));                // IntegerNode[0]
    System.out.println(parsed.get("index").isInteger());    // true
    System.out.println(parsed.get("double"));               // DoubleNode[0.13]
    System.out.println(parsed.get("array.4.foo"));          // StringNode[bar]
  }
}
```

## Parser Nodes

Nodes are the representation of key-value pairs in JSON data. Simpson supports the following types which map to the
corresponding `Node<T>` subclasses:

> While JSON does not differentiate between integer and decimal types, Simpson uses Java utilities to differentiate
> for convenience.

| Data Type | Node Type     | Super Class                 |
|-----------|---------------|-----------------------------|
| `String`  | `StringNode`  | `Node<String>`              |
| `Integer` | `IntegerNode` | `Node<Integer>`             |
| `Decimal` | `DecimalNode` | `Node<Double>`              |
| `Boolean` | `BooleanNode` | `Node<Boolean>`             |
| `Object`  | `ObjectNode`  | `Node<Map<String, Node<?>>` |
| `Array`   | `ArrayNode`   | `Node<Node<?>[]>`           |
| `Null`    | `NullNode`    | `Node<Object>`              |

### Node Methods

| Node Type                                                                   | Method                   | Description                                                 |
|-----------------------------------------------------------------------------|--------------------------|-------------------------------------------------------------|
| `Node<T>`                                                                   | `T get()`                | Returns the typed value of the node                         |
|                                                                             | `boolean isString()`     | Checks if this node is a `StringNode`                       |
|                                                                             | `boolean isInteger()`    | Checks if this node is a `IntegerNode`                      |
|                                                                             | `boolean isDecimal()`    | Checks if this node is a `DecimalNode`                      |
|                                                                             | `boolean isBoolean()`    | Checks if this node is a `Boolean`                          |
|                                                                             | `boolean isObject()`     | Checks if this node is a `ObjectNode`                       |
|                                                                             | `boolean isArray()`      | Checks if this node is a `ArrayNode`                        |
|                                                                             | `boolean isNull()`       | Checks if this node is a `NullNode`                         |
| `ObjectNode`<br><small>In addition to <code>Node\<T></code> methods</small> | `Node<?> get(keyOrPath)` | Returns a nested node matching the specified key or path    |
|                                                                             | `boolean has(key)`       | Checks if a node exists with the specified key              |
| `ArrayNode`<br><small>In addition to <code>Node\<T></code> methods</small>  | `int getLength()`        | Returns <br/>the length of <br/><br/>items <br/>of the node |
|                                                                             | `Node<?> get(int index)` | Returns the node at the specified index                     |

## Testing

Simpson contains 318 tests written in accordance with the [RFC8259 spec](https://datatracker.ietf.org/doc/html/rfc8259).
located in the `src/test/resources/rfc8259-tests/` directory.

Run the following command to run these tests:

```bash
mvn test
```