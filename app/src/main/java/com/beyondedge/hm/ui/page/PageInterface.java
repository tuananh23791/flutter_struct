package com.beyondedge.hm.ui.page;

import com.beyondedge.hm.ui.page.more.WebInterface;

/**
 * Created by Hoa Nguyen on Apr 23 2019.
 */
public interface PageInterface extends WebInterface {
    void refresh();

    void willBeDisplayed();

    void willBeHidden();

}
