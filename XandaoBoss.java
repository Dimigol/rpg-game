public class XandaoBoss extends Monstro {

    public XandaoBoss() {
        vida = 230;
        ataque = 32;
    }

    @Override
    public void aplicarEscala(int nivelHeroi) {
        int niveisIniciais = Math.min(Math.max(0, nivelHeroi - 1), 2);
        int niveisAvancados = Math.max(0, nivelHeroi - 3);

        vida += (niveisIniciais * 12) + (niveisAvancados * 30);
        ataque += (niveisIniciais * 1) + (niveisAvancados * 4);
    }
}
