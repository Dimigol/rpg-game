public class TaxadBoss extends Monstro {

    public TaxadBoss() {
        vida = 260;
        ataque = 35;
    }

    @Override
    public void aplicarEscala(int nivelHeroi) {
        int niveisIniciais = Math.min(Math.max(0, nivelHeroi - 1), 2);
        int niveisAvancados = Math.max(0, nivelHeroi - 3);

        vida += (niveisIniciais * 14) + (niveisAvancados * 34);
        ataque += (niveisIniciais * 2) + (niveisAvancados * 5);
    }
}
