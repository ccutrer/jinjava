package com.hubspot.jinjava.objects.serialization;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import java.util.Arrays;

public class PyishCharacterEscapes extends CharacterEscapes {
  public static final PyishCharacterEscapes INSTANCE = new PyishCharacterEscapes();
  private final int[] asciiEscapes;

  private PyishCharacterEscapes() {
    int[] escapes = CharacterEscapes.standardAsciiEscapesForJSON();
    escapes['\n'] = CharacterEscapes.ESCAPE_NONE;
    escapes['\t'] = CharacterEscapes.ESCAPE_NONE;
    escapes['\r'] = CharacterEscapes.ESCAPE_NONE;
    asciiEscapes = escapes;
  }

  @Override
  public int[] getEscapeCodesForAscii() {
    return Arrays.copyOf(asciiEscapes, asciiEscapes.length);
  }

  @Override
  public SerializableString getEscapeSequence(int i) {
    return null;
  }
}
