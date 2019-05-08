package com.beyondedge.hm.ui.page.more;

import java.util.Stack;

/**
 * Created by Hoa Nguyen on May 08 2019.
 */
public interface WebInterface {
    Stack<String> getStackPage();

    String defaultPage();
}
