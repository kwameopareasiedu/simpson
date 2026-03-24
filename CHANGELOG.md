# Change Log

# 0.3.0

- Moved `Parser.Node<T>` class to standalone `Node<T>` class
- Moved `Parser.StringNode` class to standalone `StringNode` class
- Moved `Parser.IntegerNode` class to standalone `IntegerNode` class
- Moved `Parser.DecimalNode` class to standalone `DecimalNode` class
- Moved `Parser.BooleanNode` class to standalone `BooleanNode` class
- Moved `Parser.ObjectNode` class to standalone `ObjectNode` class
- Moved `Parser.ArrayNode` class to standalone `ArrayNode` class
- Moved `Parser.NullNode` class to standalone `NullNode` class
- Renamed `int getLength()` in `ArrayNode` class to `int length()`

## 0.2.0

- Added Javadocs to public API
- Added `StringNode getStringNode(String keyOrPath)` to `ObjectNode` class
- Added `IntegerNode getIntegerNode(String keyOrPath)` to `ObjectNode` class
- Added `DecimalNode getDecimalNode(String keyOrPath)` to `ObjectNode` class
- Added `BooleanNode getBooleanNode(String keyOrPath)` to `ObjectNode` class
- Added `ObjectNode getObjectNode(String keyOrPath)` to `ObjectNode` class
- Added `ArrayNode getArrayNode(String keyOrPath)` to `ObjectNode` class
- Added `NullNode getNullNode(String keyOrPath)` to `ObjectNode` class
- Added `StringNode getStringNode(int index)` to `ArrayNode` class
- Added `IntegerNode getIntegerNode(int index)` to `ArrayNode` class
- Added `DecimalNode getDecimalNode(int index)` to `ArrayNode` class
- Added `BooleanNode getBooleanNode(int index)` to `ArrayNode` class
- Added `ObjectNode getObjectNode(int index)` to `ArrayNode` class
- Added `ArrayNode getArrayNode(int index)` to `ArrayNode` class
- Added `NullNode getNullNode(int index)` to `ArrayNode` class
- Configured Maven packaging to include sources

## 0.1.0

- Implemented Simpson JSON parser
- Included tests which check compliance with the [RFC8259](https://datatracker.ietf.org/doc/html/rfc8259) specification