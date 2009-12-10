/**
 * Copyright 2009 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */
package se.vgregion.portal.integrationtest.selenium;

import com.thoughtworks.selenium.SeleneseTestCase;

public class PortalStructureIntegrationTest extends SeleneseTestCase {
    @Override
    public void setUp() throws Exception {
        System.out.println("**** SELENIUM INTEGRATION TEST ****");
        setUp("http://antonio.vgregion.se:8080/", "*chrome");
    }

    public void testPortalStructure() throws Exception {
        selenium.open("/web/guest");
        selenium.waitForPageToLoad("10000");
        selenium.type("_58_login", "xxtst1");
        selenium.type("_58_password", "password1");
        if (selenium.isElementPresent("link=- Ny användare")) {
            selenium.click("link=- Ny användare");
        }
        if (selenium.isElementPresent("link=- Glömt lösenord")) {
            selenium.click("link=- Glömt lösenord");
        }
        selenium.click("//input[@value='Logga in']");
        selenium.waitForPageToLoad("10000");
        // USD web service is not too reliable
        // if (selenium.isElementPresent("link=- Uppdatera")) {
        // selenium.click("//input[@value='Uppdatera']");
        // }
        // selenium.waitForPageToLoad("10000");
        selenium.click("//ul[@id='nav-main']/li[2]/a/span");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=iNotes");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("iNotes"));
        selenium.click("link=Webbkonferens");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("iNotes"));
        selenium.click("link=Dokumentarkiv");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("Visa Dokumentbibliotek"));
        verifyTrue(selenium.isElementPresent("link=Tillgång från mitt skrivbord"));
        selenium.click("link=Kontakter");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Nästa");
        selenium.selectFrame("_csiframe_WAR_csiframecoremoduleportlet_INSTANCE_2B6b_iframe");
        selenium.selectFrame("relative=up");
        selenium.click("link=Regionkalendern");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("iNotes"));
        selenium.click("link=SAMBO");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("iNotes"));
        selenium.click("link=Resebokning");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("iNotes"));
        selenium.click("link=Clarity");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("Clarity"));
        selenium.click("link=Kom & Gå");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("KomOGå"));
        selenium.click("//ul[@id='nav-main']/li[3]/a/span");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Utdataplattform");
        selenium.waitForPageToLoad("10000");
        selenium.selectFrame("_csiframe_WAR_csiframecoremoduleportlet_INSTANCE_Qtm1_iframe");
        verifyTrue(selenium.isElementPresent("loginCancelButtonContainer"));
        selenium.selectFrame("relative=up");
        selenium.click("link=Personaladministration");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=paleko");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isElementPresent("_csiframe_WAR_csiframecoremoduleportlet_INSTANCE_m1rH_iframe"));
        selenium.click("link=Fakturaportalen");
        selenium.waitForPageToLoad("10000");
        selenium.selectFrame("_csiframe_WAR_csiframecoremoduleportlet_INSTANCE_3Uts_iframe");
        // verifyTrue(selenium.isElementPresent("//img"));
        selenium.selectFrame("relative=up");
        selenium.click("link=Självservice RH");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("Självservice"));
        selenium.click("link=KivAdmin");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isTextPresent("KivAdmin"));
        verifyTrue(selenium.isElementPresent("link=Raindance Portal"));
        selenium.click("//ul[@id='nav-main']/li[4]/a/span");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Välkommen");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Läkemedelsförteckningen");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Bild- och funktionsregistret");
        selenium.waitForPageToLoad("10000");
        selenium.type("personId", "a");
        selenium.click("//input[@value='Sök']");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Idoc24");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=InfoBroker");
        selenium.waitForPageToLoad("10000");
        selenium.click("//ul[@id='nav-main']/li[5]/a/span");
        selenium.waitForPageToLoad("10000");
        selenium.click("//a[contains(text(),'Hitta')]");
        selenium.waitForPageToLoad("10000");
        selenium.type("q", "a");
        selenium.click("//input[@value='Sök']");
        selenium.waitForPageToLoad("10000");
        selenium.click("//div[2]/div[1]/div[1]/div[1]");
        selenium.click("//div[2]/div[1]/div[1]/div[1]");
        selenium.click("link=SökiKiv");
        selenium.waitForPageToLoad("10000");
        verifyTrue(selenium.isElementPresent("advancedLinkText"));
        selenium.click("//ul[@id='nav-main']/li[6]/a/span");
        selenium.waitForPageToLoad("10000");
        selenium.click("xf-32-opsitem2");
        selenium.click("xf-37-opsitem0");
        selenium.click("xf-37-opsitem1");
        selenium.click("xf-37-opsitem2");
        selenium.click("xf-41-opsitem2");
        selenium.click("xf-41-opsitem1");
        selenium.click("xf-41-opsitem0");
        selenium.click("xf-45-opsitem0");
        selenium.click("xf-56-opsitem0");
        selenium.click("xf-58-opsitem0");
        selenium.click("xf-61-opsitem0");
        selenium.click("xf-71");
        selenium.click("xf-71");
        selenium.click("xf-67");
        selenium.click("xf-67");
        selenium.click("xf-83");
        selenium.waitForPageToLoad("10000");
        selenium.click("//ul[@id='nav-main']/li[7]/a/span");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Hem");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Kontrollpanel");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Tillbaka till VGRegion");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Mina uppgifter");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Tillbaka till VGRegion");
        selenium.waitForPageToLoad("10000");
        selenium.click("link=Logga ut");
        selenium.waitForPageToLoad("10000");
    }
}
