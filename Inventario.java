import java.util.ArrayList;

public class Inventario {

    private ArrayList<Item> itens = new ArrayList<>();

    public void adicionarItem(Item item) {
        itens.add(item);
    }

    public void listarItens() {
        if (itens.isEmpty()) {
            System.out.println("Inventario vazio.");
            return;
        }

        for (int i = 0; i < itens.size(); i++) {
            System.out.println((i + 1) + " - " + itens.get(i).getNome());
        }
    }

    public Item usarItem(int index) {
        if (index >= 0 && index < itens.size()) {
            return itens.remove(index);
        }
        return null;
    }

    public boolean estaVazio() {
        return itens.isEmpty();
    }

    public int quantidadeItens() {
        return itens.size();
    }

    public ArrayList<Item> getItens() {
        return new ArrayList<>(itens);
    }

    public void limpar() {
        itens.clear();
    }
}
