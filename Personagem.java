import java.util.Random;

public class Personagem {

    private String nome;
    private int vida;
    private int vidaMaxima;
    private int ouro;
    private int nivel;
    private int xp;
    private int xpProximoNivel;

    private int ataqueBase;
    private int bonusArma;
    private int bonusEscudo;
    private int bonusArmadura;

    private String armaEquipada;
    private String escudoEquipado;
    private String armaduraEquipada;

    private Inventario inventario;
    private int buffTurnos;
    private Random random;

    public Personagem(String nome) {
        this.nome = nome;
        this.vidaMaxima = 120;
        this.vida = vidaMaxima;
        this.ouro = 50;
        this.nivel = 1;
        this.xp = 0;
        this.xpProximoNivel = 100;

        this.ataqueBase = 20;
        this.bonusArma = 0;
        this.bonusEscudo = 0;
        this.bonusArmadura = 0;

        this.armaEquipada = "Nenhuma";
        this.escudoEquipado = "Nenhum";
        this.armaduraEquipada = "Nenhuma";

        this.inventario = new Inventario();
        this.buffTurnos = 0;
        this.random = new Random();
    }

    public int atacar() {
        return getAtaqueTotal() + random.nextInt(10);
    }

    public void receberDano(int dano) {
        int danoFinal = dano - getDefesaTotal();
        if (danoFinal < 1) {
            danoFinal = 1;
        }

        vida -= danoFinal;
        if (vida < 0) {
            vida = 0;
        }

        System.out.println("Sua defesa bloqueou " + (dano - danoFinal) + " de dano.");
    }

    public void curar(int valor) {
        vida += valor;
        if (vida > vidaMaxima) {
            vida = vidaMaxima;
        }
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public String getNome() {
        return nome;
    }

    public int getOuro() {
        return ouro;
    }

    public int getNivel() {
        return nivel;
    }

    public int getXp() {
        return xp;
    }

    public int getXpProximoNivel() {
        return xpProximoNivel;
    }

    public int getAtaqueBase() {
        return ataqueBase;
    }

    public int getBonusArma() {
        return bonusArma;
    }

    public int getBonusEscudo() {
        return bonusEscudo;
    }

    public int getBonusArmadura() {
        return bonusArmadura;
    }

    public String getArmaEquipada() {
        return armaEquipada;
    }

    public String getEscudoEquipado() {
        return escudoEquipado;
    }

    public String getArmaduraEquipada() {
        return armaduraEquipada;
    }

    public int getAtaqueTotal() {
        int bonusSolar = buffTurnos > 0 ? 10 : 0;
        return ataqueBase + bonusArma + bonusSolar;
    }

    public int getDefesaTotal() {
        return bonusEscudo + bonusArmadura;
    }

    public String getResumoEquipamentos() {
        return "Arma: " + armaEquipada + " | Escudo: " + escudoEquipado + " | Armadura: " + armaduraEquipada;
    }

    public void adicionarOuro(int valor) {
        ouro += valor;
    }

    public void adicionarXp(int valor) {
        if (valor <= 0) {
            return;
        }

        xp += valor;
        System.out.println("Voce ganhou " + valor + " de XP.");

        while (xp >= xpProximoNivel) {
            xp -= xpProximoNivel;
            subirNivel();
        }
    }

    public boolean gastarOuro(int valor) {
        if (ouro >= valor) {
            ouro -= valor;
            return true;
        }
        return false;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void equiparEspada(String nomeEspada, int bonus) {
        this.armaEquipada = nomeEspada;
        this.bonusArma = bonus;
        System.out.println("Espada equipada: " + nomeEspada + " (+" + bonus + " ATQ)");
    }

    public void equiparEscudo(String nomeEscudo, int bonus) {
        this.escudoEquipado = nomeEscudo;
        this.bonusEscudo = bonus;
        System.out.println("Escudo equipado: " + nomeEscudo + " (+" + bonus + " DEF)");
    }

    public void equiparArmadura(String nomeArmadura, int bonus) {
        this.armaduraEquipada = nomeArmadura;
        this.bonusArmadura = bonus;
        System.out.println("Armadura equipada: " + nomeArmadura + " (+" + bonus + " DEF)");
    }

    public void exposicaoSolar() {

        if (buffTurnos > 0) {
            System.out.println("seus testiculos ja estao expostos por " + buffTurnos + " turnos.");
            return;
        }

        System.out.println("Voce colocou os testiculos no sol.");
        System.out.println("Ataque aumentado por 3 turnos.");

        buffTurnos = 3;
    }

    public void atualizarBuff() {

        if (buffTurnos > 0) {
            buffTurnos--;

            if (buffTurnos == 0) {
                System.out.println("A energia solar se dissipou.");
            }
        }
    }

    public int getBuffTurnos() {
        return buffTurnos;
    }

    public void aplicarEstadoSalvo(
        int vida,
        int vidaMaxima,
        int ouro,
        int nivel,
        int xp,
        int xpProximoNivel,
        int ataqueBase,
        int bonusArma,
        int bonusEscudo,
        int bonusArmadura,
        String armaEquipada,
        String escudoEquipado,
        String armaduraEquipada,
        int buffTurnos
    ) {
        this.vidaMaxima = Math.max(1, vidaMaxima);
        this.vida = Math.max(0, Math.min(vida, this.vidaMaxima));
        this.ouro = Math.max(0, ouro);
        this.nivel = Math.max(1, nivel);
        this.xp = Math.max(0, xp);
        this.xpProximoNivel = Math.max(1, xpProximoNivel);
        this.ataqueBase = Math.max(1, ataqueBase);
        this.bonusArma = Math.max(0, bonusArma);
        this.bonusEscudo = Math.max(0, bonusEscudo);
        this.bonusArmadura = Math.max(0, bonusArmadura);
        this.armaEquipada = armaEquipada;
        this.escudoEquipado = escudoEquipado;
        this.armaduraEquipada = armaduraEquipada;
        this.buffTurnos = Math.max(0, buffTurnos);
    }

    private void subirNivel() {
        nivel++;
        vidaMaxima += 10;
        ataqueBase += 2;
        vida = vidaMaxima;
        xpProximoNivel += 55;

        System.out.println("=== LEVEL UP! ===");
        System.out.println("Voce chegou ao nivel " + nivel + ".");
        System.out.println("+10 de vida maxima e +2 de ataque base.");
        System.out.println("Vida restaurada completamente.");
    }
}
