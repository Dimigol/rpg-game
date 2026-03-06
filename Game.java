import java.util.Scanner;

public class Game {

    private Scanner in = new Scanner(System.in);
    private Personagem heroi;
    private Mapa mapa;

    public void start() {

        System.out.println("=== AVENTURA RPG ===");
        configurarTomDeTexto();
        escolherInicioJogo();

        mapa = new Mapa();

        loopPrincipal();
    }

    private void escolherInicioJogo() {
        System.out.println("1 - Novo jogo");
        System.out.println("2 - Carregar jogo");
        System.out.print("Escolha uma opcao: ");

        if (!in.hasNextInt()) {
            in.nextLine();
            criarNovoHeroi();
            return;
        }

        int opcao = in.nextInt();
        in.nextLine();

        if (opcao == 2) {
            Personagem carregado = SaveManager.carregar();
            if (carregado != null) {
                heroi = carregado;
                return;
            }

            System.out.println("Iniciando novo jogo.");
        }

        criarNovoHeroi();
    }

    private void criarNovoHeroi() {
        System.out.print("Digite o nome do heroi: ");
        heroi = new Personagem(in.nextLine());
    }

    private void loopPrincipal() {

        while (heroi.estaVivo()) {

            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1 - Explorar");
            System.out.println("2 - Loja");
            System.out.println("3 - Salvar jogo");
            System.out.println("4 - " + ConfiguracaoJogo.getRotuloAcaoSolar());
            System.out.println("5 - Inventario");
            System.out.println("6 - Status do heroi");
            System.out.println("7 - Sair");
            System.out.print("Escolha uma opcao: ");

            if (!in.hasNextInt()) {
                System.out.println("Opcao invalida. Digite um numero de 1 a 7.");
                in.nextLine();
                continue;
            }

            int opcao = in.nextInt();
            in.nextLine();

            switch (opcao) {
                case 1:
                    mapa.explorarSala(heroi, in);
                    break;
                case 2:
                    new Loja().abrirLoja(heroi, in);
                    break;
                case 3:
                    SaveManager.salvar(heroi);
                    break;
                case 4:
                    heroi.exposicaoSolar();
                    break; 
                case 5:
                    abrirInventario();
                    break;
                case 6:
                    mostrarStatusHeroi();
                    break;
                case 7:
                    System.out.println("Saindo do jogo...");
                    return;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }

        System.out.println("GAME OVER");
    }

    private void configurarTomDeTexto() {
        System.out.println("Tom de texto:");
        System.out.println("1 - Neutro");
        System.out.println("2 - Humor");
        System.out.print("Escolha uma opcao: ");

        if (!in.hasNextInt()) {
            in.nextLine();
            ConfiguracaoJogo.setLinguagemNeutra(true);
            return;
        }

        int opcao = in.nextInt();
        in.nextLine();
        ConfiguracaoJogo.setLinguagemNeutra(opcao != 2);
    }

    private void abrirInventario() {
        Inventario inventario = heroi.getInventario();

        System.out.println("\n=== INVENTARIO ===");
        inventario.listarItens();

        if (inventario.estaVazio()) {
            return;
        }

        System.out.println("0 - Voltar");
        System.out.print("Digite o numero do item para usar: ");

        if (!in.hasNextInt()) {
            System.out.println("Entrada invalida.");
            in.nextLine();
            return;
        }

        int escolha = in.nextInt();
        in.nextLine();

        if (escolha == 0) {
            return;
        }

        Item item = inventario.usarItem(escolha - 1);
        if (item == null) {
            System.out.println("Item invalido.");
            return;
        }

        heroi.curar(item.getCura());
        System.out.println("Voce usou " + item.getNome() + " e recuperou " + item.getCura() + " de vida.");
    }

    private void mostrarStatusHeroi() {
        System.out.println("\n=== STATUS DO HEROI ===");
        System.out.println("Nome: " + heroi.getNome());
        System.out.println("Nivel: " + heroi.getNivel());
        System.out.println("XP: " + heroi.getXp() + "/" + heroi.getXpProximoNivel());
        System.out.println("Vida: " + heroi.getVida() + "/" + heroi.getVidaMaxima());
        System.out.println("Ataque total: " + heroi.getAtaqueTotal());
        System.out.println("Defesa total: " + heroi.getDefesaTotal());
        System.out.println("Ouro: " + heroi.getOuro());
        System.out.println("Itens no inventario: " + heroi.getInventario().quantidadeItens());
        System.out.println(heroi.getResumoEquipamentos());

        if (heroi.getBuffTurnos() > 0) {
            System.out.println("Buff solar ativo por " + heroi.getBuffTurnos() + " turnos.");
        } else {
            System.out.println("Buff solar inativo.");
        }
    }
}
