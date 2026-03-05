import java.util.Scanner;
import java.util.Random;

public class Combate {

    private static final Random random = new Random();

    public static boolean lutar(Personagem heroi, Monstro inimigo, String nomeInimigo, int recompensaOuro, int recompensaXp, Scanner in) {
        inimigo.aplicarEscala(heroi.getNivel());

        System.out.println("\n=== BATALHA ===");
        System.out.println("Um " + nomeInimigo + " apareceu!");

        while (heroi.estaVivo() && inimigo.estaVivo()) {
            mostrarStatus(heroi, inimigo, nomeInimigo);
            System.out.println("1 - Atacar");
            System.out.println("2 - Usar pocao");
            System.out.println("3 - Colocar os testiculos no sol");
            System.out.println("4 - Fugir");
            System.out.print("Acao: ");

            int acao = lerOpcao(in);
            int resultadoAcao = executarAcao(acao, heroi, inimigo, nomeInimigo, in);

            if (resultadoAcao == 1) {
                System.out.println("Voce conseguiu fugir da batalha.");
                return false;
            }

            if (resultadoAcao == -1) {
                continue;
            }

            if (inimigo.estaVivo()) {
                int danoInimigo = inimigo.atacar();
                heroi.receberDano(danoInimigo);
                System.out.println(nomeInimigo + " atacou e causou " + danoInimigo + " de dano.");
            }

            heroi.atualizarBuff();
        }

        if (heroi.estaVivo()) {
            heroi.adicionarOuro(recompensaOuro);
            heroi.adicionarXp(recompensaXp);
            System.out.println("Voce venceu a batalha e ganhou " + recompensaOuro + " de ouro!");
            return true;
        }

        return false;
    }

    private static void mostrarStatus(Personagem heroi, Monstro inimigo, String nomeInimigo) {
        System.out.println("\n------------------------------");
        System.out.println("Heroi: " + heroi.getVida() + "/" + heroi.getVidaMaxima() + " HP | Ouro: " + heroi.getOuro());
        System.out.println("Nivel: " + heroi.getNivel() + " | XP: " + heroi.getXp() + "/" + heroi.getXpProximoNivel());
        System.out.println("ATQ: " + heroi.getAtaqueTotal() + " | DEF: " + heroi.getDefesaTotal());
        System.out.println(nomeInimigo + ": " + inimigo.getVida() + " HP");
        System.out.println("------------------------------");
    }

    private static int lerOpcao(Scanner in) {
        if (!in.hasNextInt()) {
            in.nextLine();
            return -1;
        }
        int opcao = in.nextInt();
        in.nextLine();
        return opcao;
    }

    private static int executarAcao(int acao, Personagem heroi, Monstro inimigo, String nomeInimigo, Scanner in) {
        switch (acao) {
            case 1:
                int dano = heroi.atacar();
                inimigo.receberDano(dano);
                System.out.println("Voce atacou e causou " + dano + " de dano em " + nomeInimigo + ".");
                return 0;
            case 2:
                return usarPocao(heroi, in) ? 0 : -1;
            case 3:
                heroi.exposicaoSolar();
                return 0;
            case 4:
                return tentarFugir() ? 1 : 0;
            default:
                System.out.println("Opcao invalida. Escolha 1, 2, 3 ou 4.");
                return -1;
        }
    }

    private static boolean tentarFugir() {
        boolean sucesso = random.nextInt(100) < 70;
        if (!sucesso) {
            System.out.println("Falha ao fugir. O inimigo bloqueou sua saida!");
        }
        return sucesso;
    }

    private static boolean usarPocao(Personagem heroi, Scanner in) {
        Inventario inventario = heroi.getInventario();
        if (inventario.estaVazio()) {
            System.out.println("Seu inventario esta vazio.");
            return false;
        }

        System.out.println("Escolha a pocao para usar:");
        inventario.listarItens();
        System.out.print("Numero do item: ");

        int indiceEscolhido = lerOpcao(in);
        if (indiceEscolhido <= 0) {
            System.out.println("Indice invalido.");
            return false;
        }

        Item item = inventario.usarItem(indiceEscolhido - 1);
        if (item == null) {
            System.out.println("Indice invalido.");
            return false;
        }
        heroi.curar(item.getCura());
        System.out.println("Voce usou " + item.getNome() + " e recuperou " + item.getCura() + " de vida.");
        return true;
    }
}
