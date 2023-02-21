package org.thoughtcrime.securesms.util;

import org.junit.Test;
import org.thoughtcrime.securesms.components.ComposeText;


import java.util.ArrayList;
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
    System.out.println(res);
  }

  @Test
  public void testStringJoin() {
    System.out.println(Util.join(new String[]{"123", "321"}, "444444"));
  }

  @Test
  public void testLongJoin() {
    System.out.println(Util.join(new long[]{1111, 2222}, "xxxxxx"));
  }

  @Test
  public void testLongListJoin() {
    List<Long> list = new ArrayList<>();
    list.add(11111L);
    list.add(2222222L);
    System.out.println(Util.join(list, "111111"));
  }

  @Test
  public void testRightPad() {
    String value = "12345";
    String value2 = "1234567";
    int length = 6;
    Util.rightPad(value, length);
    Util.rightPad(value2, length);
  }

  @Test
  public void testIsEmpty() {
    ComposeText value = null;
    Collection<Integer> value2 = null;
    Util.isEmpty(value);
    Util.isEmpty(value2);
  }

  @Test
  public void testGetFirst() {
    Util.getFirstNonEmpty("123", "321");
    Util.getFirstNonEmpty("", "");
  }


  @Test
  public void testIsLong() {
    Util.isLong("1111111111111111111111");
  }


  @Test
  public void getRandom() {
    List<Integer> test = new ArrayList<>();
    Util.halfOffsetFromScale(100, 10.0F);
    Util.toIntExact(12333);
    @Nullable Long first = Long.valueOf(10000);
    long second = 1000;
    Util.isEquals(first, second);
    Util.getPrettyFileSize(123);
    byte[] byteArray = {0x01, 0x02, 0x03, 0x04, 0x05};
    byte[] byteArray2 = {0x01, 0x02, 0x03, 0x04, 0x05};
    Util.combine(byteArray, byteArray2);
    Util.trim(byteArray, 3);
    Util.split(byteArray, 1, 1);
    @NonNull List<Integer> test3 = new ArrayList<>();
    test3.add(1);
    @NonNull List<Integer> test4 = new ArrayList<>();
    test4.add(2);
    Util.join(test3, test4);
    test.add(1);
    test.add(2);
    test.add(3);
    Util.getRandomElement(test);
  }

}
