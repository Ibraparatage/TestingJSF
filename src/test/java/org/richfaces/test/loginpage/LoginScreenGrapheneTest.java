/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 **/
package org.richfaces.test.loginpage;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.test.graphene.Credentials;
import org.richfaces.test.graphene.LoginController;
import org.richfaces.test.graphene.User;

import java.io.File;
import java.net.URL;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */

@RunWith(Arquillian.class)
public class LoginScreenGrapheneTest {
    private static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "login.war")
                .addClasses(Credentials.class, User.class, LoginController.class)
                .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(
                        new StringAsset("<faces-config version=\"2.0\"/>"),
                        "faces-config.xml");
    }

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL contextPath;

    @FindBy(id="loginForm:username")
    private WebElement usernameInput;

    @FindBy(id="loginForm:password")
    private WebElement passwordInput;

    @FindBy(id="loginForm:login")
    private WebElement loginButton;

    @Test
    @RunAsClient
    public void should_login_successfully() throws Exception {
        String page = contextPath + "login.jsf";
        browser.get(page);

        usernameInput.sendKeys("demo");
        passwordInput.sendKeys("demo");
        loginButton.click();

        Assert.assertTrue("User should be logged in!",
                browser.findElements(By.xpath("//li[contains(text(), 'Welcome')]")).size() > 0);
    }

}