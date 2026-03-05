import java.util.Random;
import java.util.Scanner;

public class Mapa {

    private Random random = new Random();

    public void explorarSala(Personagem heroi, Scanner in) {

        int evento = random.nextInt(100);

        if (evento < 60) {
            new Sala().explorar(heroi, in);
        } else if (evento < 90) {
            int ouroEncontrado = random.nextInt(41) + 10;
            System.out.println("Voce encontrou um bau e ganhou " + ouroEncontrado + " de ouro!");
            heroi.adicionarOuro(ouroEncontrado);
        } else {
            int tipoBoss = random.nextInt(3);

            if (tipoBoss == 0) {
                Boss boss = new Boss();
                Combate.lutar(heroi, boss, "Lula", 70, 90, in);
            } else if (tipoBoss == 1) {
                XandaoBoss boss = new XandaoBoss();
                Combate.lutar(heroi, boss, "Xandao \"o cabeca de ovo\"", 85, 110, in);
            } else {
                TaxadBoss boss = new TaxadBoss();
                Combate.lutar(heroi, boss, "Taxad", 100, 130, in);
            }
        }
    }
}
