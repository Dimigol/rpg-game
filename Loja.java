import java.util.Scanner;

public class Loja {

    public void abrirLoja(Personagem heroi, Scanner in) {

        Item pocaoPequena = new Item("Pocao Pequena", 15, 25);
        Item pocaoMedia = new Item("Pocao Media", 25, 45);
        Item pocaoGrande = new Item("Pocao Grande", 40, 70);
        Item pocaoSuprema = new Item("Pocao Suprema", 85, 130);

        boolean aberta = true;

        while (aberta) {
            System.out.println("\n=== LOJA ===");
            System.out.println("Ouro atual: " + heroi.getOuro());
            System.out.println(heroi.getResumoEquipamentos());
            System.out.println("1 - Comprar Pocao Pequena (+25 HP) - 15 ouro");
            System.out.println("2 - Comprar Pocao Media (+45 HP) - 25 ouro");
            System.out.println("3 - Comprar Pocao Grande (+70 HP) - 40 ouro");
            System.out.println("4 - Comprar Pocao Suprema (+130 HP) - 85 ouro");
            System.out.println("5 - Comprar Espada de Ferro (+8 ATQ) - 60 ouro");
            System.out.println("6 - Comprar Espada Lendaria (+15 ATQ) - 120 ouro");
            System.out.println("7 - Comprar Espada do Apocalipse (+24 ATQ) - 230 ouro");
            System.out.println("8 - Comprar Escudo de Madeira (+5 DEF) - 45 ouro");
            System.out.println("9 - Comprar Escudo de Aco (+10 DEF) - 90 ouro");
            System.out.println("10 - Comprar Escudo Titanico (+18 DEF) - 210 ouro");
            System.out.println("11 - Comprar Armadura de Couro (+6 DEF) - 55 ouro");
            System.out.println("12 - Comprar Armadura Pesada (+12 DEF) - 110 ouro");
            System.out.println("13 - Comprar Armadura Draconica (+20 DEF) - 240 ouro");
            System.out.println("14 - Sair");
            System.out.print("Escolha uma opcao: ");

            if (!in.hasNextInt()) {
                System.out.println("Opcao invalida.");
                in.nextLine();
                continue;
            }

            int opcao = in.nextInt();
            in.nextLine();

            switch (opcao) {
                case 1:
                    comprarPocao(heroi, pocaoPequena);
                    break;
                case 2:
                    comprarPocao(heroi, pocaoMedia);
                    break;
                case 3:
                    comprarPocao(heroi, pocaoGrande);
                    break;
                case 4:
                    comprarPocao(heroi, pocaoSuprema);
                    break;
                case 5:
                    comprarEspada(heroi, "Espada de Ferro", 8, 60);
                    break;
                case 6:
                    comprarEspada(heroi, "Espada Lendaria", 15, 120);
                    break;
                case 7:
                    comprarEspada(heroi, "Espada do Apocalipse", 24, 230);
                    break;
                case 8:
                    comprarEscudo(heroi, "Escudo de Madeira", 5, 45);
                    break;
                case 9:
                    comprarEscudo(heroi, "Escudo de Aco", 10, 90);
                    break;
                case 10:
                    comprarEscudo(heroi, "Escudo Titanico", 18, 210);
                    break;
                case 11:
                    comprarArmadura(heroi, "Armadura de Couro", 6, 55);
                    break;
                case 12:
                    comprarArmadura(heroi, "Armadura Pesada", 12, 110);
                    break;
                case 13:
                    comprarArmadura(heroi, "Armadura Draconica", 20, 240);
                    break;
                case 14:
                    aberta = false;
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void comprarPocao(Personagem heroi, Item pocao) {
        if (heroi.gastarOuro(pocao.getValor())) {
            heroi.getInventario().adicionarItem(pocao);
            System.out.println(pocao.getNome() + " comprada.");
        } else {
            System.out.println("Ouro insuficiente.");
        }
    }

    private void comprarEspada(Personagem heroi, String nome, int bonusAtaque, int preco) {
        if (heroi.gastarOuro(preco)) {
            heroi.equiparEspada(nome, bonusAtaque);
        } else {
            System.out.println("Ouro insuficiente.");
        }
    }

    private void comprarEscudo(Personagem heroi, String nome, int bonusDefesa, int preco) {
        if (heroi.gastarOuro(preco)) {
            heroi.equiparEscudo(nome, bonusDefesa);
        } else {
            System.out.println("Ouro insuficiente.");
        }
    }

    private void comprarArmadura(Personagem heroi, String nome, int bonusDefesa, int preco) {
        if (heroi.gastarOuro(preco)) {
            heroi.equiparArmadura(nome, bonusDefesa);
        } else {
            System.out.println("Ouro insuficiente.");
        }
    }
}
