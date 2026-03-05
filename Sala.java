import java.util.Scanner;
import java.util.Random;

public class Sala {

    private Random random = new Random();

    public void explorar(Personagem heroi, Scanner in) {
        int tipo = random.nextInt(4);

        Monstro monstro;
        String nomeMonstro;
        int recompensaOuro;
        int recompensaXp;

        if (tipo == 0) {
            monstro = new Monstro();
            nomeMonstro = "Monstro Selvagem";
            recompensaOuro = 18;
            recompensaXp = 25;
        } else if (tipo == 1) {
            monstro = new Goblin();
            nomeMonstro = "Goblin";
            recompensaOuro = 15;
            recompensaXp = 20;
        } else if (tipo == 2) {
            monstro = new Esqueleto();
            nomeMonstro = "Esqueleto";
            recompensaOuro = 22;
            recompensaXp = 30;
        } else {
            monstro = new Orc();
            nomeMonstro = "Orc";
            recompensaOuro = 28;
            recompensaXp = 40;
        }

        Combate.lutar(heroi, monstro, nomeMonstro, recompensaOuro, recompensaXp, in);
    }
}
