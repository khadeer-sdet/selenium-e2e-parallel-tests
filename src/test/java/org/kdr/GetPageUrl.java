package org.kdr;

import org.kdr.base.TestBase;
import org.testng.annotations.Test;

public class GetPageUrl extends TestBase {
    @Test
    public void tc_02_validate_page_url() {
        String url = getDriver().getCurrentUrl();
        String propUrl = prop.getProperty("url");
        assert(url.equals(propUrl));
    }
}
