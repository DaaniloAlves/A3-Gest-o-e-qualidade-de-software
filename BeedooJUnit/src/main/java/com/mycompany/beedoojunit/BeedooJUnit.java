package com.mycompany.beedoojunit;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openqa.selenium.edge.EdgeDriver;

/**
 *
 * @author Danilo
 */
public class BeedooJUnit extends Controlador {

    public static boolean logado = false;
    static boolean deuCerto = false;
    static public ArrayList<String> produtosComprados;

    public static boolean getDeuCerto() {
        return deuCerto;
    }

    
    public void consolidado(String login, String senha, WebDriver driver) throws InterruptedException {

        try {
            // instanciando um wait explicito
            WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(15));
            try {
                do {
                    // enviando o login e senha para o navegador e clicando no botao de entrar
                    logar(login, senha, driver);

                    Thread.sleep(500);

                    // logica para verificar se o login foi aceito, funciona através de um pop-up que aparece caso o login falhe
                    logado = !(existeXPATH("//div[contains(@class, 'alert bs-callout bs-callout-danger fade in alert_beebot')]", driver));
                    if (!logado) {
                        loginFalhou(driver);
                    } else {
                        System.out.println("Logado com sucesso!");

                        // logica para ler todos os avisos iniciais, se tiver
                        if (existePopUpInicial(driver)) {
                            percorrerPopUp(driver); 
                        }

                        // thread sleep para esperar a pagina carregar e evitar conflitos
                        Thread.sleep(3000);

                        // entrando na loja clicando no carrinho
                        clickClass("fa-shopping-cart", espera);
                        // thread sleep para esperar a pagina carregar e evitar conflitos
                        Thread.sleep(3000);

                        // logica pra definir o item desejado
                        ArrayList<String> itensDesejados = new ArrayList<>();

                            // adicionando um intervalo de muitos produtos
                                for (int i = 22979; i <= 23900; i++) {
                                    if (i != 23280) {
                                        String pd = "product_" + i;
                                        itensDesejados.add(pd);
                                    }
                                }

                        // print para indicar os id's dos produtos selecionados
                        System.out.println("Produtos desejados: " + itensDesejados);

                        ArrayList<String> produtosFinais = addCarrinho(itensDesejados, driver);
                        System.out.println("1 ou mais produtos disponiveis!\n Produtos selecionados: " + addCarrinho(itensDesejados, driver));

                        try {
                            // logica para adicionar varios produtos no carrinho de uma vez
                            for (int i = 0; i == produtosFinais.size() - 1; i++) {
                                // comprando o produto
                                clickID(produtosFinais.get(i), espera);
                                // adicionando no carrinho
                                clickID("addCart", espera);
                                // fechando pop-up
                                clickClass("close", espera);
                            }

                            // thread sleep para esperar a pagina carregar e evitar conflitos
                            Thread.sleep(2000);

                            // metodo para comprar o que tiver no carrinho
                            comprarCarrinho(driver, espera);

                            // produtosComprados recebendo o arraylist dos produtos comprados
                            produtosComprados = produtosFinais;

                            System.out.println("Produtos comprados: " + produtosFinais + ". Automatização concluída!");
                            deuCerto = true;
                            driver.quit();
                        } catch (NullPointerException e) {
                            System.out.println("TENTANDO DE NOVO");
                        }
                    }
                } while (!deuCerto);
            } catch (UnreachableBrowserException | NoSuchWindowException a) {
                driver.quit();
                if (getDeuCerto() == false) {
                    System.out.println("Navegador fechado");
                }
            } catch (NoSuchSessionException | NoSuchElementException ns) {
                driver.quit();
            }
        } catch (SessionNotCreatedException ee) {
            System.out.println("SessionNotCreatedException");
        }
    }
}
