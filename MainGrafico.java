import javax.swing.SwingUtilities;

public class MainGrafico {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JogoGrafico().setVisible(true));
    }
}
