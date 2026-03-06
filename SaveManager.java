import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

public class SaveManager {
    private static final String SAVE_PATH = "save.txt";
    private static final int MAX_NIVEL = 100;
    private static final int MAX_OURO = 1_000_000;
    private static final int MAX_VIDA = 10_000;
    private static final int MAX_ATAQUE_BASE = 500;
    private static final int MAX_BONUS_EQUIP = 200;
    private static final int MAX_BUFF_TURNOS = 3;
    private static final int MAX_ITENS = 200;

    private static final String[] EQUIP_PADRAO = {"Nenhuma", "Nenhum", "Nenhuma"};

    public static void salvar(Personagem heroi) {
        StringBuilder payload = new StringBuilder();
        adicionarLinha(payload, "Nome", heroi.getNome());
        adicionarLinha(payload, "Vida", heroi.getVida());
        adicionarLinha(payload, "VidaMaxima", heroi.getVidaMaxima());
        adicionarLinha(payload, "Ouro", heroi.getOuro());
        adicionarLinha(payload, "Nivel", heroi.getNivel());
        adicionarLinha(payload, "XP", heroi.getXp());
        adicionarLinha(payload, "XpProximoNivel", heroi.getXpProximoNivel());
        adicionarLinha(payload, "AtaqueBase", heroi.getAtaqueBase());
        adicionarLinha(payload, "BonusArma", heroi.getBonusArma());
        adicionarLinha(payload, "BonusEscudo", heroi.getBonusEscudo());
        adicionarLinha(payload, "BonusArmadura", heroi.getBonusArmadura());
        adicionarLinha(payload, "ArmaEquipada", heroi.getArmaEquipada());
        adicionarLinha(payload, "EscudoEquipado", heroi.getEscudoEquipado());
        adicionarLinha(payload, "ArmaduraEquipada", heroi.getArmaduraEquipada());
        adicionarLinha(payload, "BuffTurnos", heroi.getBuffTurnos());

        ArrayList<Item> itens = heroi.getInventario().getItens();
        adicionarLinha(payload, "Itens", itens.size());
        for (Item item : itens) {
            payload.append("Item:")
                .append(item.getNome()).append("|")
                .append(item.getValor()).append("|")
                .append(item.getCura()).append("\n");
        }

        long checksum = calcularChecksum(payload.toString());

        try (FileWriter writer = new FileWriter(SAVE_PATH)) {
            writer.write(payload.toString());
            writer.write("Checksum:" + checksum + "\n");
            System.out.println("Jogo salvo!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar.");
        }
    }

    public static Personagem carregar() {
        Map<String, String> campos = new HashMap<>();
        ArrayList<Item> itens = new ArrayList<>();
        StringBuilder payloadLido = new StringBuilder();
        String checksumLido = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_PATH))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Checksum:")) {
                    checksumLido = linha.substring("Checksum:".length()).trim();
                    continue;
                }

                payloadLido.append(linha).append("\n");

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

        if (!checksumValido(checksumLido, payloadLido.toString())) {
            System.out.println("Save invalido ou alterado manualmente. Nao foi carregado.");
            return null;
        }

        String nome = sanitizarTexto(campos.getOrDefault("Nome", "Heroi"), "Heroi");
        Personagem heroi = new Personagem(nome);

        int nivel = clamp(parseInt(campos.get("Nivel"), heroi.getNivel()), 1, MAX_NIVEL);
        int vidaMaxima = clamp(parseInt(campos.get("VidaMaxima"), heroi.getVidaMaxima()), 1, MAX_VIDA);
        int xpProximoNivel = clamp(parseInt(campos.get("XpProximoNivel"), heroi.getXpProximoNivel()), 1, 100_000);
        int xp = clamp(parseInt(campos.get("XP"), heroi.getXp()), 0, Math.max(0, xpProximoNivel - 1));

        heroi.aplicarEstadoSalvo(
            clamp(parseInt(campos.get("Vida"), heroi.getVida()), 0, vidaMaxima),
            vidaMaxima,
            clamp(parseInt(campos.get("Ouro"), heroi.getOuro()), 0, MAX_OURO),
            nivel,
            xp,
            xpProximoNivel,
            clamp(parseInt(campos.get("AtaqueBase"), heroi.getAtaqueBase()), 1, MAX_ATAQUE_BASE),
            clamp(parseInt(campos.get("BonusArma"), heroi.getBonusArma()), 0, MAX_BONUS_EQUIP),
            clamp(parseInt(campos.get("BonusEscudo"), heroi.getBonusEscudo()), 0, MAX_BONUS_EQUIP),
            clamp(parseInt(campos.get("BonusArmadura"), heroi.getBonusArmadura()), 0, MAX_BONUS_EQUIP),
            sanitizarTexto(campos.getOrDefault("ArmaEquipada", heroi.getArmaEquipada()), EQUIP_PADRAO[0]),
            sanitizarTexto(campos.getOrDefault("EscudoEquipado", heroi.getEscudoEquipado()), EQUIP_PADRAO[1]),
            sanitizarTexto(campos.getOrDefault("ArmaduraEquipada", heroi.getArmaduraEquipada()), EQUIP_PADRAO[2]),
            clamp(parseInt(campos.get("BuffTurnos"), heroi.getBuffTurnos()), 0, MAX_BUFF_TURNOS)
        );

        heroi.getInventario().limpar();
        for (int i = 0; i < itens.size() && i < MAX_ITENS; i++) {
            Item item = itens.get(i);
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
            String nome = sanitizarTexto(partes[0], "");
            if (nome.isEmpty()) {
                return null;
            }
            int valor = clamp(Integer.parseInt(partes[1]), 1, 100_000);
            int cura = clamp(Integer.parseInt(partes[2]), 1, 100_000);
            return new Item(nome, valor, cura);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void adicionarLinha(StringBuilder payload, String chave, Object valor) {
        payload.append(chave).append(":").append(valor).append("\n");
    }

    private static long calcularChecksum(String texto) {
        CRC32 crc = new CRC32();
        crc.update(texto.getBytes());
        return crc.getValue();
    }

    private static boolean checksumValido(String checksumLido, String payload) {
        if (checksumLido == null || checksumLido.isBlank()) {
            return false;
        }

        try {
            long esperado = Long.parseLong(checksumLido);
            long atual = calcularChecksum(payload);
            return esperado == atual;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int clamp(int valor, int min, int max) {
        return Math.max(min, Math.min(max, valor));
    }

    private static String sanitizarTexto(String texto, String padrao) {
        if (texto == null) {
            return padrao;
        }

        String limpo = texto.replace('\n', ' ').replace('\r', ' ').trim();
        if (limpo.isEmpty() || limpo.contains("|") || limpo.length() > 60) {
            return padrao;
        }

        return limpo;
    }
}
