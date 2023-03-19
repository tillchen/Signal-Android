package org.thoughtcrime.securesms.util;

import org.junit.Test;
import org.thoughtcrime.securesms.components.ComposeText;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UtilTest {

  @Test
  public void testAsList() {
    List<Integer> res = Util.asList(1, 2, 3);

    assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3)), res);

  }

  @Test
  public void testStringJoin() {

    System.out.println(Util.join(new String[]{"123", "321"}, "444444"));
    assertEquals("123444444321", Util.join(new String[]{"123", "321"}, "444444"));
  }

  @Test
  public void testLongJoin() {

    System.out.println(Util.join(new long[]{1111, 2222}, "xxxxxx"));
    assertEquals("1111xxxxxx2222", Util.join(new long[]{1111, 2222}, "xxxxxx"));
  }

  @Test
  public void testLongListJoin() {
    List<Long> list = new ArrayList<>();
    list.add(11111L);
    list.add(2222222L);
    System.out.println(Util.join(list, "111111"));
    assertEquals("111111111112222222", Util.join(list, "111111"));
  }

  @Test
  public void testRightPad() {
    String value = "12345";
    String value2 = "1234567";
    int length = 6;
    System.out.println(Util.rightPad(value, length));
    System.out.println(Util.rightPad(value2, length));
    assertEquals("12345 ",Util.rightPad(value, length));
    assertEquals("1234567",Util.rightPad(value2, length));
  }

  @Test
  public void testIsEmpty() {
    ComposeText value = null;
    Collection<Integer> value2 = null;
    Util.isEmpty(value);
    Util.isEmpty(value2);
    assertEquals(true, Util.isEmpty(value));
    assertEquals(true, Util.isEmpty(value2));
  }

  @Test
  public void testGetFirstNonEmpty() {
    Util.getFirstNonEmpty("123", "321");
    System.out.println(Util.getFirstNonEmpty("", ""));
    assertEquals("123", Util.getFirstNonEmpty("123", "321"));
    assertEquals("", Util.getFirstNonEmpty("", ""));
  }


  @Test
  public void testIsLong() {

    System.out.println(Util.isLong("1111111111111111111111"));
    assertEquals(false, Util.isLong("1111111111111111111111"));
  }


  @Test
  public void getRandom() {
    List<Integer> test = new ArrayList<>();
    test.add(1);
    test.add(2);
    test.add(3);
    System.out.println(Util.getRandomElement(test));

  }

  @Test
  public void testForHalfOffsetFromScale() {
    assertEquals(-450.0, Util.halfOffsetFromScale(100, 10.0F), 0.000001);
  }

  @Test
  public void isEqual() {
    @Nullable Long first = Long.valueOf(10000);
    long second = 1000;
    assertEquals(false, Util.isEquals(first, second));
    assertEquals(12333, Util.toIntExact(12333));
  }

  @Test
  public void testGetPrettyFileSize() {
    assertEquals("123 B", Util.getPrettyFileSize(123));  }

  @Test
  public void testCombine() {
    byte[] byteArray  = { 0x01, 0x02, 0x03, 0x04, 0x05 };
    byte[] byteArray2 = { 0x01, 0x02, 0x03, 0x04, 0x05 };
    assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x01, 0x02, 0x03, 0x04, 0x05 }, Util.combine(byteArray, byteArray2));
  }

  @Test
  public void testTrim() {
    byte[] byteArray  = { 0x01, 0x02, 0x03, 0x04, 0x05 };
    assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, Util.trim(byteArray, 3));
  }

  @Test
  public void testSplit() {
    byte[] byteArray  = { 0x01, 0x02, 0x03, 0x04, 0x05 };
    assertArrayEquals(new byte[][]{{0x01}, {0x02}}, Util.split(byteArray, 1, 1));
  }

  @Test
  public void testJoin() {
    @NonNull List<Integer> test3 = new ArrayList<>();
    test3.add(1);
    @NonNull List<Integer> test4 = new ArrayList<>();
    test4.add(2);

    assertEquals(new ArrayList<>(Arrays.asList(1, 2)), Util.join(test3, test4));
  }
}



