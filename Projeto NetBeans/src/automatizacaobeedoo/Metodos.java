package automatizacaobeedoo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Metodos {

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

    public static void comprarCarrinho(WebDriver driver, WebDriverWait wait) {
        
        // abrindo o carrinho
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Meu carrinho"))).click();
        
        List<WebElement> testeFinal = driver.findElements(By.xpath("//button[contains(@class, 'btn-success')]"));
        Iterator<WebElement> testeitrFinal = testeFinal.iterator();
        for (int i = testeFinal.size(); i >= (testeFinal.size() - 3); i--) {
            try {
                testeitrFinal.next().click();
                System.out.println("Botao encontrado!");
            } catch (ElementNotInteractableException e) {
                System.out.println("Procurando btn-success correto...");
            }
        }

        List<WebElement> testeFinal2 = driver.findElements(By.xpath("//button[contains(@class, 'btn-success')]"));
        Iterator<WebElement> testeitrFinal2 = testeFinal2.iterator();
        for (int i = testeFinal2.size(); i >= (testeFinal.size() - 3); i--) {
            try {
                testeitrFinal2.next().click();
                System.out.println("Botao encontrado!");
            } catch (ElementNotInteractableException e) {
                System.out.println("Procurando btn-success correto...");
            }
        }
    }
}
