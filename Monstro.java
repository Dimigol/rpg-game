import java.util.Random;

public class Monstro {

    protected int vida = 80;
    protected int ataque = 15;
    protected Random random = new Random();

    public int atacar() {
        return ataque + random.nextInt(10);
    }

    public void aplicarEscala(int nivelHeroi) {
        int niveisIniciais = Math.min(Math.max(0, nivelHeroi - 1), 2);
        int niveisAvancados = Math.max(0, nivelHeroi - 3);

        vida += (niveisIniciais * 6) + (niveisAvancados * 16);
        ataque += (niveisIniciais * 1) + (niveisAvancados * 3);
    }

    public void receberDano(int dano) {
        vida -= dano;
        if (vida < 0) vida = 0;
    }

    public boolean estaVivo() { return vida > 0; }

    public int getVida() { return vida; }
}
