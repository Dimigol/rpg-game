public class ConfiguracaoJogo {

    private static boolean linguagemNeutra = true;

    public static void setLinguagemNeutra(boolean neutra) {
        linguagemNeutra = neutra;
    }

    public static String getRotuloAcaoSolar() {
        return linguagemNeutra ? "Exposicao solar" : "Colocar os testiculos no sol";
    }

    public static String getMensagemSolarJaAtiva(int turnos) {
        return linguagemNeutra
            ? "A exposicao solar ja esta ativa por " + turnos + " turnos."
            : "seus testiculos ja estao expostos por " + turnos + " turnos.";
    }

    public static String getMensagemSolarAtivada() {
        return linguagemNeutra
            ? "Voce fez exposicao solar."
            : "Voce colocou os testiculos no sol.";
    }
}
