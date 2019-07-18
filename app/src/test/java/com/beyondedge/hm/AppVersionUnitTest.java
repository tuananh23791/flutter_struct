package com.beyondedge.hm;

import com.beyondedge.hm.utils.AppVersion;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class AppVersionUnitTest {
    @Test
    public void valueOf_isCorrect() {
        assertEquals(3, AppVersion.valueOf("3"));
    }

    @Test
    public void valueOf_Zero() {
        assertEquals(0, AppVersion.valueOf("0"));
    }

    @Test
    public void valueOf_Empty() {
        assertEquals(0, AppVersion.valueOf(""));
    }

    @Test
    public void valueOf_not_number() {
        assertEquals(0, AppVersion.valueOf("k"));
    }

    //----

    @Test
    public void getStringNum_empty() {
        String[] input = {};
        assertEquals("0", AppVersion.getStringNum(input, 1));
    }

    @Test
    public void getStringNum_over_pos() {
        String[] input = {"1", "2", "3"};
        assertEquals("0", AppVersion.getStringNum(input, 4));
    }

    @Test
    public void getStringNum_correct0() {
        String[] input = {"1", "2", "3"};
        assertEquals("1", AppVersion.getStringNum(input, 0));
    }

    @Test
    public void getStringNum_correct1() {
        String[] input = {"1", "2", "3"};
        assertEquals("2", AppVersion.getStringNum(input, 1));
    }

    @Test
    public void getStringNum_correct2() {
        String[] input = {"1", "2", "3"};
        assertEquals("3", AppVersion.getStringNum(input, 2));
    }

    //***
    // isLager

    //>>>>-----------false

    @Test
    public void isLager_false_special1() {
        String input1 = "";
        String input2 = "0.0.10";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_false_special2() {
        String input1 = null;
        String input2 = "0.0.10";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_false_special3() {
        String input1 = null;
        String input2 = "null";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_false_special4() {
        String input1 = null;
        String input2 = null;
        assertFalse(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_false_special5() {
        String input1 = "";
        String input2 = null;
        assertFalse(AppVersion.isLager(input1, input2));
    }


    @Test
    public void isLager_false_special6() {
        String input1 = "hh";
        String input2 = "1.2.3.4";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    //

    @Test
    public void isLager_false1() {
        String input1 = "0.0.1";
        String input2 = "0.0.10";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_false2() {
        String input1 = "1.0.1";
        String input2 = "2.0.10";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_false3() {
        String input1 = "0.0.1";
        String input2 = "0.1.10";
        assertFalse(AppVersion.isLager(input1, input2));
    }

    //>>>>-----------true

    @Test
    public void isLager_true1() {
        String input1 = "0.0.1";
        String input2 = "0.0.1";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true2() {
        String input1 = "0.1.0";
        String input2 = "0.0.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true3() {
        String input1 = "1.1.0";
        String input2 = "0.0.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true4() {
        String input1 = "9.1.0";
        String input2 = "1.10.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true5() {
        String input1 = "009.10.0";
        String input2 = "01.10.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true6() {
        String input1 = "9.10.0";
        String input2 = "1.10.10.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true7() {
        String input1 = "9.10.0.19";
        String input2 = "1.10.10.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }

    @Test
    public void isLager_true8() {
        String input1 = "9.10.0.19";
        String input2 = "1.10.10";
        assertTrue(AppVersion.isLager(input1, input2));
    }
    //----- isLastVersion

    @Test
    public void isForceUpdate_true1() {
        String input1 = "0.0.1";
        ArrayList<String> list = new ArrayList<>();
        list.add("0.0.1");
        list.add("0.0.2");
        list.add("0.0.3");
        list.add("0.0.4");
        assertTrue(AppVersion.isLastVersion(input1, list));
    }

    @Test
    public void isForceUpdate_true3() {
        String input1 = "0.0.1";
        ArrayList<String> list = new ArrayList<>();
//        list.add("0.0.1");
        list.add("0.0.2");
        list.add("0.0.3");
        list.add("0.0.4");
        assertTrue(AppVersion.isLastVersion(input1, list));
    }

    @Test
    public void isForceUpdate_true4() {
        String input1 = "0.0.15";
        ArrayList<String> list = new ArrayList<>();
        list.add("0.0.10");
        list.add("0.0.22");
        list.add("0.0.33");
        list.add("0.0.44");
        assertTrue(AppVersion.isLastVersion(input1, list));
    }

    //false

    @Test
    public void isForceUpdate_true2() {
        String input1 = "0.0.4";
        ArrayList<String> list = new ArrayList<>();
        list.add("0.0.1");
        list.add("0.0.2");
        list.add("0.0.3");
        list.add("0.0.4");
        assertFalse(AppVersion.isLastVersion(input1, list));
    }

}