public class Boss extends Monstro {

    public Boss() {
        vida = 200;
        ataque = 30;
    }

    @Override
    public void aplicarEscala(int nivelHeroi) {
        int niveisIniciais = Math.min(Math.max(0, nivelHeroi - 1), 2);
        int niveisAvancados = Math.max(0, nivelHeroi - 3);

        vida += (niveisIniciais * 10) + (niveisAvancados * 28);
        ataque += (niveisIniciais * 1) + (niveisAvancados * 4);
    }
}
