/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.beedoojunit;

import java.io.Console;
import java.util.Scanner;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

/**
 *
 * @author Danilo
 */
public class BeedooJUnitTest {

    protected static WebDriver driver;

    public BeedooJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        driver = new EdgeDriver();
        driver.get("https://altio.beedoo.io");
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoginCerto() {

    }

    @Test
    public void testGetDeuCerto() {
        System.out.println("getDeuCerto");
        boolean expResult = false;
        boolean result = BeedooJUnit.getDeuCerto();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void logarErrado() throws InterruptedException {
        Controlador.logar("loginQualquer", "senhaQualquer", driver);
        Thread.sleep(500);
        // logica para verificar se o login foi aceito, funciona através de um pop-up que aparece caso o login falhe
        boolean logado = !(Controlador.existeXPATH("//div[contains(@class, 'alert bs-callout bs-callout-danger fade in alert_beebot')]", driver));
        Thread.sleep(2000);
        driver.quit();
        assertEquals(true, logado);
    }
    
    @Test
    public void logarCerto() throws InterruptedException {
        Controlador.logar(" ",                                                                                                                                                                                                                                               " ", driver);
        Thread.sleep(500);
        // logica para verificar se o login foi aceito, funciona através de um pop-up que aparece caso o login falhe
        boolean logado = !(Controlador.existeXPATH("//div[contains(@class, 'alert bs-callout bs-callout-danger fade in alert_beebot')]", driver));
        Thread.sleep(2000);
        driver.quit();
        assertEquals(true, logado);
    }

    @Test
    public void testConsolidado() throws Exception {
        BeedooJUnit comprar = new BeedooJUnit();
        comprar.consolidado(" ", " ", "comCordao", driver);
        assertEquals(comprar, comprar.getDeuCerto());
    }

}
