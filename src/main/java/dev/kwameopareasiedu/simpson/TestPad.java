package dev.kwameopareasiedu.simpson;

public class TestPad {
  private TestPad() { }

  public static void main(String[] args) {
    System.out.println(Character.isISOControl('\u007F'));
    System.out.println(Simpson.parse("[\"\u007F\"]"));
  }
}
