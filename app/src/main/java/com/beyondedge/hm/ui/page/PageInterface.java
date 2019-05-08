package com.beyondedge.hm.ui.page;

import java.util.Stack;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public interface PageInterface {
    void refresh();

    void willBeDisplayed();

    void willBeHidden();

    Stack<String> getStackPage();

    String defaultPage();
}
