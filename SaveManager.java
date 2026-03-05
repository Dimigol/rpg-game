import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveManager {

    public static void salvar(Personagem heroi) {
        try {
            FileWriter writer = new FileWriter("save.txt");

            writer.write("Nome:" + heroi.getNome() + "\n");
            writer.write("Vida:" + heroi.getVida() + "\n");
            writer.write("VidaMaxima:" + heroi.getVidaMaxima() + "\n");
            writer.write("Ouro:" + heroi.getOuro() + "\n");
            writer.write("Nivel:" + heroi.getNivel() + "\n");
            writer.write("XP:" + heroi.getXp() + "\n");
            writer.write("XpProximoNivel:" + heroi.getXpProximoNivel() + "\n");
            writer.write("AtaqueBase:" + heroi.getAtaqueBase() + "\n");
            writer.write("BonusArma:" + heroi.getBonusArma() + "\n");
            writer.write("BonusEscudo:" + heroi.getBonusEscudo() + "\n");
            writer.write("BonusArmadura:" + heroi.getBonusArmadura() + "\n");
            writer.write("ArmaEquipada:" + heroi.getArmaEquipada() + "\n");
            writer.write("EscudoEquipado:" + heroi.getEscudoEquipado() + "\n");
            writer.write("ArmaduraEquipada:" + heroi.getArmaduraEquipada() + "\n");
            writer.write("BuffTurnos:" + heroi.getBuffTurnos() + "\n");

            ArrayList<Item> itens = heroi.getInventario().getItens();
            writer.write("Itens:" + itens.size() + "\n");
            for (Item item : itens) {
                writer.write("Item:" + item.getNome() + "|" + item.getValor() + "|" + item.getCura() + "\n");
            }

            writer.close();
            System.out.println("Jogo salvo!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }

    public static Personagem carregar() {
        Map<String, String> campos = new HashMap<>();
        ArrayList<Item> itens = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("save.txt"))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Item:")) {
                    Item item = parseItem(linha.substring(5));
                    if (item != null) {
                        itens.add(item);
                    }
                    continue;
                }

                int separador = linha.indexOf(':');
                if (separador <= 0) {
                    continue;
                }

                String chave = linha.substring(0, separador);
                String valor = linha.substring(separador + 1);
                campos.put(chave, valor);
            }
        } catch (IOException e) {
            System.out.println("Nao foi possivel carregar o save.");
            return null;
        }

        String nome = campos.getOrDefault("Nome", "Heroi");
        Personagem heroi = new Personagem(nome);

        heroi.aplicarEstadoSalvo(
            parseInt(campos.get("Vida"), heroi.getVida()),
            parseInt(campos.get("VidaMaxima"), heroi.getVidaMaxima()),
            parseInt(campos.get("Ouro"), heroi.getOuro()),
            parseInt(campos.get("Nivel"), heroi.getNivel()),
            parseInt(campos.get("XP"), heroi.getXp()),
            parseInt(campos.get("XpProximoNivel"), heroi.getXpProximoNivel()),
            parseInt(campos.get("AtaqueBase"), heroi.getAtaqueBase()),
            parseInt(campos.get("BonusArma"), heroi.getBonusArma()),
            parseInt(campos.get("BonusEscudo"), heroi.getBonusEscudo()),
            parseInt(campos.get("BonusArmadura"), heroi.getBonusArmadura()),
            campos.getOrDefault("ArmaEquipada", heroi.getArmaEquipada()),
            campos.getOrDefault("EscudoEquipado", heroi.getEscudoEquipado()),
            campos.getOrDefault("ArmaduraEquipada", heroi.getArmaduraEquipada()),
            parseInt(campos.get("BuffTurnos"), heroi.getBuffTurnos())
        );

        heroi.getInventario().limpar();
        for (Item item : itens) {
            heroi.getInventario().adicionarItem(item);
        }

        System.out.println("Jogo carregado com sucesso!");
        return heroi;
    }

    private static int parseInt(String valor, int padrao) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return padrao;
        }
    }

    private static Item parseItem(String dados) {
        String[] partes = dados.split("\\|");
        if (partes.length != 3) {
            return null;
        }

        try {
            String nome = partes[0];
            int valor = Integer.parseInt(partes[1]);
            int cura = Integer.parseInt(partes[2]);
            return new Item(nome, valor, cura);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
