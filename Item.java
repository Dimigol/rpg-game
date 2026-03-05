public class Item {

    private String nome;
    private int valor;
    private int cura;

    public Item(String nome, int valor, int cura) {
        this.nome = nome;
        this.valor = valor;
        this.cura = cura;
    }

    public String getNome() { return nome; }
    public int getValor() { return valor; }
    public int getCura() { return cura; }
}