package com.vodotiiets;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Denys Vodotiiets.
 */
public class OpenAddressingHashMapTest {

    private OpenAddressingHashMap map;
    private final int SIZE = 100;

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentExceptionIfCapacityIsNegative() {
        map = new OpenAddressingHashMap(-1);
    }

    @Test()
    public void sizeShouldBeZero() {
        map = new OpenAddressingHashMap();
        Assert.assertTrue(map.size() == 0);
    }

    @Test()
    public void nodeShouldBeAdded() {
        map = new OpenAddressingHashMap();
        Assert.assertTrue(map.put(1, 2) && map.size() > 0);
    }

    @Test()
    public void secondNodeShouldBeAddedButSizeStillOne() {
        map = new OpenAddressingHashMap();
        map.put(1, 2);
        Assert.assertTrue(map.put(1, 3) && map.size() == 1);
    }

    @Test()
    public void allNodesShouldBeAdded() {
        map = new OpenAddressingHashMap(SIZE);

        for (int i = 0; i < SIZE; i++) {
            Assert.assertTrue(map.put(i, (long)i) && map.size() == i + 1);
        }

        Assert.assertTrue(map.size() == SIZE);
    }

    @Test()
    public void receivedValueShouldBeTheSame() {
        map = new OpenAddressingHashMap();
        long number = 2;
        map.put(1, number);
        Assert.assertEquals(number , map.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionIfTryGetWhenMapIsEmpty() {
        map = new OpenAddressingHashMap();
        map.get(1);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionIfTryGetWhenMapHasNoSuchKey() {
        map = new OpenAddressingHashMap();
        map.put(1, 2);
        map.get(2);
    }

    @Test(expected = IllegalStateException.class)
    public void throwsIllegalStateExceptionIfTryPutWhenMapIsFull() {
        map = new OpenAddressingHashMap(SIZE);

        for (int i = 0; i < SIZE * 3 / 2 + 2; i++) {
            map.put(i, (long)i);
        }
    }

}