package com.mycompany.beedoojunit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Controlador {

    public static boolean existeXPATH(String st, WebDriver driver) {

        // metodo que facilita a verificaçao do xpath
        try {
            driver.findElement(By.xpath(st));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    public static boolean existeID(String st, WebDriver driver) {

        // metodo que facilita a verificaçao do id
        try {
            driver.findElement(By.id(st));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    public static void clickID(String id, WebDriverWait waiter) {
        waiter.until(ExpectedConditions.elementToBeClickable(By.id(id))).click();
    }
    public static void clickClass(String className, WebDriverWait waiter) {
        waiter.until(ExpectedConditions.elementToBeClickable(By.className(className))).click();
    }

    public static void logar(String login, String senha, WebDriver driver) {
        driver.findElement(By.id("inputLogin")).sendKeys(login);
        driver.findElement(By.id("inputPassword")).sendKeys(senha);
        driver.findElement(By.className("btn")).click();
    }

    public static void loginFalhou(WebDriver driver) {
        driver.quit();
        System.out.println("Login ou senha inválidos");
    }

    public static boolean existePopUpInicial(WebDriver driver) throws InterruptedException {
        // thread sleep para esperar a pagina carregar e evitar conflitos
        Thread.sleep(10000);

        // verificando se existe a caixa de notificações inicial
        boolean popupInicial = existeXPATH("//div[contains(@class, 'box-content list-required-posts')]", driver);
        return popupInicial;
    }

    public static void percorrerPopUp(WebDriver driver) throws InterruptedException {
        // criando um iterator de uma lista de todas as notificações para clicar nelas
        List<WebElement> listaItems = driver.findElements(By.className("post-item"));
        Iterator<WebElement> itr = listaItems.iterator();
        while (itr.hasNext()) {
            itr.next().click();
        }

        // thread sleep para esperar a pagina carregar e evitar conflitos
        Thread.sleep(3000);

        // criando um iterator de uma lista de todos os botoes de classe "btn-gray"
        List<WebElement> btnGray = driver.findElements(By.xpath("//button[contains(@class, 'btn-gray')]"));
        Iterator<WebElement> btnGrayItr = btnGray.iterator();
        System.out.println("botoes 'btn-gray' encontrados: " + btnGray.size());
        boolean btnGrayEncontrado = false;
        do {
            try {
                btnGrayItr.next().click();
                System.out.println("Botao encontrado!");
                btnGrayEncontrado = true;
            } catch (ElementNotInteractableException | TimeoutException e) {
                System.out.println("Procurando btn-gray correto...");
            }
        } while (!btnGrayEncontrado);
    }

    public static ArrayList addCarrinho(ArrayList<String> e, WebDriver driver) throws InterruptedException {

        // metodo para verificar se algum produto da lista está disponivel, e se estiver, adicionar ao carrinho
        ArrayList<String> produtosAdicionados = new ArrayList<>();

        boolean comprou = true;
        boolean produtoDisponivel = false;

        while (!produtoDisponivel) {
            Iterator<String> lista = e.iterator();

            while (lista.hasNext()) {
                String next = lista.next();
                try {
                    if (existeID(next, driver)) {
                        produtosAdicionados.add(next);
                        produtoDisponivel = true;
                    }
                } catch (NoSuchElementException a) {
                }
            }

            String url = "https://altio.beedoo.io/store";
            if (!driver.getCurrentUrl().equals(url)) {
                produtoDisponivel = true;
                comprou = false;
            }
            if (!produtoDisponivel) {
                Thread.sleep(15000);
                System.out.println("atualizando...");
                driver.navigate().refresh();
            }
        }
        if (comprou == true) {
            return produtosAdicionados;
        } else {
            return null;
        }

    }

    public static void comprarCarrinho(WebDriver driver, WebDriverWait wait) throws InterruptedException {

        // abrindo o carrinho
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Meu carrinho"))).click();

        Thread.sleep(2000);

        List<WebElement> btnSuccess = driver.findElements(By.xpath("//button[contains(@class, 'btn-success')]"));
        Iterator<WebElement> btnSuccessItr = btnSuccess.iterator();
        System.out.println("botoes 'btn-success' encontrados: " + btnSuccess.size());
        boolean btnSuccessEncontrado = false;
        do {
            try {
                btnSuccessItr.next().click();
                System.out.println("Botao encontrado!");
                btnSuccessEncontrado = true;
            } catch (ElementNotInteractableException | TimeoutException e) {
                System.out.println("Procurando btn-success correto...");
            }
        } while (!btnSuccessEncontrado);

        Thread.sleep(2000);

        List<WebElement> btnSuccess2 = driver.findElements(By.xpath("//button[contains(@class, 'btn-success')]"));
        Iterator<WebElement> btnSuccess2Itr = btnSuccess2.iterator();
        System.out.println("botoes 'btn-success' encontrados: " + btnSuccess2.size());
        boolean btnSuccess2Encontrado = false;
        do {
            try {
                btnSuccess2Itr.next().click();
                System.out.println("Botao encontrado!");
                btnSuccess2Encontrado = true;
            } catch (ElementNotInteractableException | TimeoutException e) {
                System.out.println("Procurando btn-success2 correto...");
            }
        } while (!btnSuccess2Encontrado);
    }
}
