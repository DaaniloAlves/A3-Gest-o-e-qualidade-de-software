package automatizacaobeedoo;

import static automatizacaobeedoo.Main.getDeuCerto;
import static automatizacaobeedoo.Main.produtosComprados;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
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

/**
 *
 * @author Danilo
 */
public class Main extends Metodos {

    static boolean deuCerto = false;
    static public ArrayList<String> produtosComprados;

    public static boolean getDeuCerto() {
        return deuCerto;
    }

    public static void automatizacaoBeedoo(JFrame jf, String login, String senha, String acao) throws InterruptedException {

        try {
            // inicia o chrome
            WebDriver driver = new ChromeDriver();

            // instanciando um wait explicito
            WebDriverWait espera = new WebDriverWait(driver, Duration.ofSeconds(15));
            driver.manage().window().maximize();

            try {
                do {
                    // entrando no site do beedoo
                    driver.get("https://altio.beedoo.io/login");

                    // enviando o login e senha para o navegador e clicando no botao de entrar
                    driver.findElement(By.id("inputLogin")).sendKeys(login);
                    driver.findElement(By.id("inputPassword")).sendKeys(senha);
                    driver.findElement(By.className("btn")).click();

                    Thread.sleep(500);

                    // logica para verificar se o login foi aceito, funciona através de um pop-up que aparece caso o login falhe
                    boolean loginCerto = !(existeXPATH("//div[contains(@class, 'alert bs-callout bs-callout-danger fade in alert_beebot')]", driver));
                    if (!loginCerto) {
                        driver.quit();
                        jf.show();
                        System.out.println("Login ou senha inválidos");
                        JOptionPane.showMessageDialog(jf, "Usuário ou senha inválidos", "Aviso", 2);
                    } else {

                        System.out.println("Logado com sucesso!");

                        // thread sleep para esperar a pagina carregar e evitar conflitos
                        Thread.sleep(10000);

                        // verificando se existe a caixa de notificações inicial
                        boolean popupInicial = existeXPATH("//div[contains(@class, 'box-content list-required-posts')]", driver);
                        System.out.println("Existe o popup inicial: " + popupInicial);

                        // logica para ler todos os avisos iniciais, se tiver
                        if (popupInicial) {

                            // criando um iterator de uma lista de todas as notificações para clicar nelas
                            List<WebElement> listaItems = driver.findElements(By.className("post-item"));
                            Iterator<WebElement> itr = listaItems.iterator();
                            while (itr.hasNext()) {
                                itr.next().click();
                            }

                            // thread sleep para esperar a pagina carregar e evitar conflitos
                            Thread.sleep(1000);

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

                        // thread sleep para esperar a pagina carregar e evitar conflitos
                        Thread.sleep(3000);

                        // entrando na loja clicando no carrinho
                        espera.until(ExpectedConditions.elementToBeClickable(By.className("fa-shopping-cart"))).click();

                        // thread sleep para esperar a pagina carregar e evitar conflitos
                        Thread.sleep(3000);

                        // logica pra definir o item desejado
                        ArrayList<String> itensDesejados = new ArrayList<>();

                        switch (acao) {
                            // adicionando apenas o cordao, se a caixinha do cordao estiver marcada
                            case "comCordao" ->
                                itensDesejados.add("product_20431");
                            // adicionando muitos produtos, se a caixinha do cordao estiver desmarcada
                            case "semCordao" -> {
                                for (int i = 22979; i <= 23900; i++) {
                                    if (i != 23280) {
                                        String pd = "product_" + i;
                                        itensDesejados.add(pd);
                                    }
                                }
                            }
                            // print para verificar se algum parametro foi passado errado
                            default ->
                                System.out.println("Array vazio, 'string acao' teve valor errado");
                        }

                        // print para indicar os id's dos produtos selecionados
                        System.out.println("Produtos desejados: " + itensDesejados);

                        ArrayList<String> produtosFinais = addCarrinho(itensDesejados, driver);
                        System.out.println("1 ou mais produtos disponiveis!\n Produtos selecionados: " + addCarrinho(itensDesejados, driver));

                        try {
                            // logica para adicionar varios produtos no carrinho de uma vez
                            for (int i = 0; i == produtosFinais.size() - 1; i++) {
                                // comprando o produto
                                espera.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(produtosFinais.get(i))))).click();
                                // adicionando no carrinho
                                espera.until(ExpectedConditions.elementToBeClickable(By.id("addCart"))).click();
                                // fechando pop-up
                                espera.until(ExpectedConditions.elementToBeClickable(By.className("close"))).click();
                            }

                            // thread sleep para esperar a pagina carregar e evitar conflitos
                            Thread.sleep(2000);

                            // metodo para comprar o que tiver no carrinho
                            comprarCarrinho(driver, espera);

                            // produtosComprados recebendo o arraylist dos produtos comprados
                            produtosComprados = produtosFinais;

                            System.out.println("Produtos comprados: " + produtosFinais + ". Automatização concluída!");
                            deuCerto = true;
                            Thread.sleep(5000);
                            driver.quit();
                            JOptionPane.showMessageDialog(jf, "Compra(s) efetuada(s)!\nOs id's dos produtos comprados são: " + Main.produtosComprados, "Aviso", 1);
                        } catch (NullPointerException e) {
                            System.out.println("TENTANDO DE NOVO");
                        }
                    }
                } while (!deuCerto);
            } catch (UnreachableBrowserException | NoSuchWindowException a) {
                driver.quit();
                jf.show();
                if (getDeuCerto() == false) {
                    JOptionPane.showMessageDialog(jf, "Navegador fechado\nInterrompendo o programa", "Aviso", 2);
                } 
            } catch (NoSuchSessionException ns) {

            }
        } catch (SessionNotCreatedException ee) {
            JOptionPane.showMessageDialog(jf, "Exception detectada", "Aviso", 2);
        }
    }
}
