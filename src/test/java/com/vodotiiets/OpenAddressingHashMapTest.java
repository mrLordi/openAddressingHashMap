package com.vodotiiets;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Denys Vodotiiets.
 */
public class OpenAddressingHashMapTest {

    private OpenAddressingHashMap<Integer, Long> map;
    private final int SIZE = 100;

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfCapacityIsNegative() {
        map = new OpenAddressingHashMap<>(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfLoadFactorIsNotPositive() {
        map = new OpenAddressingHashMap<>(16, 0);
    }

    @Test()
    public void sizeShouldBeZero() {
        map = new OpenAddressingHashMap<>();
        Assert.assertTrue(map.size() == 0);
    }

    @Test()
    public void nodeShouldBeAdded() {
        map = new OpenAddressingHashMap<>();
        Assert.assertTrue(map.put(1, 2L) && map.size() > 0);
    }

    @Test()
    public void secondNodeShouldNotBeAdded() {
        map = new OpenAddressingHashMap<>();
        map.put(1, 2L);
        Assert.assertFalse(map.putOnlyIfAbsent(1, 2L));
    }

    @Test()
    public void secondNodeShouldBeAddedButSizeStillOne() {
        map = new OpenAddressingHashMap<>();
        map.put(1, 2L);
        Assert.assertTrue(map.put(1, 3L) && map.size() == 1);
    }

    @Test()
    public void allNodesShouldBeAdded() {
        map = new OpenAddressingHashMap<>();

        for (int i = 0; i < SIZE; i++) {
            Assert.assertTrue(map.put(i, (long)i) && map.size() == i + 1);
        }

        Assert.assertTrue(map.size() == SIZE);
    }

    @Test()
    public void allNodesShouldNotBeAddedExceptFirst() {
        map = new OpenAddressingHashMap<>();
        map.put(1, 1L);

        for (int i = 0; i < SIZE; i++) {
            Assert.assertFalse(map.putOnlyIfAbsent(1, (long)i));
        }

        Assert.assertTrue(map.size() == 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfKeyIsNull() {
        map = new OpenAddressingHashMap<>();
        Assert.assertTrue(map.put(null, 1L));
    }

    @Test()
    public void receivedValueShouldBeTheSame() {
        map = new OpenAddressingHashMap<>();
        Long number = 2L;
        map.put(1, number);
        Assert.assertEquals(number , map.get(1));
    }

    @Test()
    public void receivedValueShouldBeNull() {
        map = new OpenAddressingHashMap<>();
        Assert.assertNull(map.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfGetKeyIsNull() {
        map = new OpenAddressingHashMap<>();
        map.get(null);
    }

}