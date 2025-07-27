package org.kdr;

import org.kdr.base.TestBase;
import org.testng.annotations.Test;

public class GetPageTitle extends TestBase {

    @Test
    public void tc_01_validate_webpage_title() {
        String title = getDriver().getTitle();
        assert(title.equals("OrangeHRM"));
    }
}
