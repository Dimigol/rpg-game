
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

public class JogoGrafico extends JFrame {
    private static final String TELA_MENU = "menu";
    private static final String TELA_HUB = "hub";
    private static final String TELA_BATALHA = "batalha";
    private static final String TELA_LOJA = "loja";
    private static final String TELA_INVENTARIO = "inventario";

    private final java.awt.CardLayout cardLayout = new java.awt.CardLayout();
    private final JPanel root = new JPanel(cardLayout);
    private final Random random = new Random();

    private final Image bgMenu = carregarImagemOuFallback("assets/backgrounds/menu.png", 1600, 900, new Color(17, 33, 48));
    private final Image bgHub = carregarImagemOuFallback("assets/backgrounds/hub.png", 1600, 900, new Color(14, 28, 40));
    private final Image bgBattle = carregarImagemOuFallback("assets/backgrounds/battle.png", 1600, 900, new Color(30, 24, 36));
    private final Image bgShop = carregarImagemOuFallback("assets/backgrounds/shop.png", 1600, 900, new Color(22, 36, 30));
    private final Image bgInventory = carregarImagemOuFallback("assets/backgrounds/inventory.png", 1600, 900, new Color(22, 22, 29));

    private final Image heroSprite = carregarImagemOuFallback("assets/sprites/hero.png", 220, 220, new Color(60, 140, 255));
    private final Image enemyDefaultSprite = carregarImagemOuFallback("assets/sprites/enemy_default.png", 220, 220, new Color(221, 73, 88));
    private final Image goblinSprite = carregarImagemOuFallback("assets/sprites/goblin.png", 220, 220, new Color(76, 171, 80));
    private final Image skeletonSprite = carregarImagemOuFallback("assets/sprites/skeleton.png", 220, 220, new Color(200, 210, 220));
    private final Image orcSprite = carregarImagemOuFallback("assets/sprites/orc.png", 220, 220, new Color(94, 136, 66));
    private final Image bossSprite = carregarImagemOuFallback("assets/sprites/boss.png", 220, 220, new Color(197, 71, 41));
    private final Image xandaoSprite = carregarImagemOuFallback("assets/sprites/xandao.png", 220, 220, new Color(166, 95, 218));
    private final Image taxadSprite = carregarImagemOuFallback("assets/sprites/taxad.png", 220, 220, new Color(208, 146, 48));

    private Personagem heroi;
    private Monstro inimigoAtual;
    private String nomeInimigoAtual;
    private int recompensaOuroAtual;
    private int recompensaXpAtual;
    private int hpInimigoMaximo;

    private JTextField campoNome;
    private JLabel menuFeedback;

    private JLabel hubHeader;
    private JTextArea hubLog;

    private JLabel battleHeroLabel;
    private JLabel battleEnemyLabel;
    private JProgressBar battleHeroHpBar;
    private JProgressBar battleEnemyHpBar;
    private JTextArea battleLog;
    private JPanel battlePotionPanel;
    private JPanel battlePotionList;
    private SpritePanel heroSpritePanel;
    private SpritePanel enemySpritePanel;
    private FloatingTextLayer floatingLayer;
    private JButton btnAtacar;
    private JButton btnPocao;
    private JButton btnSolar;
    private JButton btnFugir;

    private JLabel shopHeader;
    private JTextArea shopLog;

    private JLabel invHeader;
    private JPanel invListPanel;

    public JogoGrafico() {
        aplicarTema();
        setTitle("Aventura RPG - Modo Grafico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);

        root.add(criarTelaMenu(), TELA_MENU);
        root.add(criarTelaHub(), TELA_HUB);
        root.add(criarTelaBatalha(), TELA_BATALHA);
        root.add(criarTelaLoja(), TELA_LOJA);
        root.add(criarTelaInventario(), TELA_INVENTARIO);
        setContentPane(root);
        cardLayout.show(root, TELA_MENU);
    }

    private void aplicarTema() {
        UIManager.put("Panel.background", new Color(18, 24, 34));
        UIManager.put("Button.background", new Color(35, 110, 210));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Label.foreground", new Color(235, 242, 249));
        UIManager.put("TextArea.background", new Color(8, 13, 20));
        UIManager.put("TextArea.foreground", new Color(206, 219, 233));
        UIManager.put("ProgressBar.foreground", new Color(214, 77, 77));
    }

    private JPanel criarTelaMenu() {
        ImagePanel panel = new ImagePanel(bgMenu);
        panel.setLayout(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        JLabel title = new JLabel("AVENTURA RPG", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 46));
        panel.add(title, BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setOpaque(true);
        card.setBackground(new Color(0, 0, 0, 140));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblNome = new JLabel("Nome do heroi:");
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        campoNome = new JTextField("Heroi", 14);
        campoNome.setMaximumSize(new Dimension(260, 32));
        campoNome.setHorizontalAlignment(JTextField.CENTER);
        campoNome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnNovoJogo = new JButton("Novo Jogo");
        JButton btnCarregar = new JButton("Carregar Save");

        btnNovoJogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCarregar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnNovoJogo.addActionListener(e -> iniciarNovoJogo());
        btnCarregar.addActionListener(e -> carregarJogo());

        menuFeedback = new JLabel(" ");
        menuFeedback.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuFeedback.setForeground(new Color(255, 210, 120));

        card.add(lblNome);
        card.add(Box.createVerticalStrut(8));
        card.add(campoNome);
        card.add(Box.createVerticalStrut(12));
        card.add(btnNovoJogo);
        card.add(Box.createVerticalStrut(8));
        card.add(btnCarregar);
        card.add(Box.createVerticalStrut(10));
        card.add(menuFeedback);

        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarTelaHub() {
        ImagePanel panel = new ImagePanel(bgHub);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        hubHeader = new JLabel("Heroi");
        hubHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(hubHeader, BorderLayout.NORTH);

        hubLog = criarLogArea();
        panel.add(new JScrollPane(hubLog), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(2, 4, 8, 8));
        buttons.setOpaque(false);
        JButton bExplorar = new JButton("Explorar");
        JButton bLoja = new JButton("Loja");
        JButton bInventario = new JButton("Inventario");
        JButton bStatus = new JButton("Status");
        JButton bSolar = new JButton("Acao Solar");
        JButton bSalvar = new JButton("Salvar");
        JButton bMenu = new JButton("Menu");
        JButton bSair = new JButton("Sair");

        bExplorar.addActionListener(e -> explorar());
        bLoja.addActionListener(e -> abrirLoja());
        bInventario.addActionListener(e -> abrirInventarioHub());
        bStatus.addActionListener(e -> appendHubLog(montarStatusTexto()));
        bSolar.addActionListener(e -> {
            heroi.exposicaoSolar();
            appendHubLog("Acao solar ativada.");
            atualizarHubHeader();
        });
        bSalvar.addActionListener(e -> {
            SaveManager.salvar(heroi);
            appendHubLog("Save salvo.");
        });
        bMenu.addActionListener(e -> cardLayout.show(root, TELA_MENU));
        bSair.addActionListener(e -> dispose());

        buttons.add(bExplorar);
        buttons.add(bLoja);
        buttons.add(bInventario);
        buttons.add(bStatus);
        buttons.add(bSolar);
        buttons.add(bSalvar);
        buttons.add(bMenu);
        buttons.add(bSair);
        panel.add(buttons, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel criarTelaBatalha() {
        ImagePanel panel = new ImagePanel(bgBattle);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new GridLayout(2, 2, 8, 4));
        top.setOpaque(false);
        battleHeroLabel = new JLabel("Heroi");
        battleEnemyLabel = new JLabel("Inimigo", SwingConstants.RIGHT);
        battleHeroHpBar = criarBarraHp();
        battleEnemyHpBar = criarBarraHp();
        top.add(battleHeroLabel);
        top.add(battleEnemyLabel);
        top.add(battleHeroHpBar);
        top.add(battleEnemyHpBar);
        panel.add(top, BorderLayout.NORTH);

        JLayeredPane scene = new JLayeredPane();
        scene.setPreferredSize(new Dimension(900, 300));
        JPanel spriteContainer = new JPanel(null);
        spriteContainer.setOpaque(false);
        heroSpritePanel = new SpritePanel(heroSprite);
        enemySpritePanel = new SpritePanel(enemyDefaultSprite);
        heroSpritePanel.setBounds(120, 70, 220, 220);
        enemySpritePanel.setBounds(560, 70, 220, 220);
        spriteContainer.add(heroSpritePanel);
        spriteContainer.add(enemySpritePanel);

        floatingLayer = new FloatingTextLayer();
        floatingLayer.setBounds(0, 0, 900, 300);
        spriteContainer.setBounds(0, 0, 900, 300);
        scene.add(spriteContainer, Integer.valueOf(1));
        scene.add(floatingLayer, Integer.valueOf(2));

        panel.add(scene, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setOpaque(false);
        battleLog = criarLogArea();
        battleLog.setRows(7);
        bottom.add(new JScrollPane(battleLog), BorderLayout.CENTER);

        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actionBar.setOpaque(false);
        btnAtacar = new JButton("Atacar");
        btnPocao = new JButton("Pocoes");
        btnSolar = new JButton("Solar");
        btnFugir = new JButton("Fugir");
        JButton btnVoltar = new JButton("Voltar Hub");

        btnAtacar.addActionListener(e -> executarTurnoJogador(1));
        btnPocao.addActionListener(e -> togglePainelPocoes());
        btnSolar.addActionListener(e -> executarTurnoJogador(3));
        btnFugir.addActionListener(e -> executarTurnoJogador(4));
        btnVoltar.addActionListener(e -> {
            setBotoesCombate(false);
            cardLayout.show(root, TELA_HUB);
        });

        actionBar.add(btnAtacar);
        actionBar.add(btnPocao);
        actionBar.add(btnSolar);
        actionBar.add(btnFugir);
        actionBar.add(btnVoltar);

        battlePotionPanel = new JPanel(new BorderLayout(6, 6));
        battlePotionPanel.setOpaque(true);
        battlePotionPanel.setBackground(new Color(0, 0, 0, 140));
        battlePotionPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        battlePotionPanel.add(new JLabel("Clique para usar uma pocao:"), BorderLayout.NORTH);
        battlePotionList = new JPanel(new GridLayout(0, 1, 4, 4));
        battlePotionList.setOpaque(false);
        battlePotionPanel.add(battlePotionList, BorderLayout.CENTER);
        battlePotionPanel.setVisible(false);

        bottom.add(actionBar, BorderLayout.NORTH);
        bottom.add(battlePotionPanel, BorderLayout.EAST);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel criarTelaLoja() {
        ImagePanel panel = new ImagePanel(bgShop);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);
        shopHeader = new JLabel("Loja");
        shopHeader.setFont(new Font("SansSerif", Font.BOLD, 17));
        JButton back = new JButton("Voltar");
        back.addActionListener(e -> {
            atualizarHubHeader();
            cardLayout.show(root, TELA_HUB);
        });
        top.add(shopHeader, BorderLayout.CENTER);
        top.add(back, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(5, 3, 8, 8));
        grid.setOpaque(false);
        addShopButton(grid, "Pocao Pequena", "+25 HP", 15, () -> comprarPocao(new Item("Pocao Pequena", 15, 25)));
        addShopButton(grid, "Pocao Media", "+45 HP", 25, () -> comprarPocao(new Item("Pocao Media", 25, 45)));
        addShopButton(grid, "Pocao Grande", "+70 HP", 40, () -> comprarPocao(new Item("Pocao Grande", 40, 70)));
        addShopButton(grid, "Pocao Suprema", "+130 HP", 85, () -> comprarPocao(new Item("Pocao Suprema", 85, 130)));
        addShopButton(grid, "Espada de Ferro", "+8 ATQ", 60, () -> comprarEspada("Espada de Ferro", 8, 60));
        addShopButton(grid, "Espada Lendaria", "+15 ATQ", 120, () -> comprarEspada("Espada Lendaria", 15, 120));
        addShopButton(grid, "Espada do Apocalipse", "+24 ATQ", 230, () -> comprarEspada("Espada do Apocalipse", 24, 230));
        addShopButton(grid, "Escudo de Madeira", "+5 DEF", 45, () -> comprarEscudo("Escudo de Madeira", 5, 45));
        addShopButton(grid, "Escudo de Aco", "+10 DEF", 90, () -> comprarEscudo("Escudo de Aco", 10, 90));
        addShopButton(grid, "Escudo Titanico", "+18 DEF", 210, () -> comprarEscudo("Escudo Titanico", 18, 210));
        addShopButton(grid, "Armadura de Couro", "+6 DEF", 55, () -> comprarArmadura("Armadura de Couro", 6, 55));
        addShopButton(grid, "Armadura Pesada", "+12 DEF", 110, () -> comprarArmadura("Armadura Pesada", 12, 110));
        addShopButton(grid, "Armadura Draconica", "+20 DEF", 240, () -> comprarArmadura("Armadura Draconica", 20, 240));
        panel.add(new JScrollPane(grid), BorderLayout.CENTER);

        shopLog = criarLogArea();
        shopLog.setRows(5);
        panel.add(new JScrollPane(shopLog), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel criarTelaInventario() {
        ImagePanel panel = new ImagePanel(bgInventory);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.setOpaque(false);
        invHeader = new JLabel("Inventario");
        invHeader.setFont(new Font("SansSerif", Font.BOLD, 17));
        JButton back = new JButton("Voltar");
        back.addActionListener(e -> {
            atualizarHubHeader();
            cardLayout.show(root, TELA_HUB);
        });
        top.add(invHeader, BorderLayout.CENTER);
        top.add(back, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        invListPanel = new JPanel(new GridLayout(0, 1, 6, 6));
        invListPanel.setOpaque(false);
        panel.add(new JScrollPane(invListPanel), BorderLayout.CENTER);
        return panel;
    }

    private void iniciarNovoJogo() {
        String nome = campoNome.getText() == null ? "" : campoNome.getText().trim();
        if (nome.isEmpty()) {
            menuFeedback.setText("Digite um nome valido.");
            return;
        }

        ConfiguracaoJogo.setLinguagemNeutra(true);
        heroi = new Personagem(nome);
        menuFeedback.setText(" ");
        hubLog.setText("Novo heroi criado: " + heroi.getNome() + "\n");
        atualizarHubHeader();
        cardLayout.show(root, TELA_HUB);
    }

    private void carregarJogo() {
        Personagem carregado = SaveManager.carregar();
        if (carregado == null) {
            menuFeedback.setText("Falha ao carregar save.");
            return;
        }

        heroi = carregado;
        menuFeedback.setText("Save carregado.");
        hubLog.setText("Save carregado com sucesso.\n");
        atualizarHubHeader();
        cardLayout.show(root, TELA_HUB);
    }

    private void explorar() {
        if (heroi == null || !heroi.estaVivo()) {
            return;
        }

        int evento = random.nextInt(100);
        if (evento < 60) {
            iniciarBatalha(gerarInimigoComum());
            return;
        }
        if (evento < 90) {
            int ouro = random.nextInt(41) + 10;
            heroi.adicionarOuro(ouro);
            appendHubLog("Voce encontrou um bau e ganhou " + ouro + " de ouro.");
            atualizarHubHeader();
            return;
        }
        iniciarBatalha(gerarBoss());
    }

    private void abrirLoja() {
        atualizarShopHeader();
        shopLog.setText("");
        cardLayout.show(root, TELA_LOJA);
    }

    private void abrirInventarioHub() {
        atualizarInventarioPanel();
        cardLayout.show(root, TELA_INVENTARIO);
    }

    private void iniciarBatalha(Encontro e) {
        inimigoAtual = e.inimigo;
        inimigoAtual.aplicarEscala(heroi.getNivel());
        nomeInimigoAtual = e.nome;
        recompensaOuroAtual = e.ouro;
        recompensaXpAtual = e.xp;
        hpInimigoMaximo = inimigoAtual.getVida();

        enemySpritePanel.setSprite(spriteParaInimigo(nomeInimigoAtual));
        heroSpritePanel.setSprite(heroSprite);
        floatingLayer.clearTexts();
        battleLog.setText("Um " + nomeInimigoAtual + " apareceu!\n");
        battlePotionPanel.setVisible(false);
        atualizarHUDCombate();
        setBotoesCombate(true);
        cardLayout.show(root, TELA_BATALHA);
    }

    private Encontro gerarInimigoComum() {
        int tipo = random.nextInt(4);
        if (tipo == 0) return new Encontro(new Monstro(), "Monstro Selvagem", 18, 25);
        if (tipo == 1) return new Encontro(new Goblin(), "Goblin", 15, 20);
        if (tipo == 2) return new Encontro(new Esqueleto(), "Esqueleto", 22, 30);
        return new Encontro(new Orc(), "Orc", 28, 40);
    }

    private Encontro gerarBoss() {
        int tipo = random.nextInt(3);
        if (tipo == 0) return new Encontro(new Boss(), "Lula", 70, 90);
        if (tipo == 1) return new Encontro(new XandaoBoss(), "Xandao \"o cabeca de ovo\"", 85, 110);
        return new Encontro(new TaxadBoss(), "Taxad", 100, 130);
    }

    private void executarTurnoJogador(int acao) {
        if (heroi == null || inimigoAtual == null || !heroi.estaVivo() || !inimigoAtual.estaVivo()) {
            return;
        }

        if (acao == 1) {
            int dano = heroi.atacar();
            inimigoAtual.receberDano(dano);
            appendBatalhaLog("Voce atacou e causou " + dano + " de dano em " + nomeInimigoAtual + ".");
            enemySpritePanel.flashHit();
            floatingLayer.spawn("-" + dano, 690, 130, new Color(255, 120, 120));
        } else if (acao == 3) {
            heroi.exposicaoSolar();
            appendBatalhaLog("Acao solar ativada.");
            floatingLayer.spawn("+ATQ", 260, 130, new Color(255, 214, 96));
        } else if (acao == 4) {
            boolean fugiu = random.nextInt(100) < 70;
            if (fugiu) {
                appendBatalhaLog("Voce conseguiu fugir.");
                finalizarBatalha(false, true);
                return;
            }
            appendBatalhaLog("Falha ao fugir.");
        }

        if (!inimigoAtual.estaVivo()) {
            heroi.adicionarOuro(recompensaOuroAtual);
            heroi.adicionarXp(recompensaXpAtual);
            appendBatalhaLog("Vitoria! + " + recompensaOuroAtual + " ouro.");
            floatingLayer.spawn("+XP " + recompensaXpAtual, 260, 100, new Color(110, 255, 170));
            finalizarBatalha(true, false);
            return;
        }

        int danoInimigo = inimigoAtual.atacar();
        int danoFinal = heroi.receberDano(danoInimigo);
        int bloqueado = danoInimigo - danoFinal;
        appendBatalhaLog(nomeInimigoAtual + " atacou e causou " + danoFinal + " de dano.");
        if (bloqueado > 0) {
            appendBatalhaLog("Sua defesa bloqueou " + bloqueado + " de dano.");
        }
        heroSpritePanel.flashHit();
        floatingLayer.spawn("-" + danoFinal, 260, 130, new Color(255, 120, 120));
        heroi.atualizarBuff();
        atualizarHUDCombate();

        if (!heroi.estaVivo()) {
            appendBatalhaLog("GAME OVER");
            setBotoesCombate(false);
            finalizarBatalha(false, false);
        }
    }

    private void togglePainelPocoes() {
        if (battlePotionPanel.isVisible()) {
            battlePotionPanel.setVisible(false);
            return;
        }
        atualizarPainelPocoesBatalha();
        battlePotionPanel.setVisible(true);
    }

    private void atualizarPainelPocoesBatalha() {
        battlePotionList.removeAll();
        List<Item> itens = heroi.getInventario().getItens();
        if (itens.isEmpty()) {
            JLabel vazio = new JLabel("Inventario vazio.");
            battlePotionList.add(vazio);
        } else {
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.get(i);
                int idx = i;
                JButton botao = new JButton(item.getNome() + " (+" + item.getCura() + " HP)");
                botao.addActionListener((ActionEvent e) -> usarPocaoBatalha(idx));
                battlePotionList.add(botao);
            }
        }
        battlePotionList.revalidate();
        battlePotionList.repaint();
    }

    private void usarPocaoBatalha(int index) {
        Item item = heroi.getInventario().usarItem(index);
        if (item == null) {
            appendBatalhaLog("Pocao invalida.");
            return;
        }

        heroi.curar(item.getCura());
        appendBatalhaLog("Voce usou " + item.getNome() + " e recuperou " + item.getCura() + " de vida.");
        floatingLayer.spawn("+" + item.getCura(), 260, 110, new Color(120, 255, 150));
        battlePotionPanel.setVisible(false);
        atualizarHUDCombate();

        int danoInimigo = inimigoAtual.atacar();
        int danoFinal = heroi.receberDano(danoInimigo);
        appendBatalhaLog(nomeInimigoAtual + " atacou e causou " + danoFinal + " de dano.");
        heroSpritePanel.flashHit();
        floatingLayer.spawn("-" + danoFinal, 260, 130, new Color(255, 120, 120));
        heroi.atualizarBuff();
        atualizarHUDCombate();

        if (!heroi.estaVivo()) {
            appendBatalhaLog("GAME OVER");
            setBotoesCombate(false);
            finalizarBatalha(false, false);
        }
    }

    private void finalizarBatalha(boolean venceu, boolean fugiu) {
        setBotoesCombate(false);
        atualizarHubHeader();
        if (venceu) appendHubLog("Voce venceu " + nomeInimigoAtual + " e ganhou recompensas.");
        else if (fugiu) appendHubLog("Voce fugiu da batalha.");
        else if (!heroi.estaVivo()) appendHubLog("GAME OVER.");
        cardLayout.show(root, TELA_HUB);
    }

    private void setBotoesCombate(boolean ativo) {
        btnAtacar.setEnabled(ativo);
        btnPocao.setEnabled(ativo);
        btnSolar.setEnabled(ativo);
        btnFugir.setEnabled(ativo);
    }

    private void atualizarHubHeader() {
        if (heroi == null) return;
        hubHeader.setText(
            heroi.getNome()
                + " | HP " + heroi.getVida() + "/" + heroi.getVidaMaxima()
                + " | Ouro " + heroi.getOuro()
                + " | Nv " + heroi.getNivel()
                + " | ATQ " + heroi.getAtaqueTotal()
                + " | DEF " + heroi.getDefesaTotal()
        );
    }

    private void atualizarHUDCombate() {
        battleHeroLabel.setText(heroi.getNome() + " (Nv " + heroi.getNivel() + ")");
        battleEnemyLabel.setText(nomeInimigoAtual + " ");
        battleHeroHpBar.setMaximum(heroi.getVidaMaxima());
        battleHeroHpBar.setValue(heroi.getVida());
        battleHeroHpBar.setString("HP " + heroi.getVida() + "/" + heroi.getVidaMaxima());

        battleEnemyHpBar.setMaximum(Math.max(1, hpInimigoMaximo));
        battleEnemyHpBar.setValue(Math.max(0, inimigoAtual.getVida()));
        battleEnemyHpBar.setString("HP " + inimigoAtual.getVida() + "/" + hpInimigoMaximo);
    }

    private void atualizarShopHeader() {
        if (heroi == null) return;
        shopHeader.setText("Loja | Ouro: " + heroi.getOuro() + " | " + heroi.getResumoEquipamentos());
    }

    private void atualizarInventarioPanel() {
        invListPanel.removeAll();
        invHeader.setText("Inventario | HP " + heroi.getVida() + "/" + heroi.getVidaMaxima());
        List<Item> itens = heroi.getInventario().getItens();
        if (itens.isEmpty()) {
            invListPanel.add(new JLabel("Inventario vazio."));
        } else {
            for (int i = 0; i < itens.size(); i++) {
                Item item = itens.get(i);
                int idx = i;
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.setOpaque(false);
                JLabel lbl = new JLabel(item.getNome() + " (cura " + item.getCura() + ")");
                JButton usar = new JButton("Usar");
                usar.addActionListener(e -> {
                    Item usado = heroi.getInventario().usarItem(idx);
                    if (usado == null) return;
                    heroi.curar(usado.getCura());
                    appendHubLog("Voce usou " + usado.getNome() + " e recuperou " + usado.getCura() + " de vida.");
                    atualizarHubHeader();
                    atualizarInventarioPanel();
                });
                row.add(lbl);
                row.add(usar);
                invListPanel.add(row);
            }
        }
        invListPanel.revalidate();
        invListPanel.repaint();
    }

    private void addShopButton(JPanel container, String nome, String bonus, int preco, Runnable action) {
        JButton btn = new JButton("<html><b>" + nome + "</b><br/>" + bonus + " | " + preco + " ouro</html>");
        btn.addActionListener(e -> {
            action.run();
            atualizarShopHeader();
            atualizarHubHeader();
        });
        container.add(btn);
    }

    private void comprarPocao(Item item) {
        if (heroi.gastarOuro(item.getValor())) {
            heroi.getInventario().adicionarItem(item);
            appendShopLog(item.getNome() + " comprada.");
        } else {
            appendShopLog("Ouro insuficiente.");
        }
    }

    private void comprarEspada(String nome, int bonus, int preco) {
        if (heroi.gastarOuro(preco)) {
            heroi.equiparEspada(nome, bonus);
            appendShopLog("Equipada: " + nome);
        } else {
            appendShopLog("Ouro insuficiente.");
        }
    }

    private void comprarEscudo(String nome, int bonus, int preco) {
        if (heroi.gastarOuro(preco)) {
            heroi.equiparEscudo(nome, bonus);
            appendShopLog("Equipado: " + nome);
        } else {
            appendShopLog("Ouro insuficiente.");
        }
    }

    private void comprarArmadura(String nome, int bonus, int preco) {
        if (heroi.gastarOuro(preco)) {
            heroi.equiparArmadura(nome, bonus);
            appendShopLog("Equipada: " + nome);
        } else {
            appendShopLog("Ouro insuficiente.");
        }
    }

    private String montarStatusTexto() {
        return "Status:\n"
            + "Nivel " + heroi.getNivel()
            + " | XP " + heroi.getXp() + "/" + heroi.getXpProximoNivel()
            + " | HP " + heroi.getVida() + "/" + heroi.getVidaMaxima()
            + " | ATQ " + heroi.getAtaqueTotal()
            + " | DEF " + heroi.getDefesaTotal()
            + " | Ouro " + heroi.getOuro() + "\n"
            + heroi.getResumoEquipamentos() + "\n";
    }

    private void appendHubLog(String msg) {
        hubLog.append(msg + "\n");
        hubLog.setCaretPosition(hubLog.getDocument().getLength());
    }

    private void appendBatalhaLog(String msg) {
        battleLog.append(msg + "\n");
        battleLog.setCaretPosition(battleLog.getDocument().getLength());
        atualizarHUDCombate();
    }

    private void appendShopLog(String msg) {
        shopLog.append(msg + "\n");
        shopLog.setCaretPosition(shopLog.getDocument().getLength());
    }

    private JTextArea criarLogArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return area;
    }

    private JProgressBar criarBarraHp() {
        JProgressBar bar = new JProgressBar();
        bar.setStringPainted(true);
        bar.setForeground(new Color(214, 77, 77));
        bar.setBackground(new Color(54, 64, 80));
        return bar;
    }

    private Image spriteParaInimigo(String nome) {
        if (nome.equals("Goblin")) return goblinSprite;
        if (nome.equals("Esqueleto")) return skeletonSprite;
        if (nome.equals("Orc")) return orcSprite;
        if (nome.equals("Lula")) return bossSprite;
        if (nome.startsWith("Xandao")) return xandaoSprite;
        if (nome.equals("Taxad")) return taxadSprite;
        return enemyDefaultSprite;
    }

    private Image carregarImagemOuFallback(String path, int w, int h, Color color) {
        try {
            File f = new File(path);
            if (f.exists()) {
                BufferedImage img = ImageIO.read(f);
                return img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            }
        } catch (IOException ignored) {
        }

        BufferedImage fallback = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = fallback.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillRoundRect(0, 0, w, h, 28, 28);
        g.setColor(new Color(255, 255, 255, 70));
        g.drawRoundRect(10, 10, w - 20, h - 20, 24, 24);
        g.dispose();
        return fallback;
    }

    private static class Encontro {
        private final Monstro inimigo;
        private final String nome;
        private final int ouro;
        private final int xp;

        private Encontro(Monstro inimigo, String nome, int ouro, int xp) {
            this.inimigo = inimigo;
            this.nome = nome;
            this.ouro = ouro;
            this.xp = xp;
        }
    }

    private static class ImagePanel extends JPanel {
        private final Image background;

        private ImagePanel(Image background) {
            this.background = background;
            setOpaque(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
            g.setColor(new Color(0, 0, 0, 64));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private static class SpritePanel extends JPanel {
        private Image sprite;
        private float flash = 0f;

        private SpritePanel(Image sprite) {
            this.sprite = sprite;
            setOpaque(false);
        }

        private void setSprite(Image sprite) {
            this.sprite = sprite;
            repaint();
        }

        private void flashHit() {
            flash = 1f;
            Timer timer = new Timer(30, null);
            timer.addActionListener(e -> {
                flash -= 0.2f;
                if (flash <= 0f) {
                    flash = 0f;
                    timer.stop();
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            if (sprite != null) {
                g2.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2.setColor(new Color(90, 90, 90));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            if (flash > 0f) {
                g2.setColor(new Color(255, 70, 70, Math.min(255, (int) (flash * 180))));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.dispose();
        }
    }

    private static class FloatingTextLayer extends JComponent {
        private final List<FloatingText> texts = new ArrayList<>();
        private final Timer timer;

        private FloatingTextLayer() {
            setOpaque(false);
            timer = new Timer(33, e -> {
                for (int i = texts.size() - 1; i >= 0; i--) {
                    FloatingText t = texts.get(i);
                    t.y -= t.vy;
                    t.life--;
                    if (t.life <= 0) texts.remove(i);
                }
                repaint();
            });
            timer.start();
        }

        private void spawn(String text, int x, int y, Color color) {
            texts.add(new FloatingText(text, x, y, color));
            repaint();
        }

        private void clearTexts() {
            texts.clear();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(new Font("SansSerif", Font.BOLD, 22));
            for (FloatingText t : texts) {
                int alpha = Math.max(0, Math.min(255, t.life * 4));
                g2.setColor(new Color(t.color.getRed(), t.color.getGreen(), t.color.getBlue(), alpha));
                g2.drawString(t.text, t.x, (int) t.y);
            }
            g2.dispose();
        }
    }

    private static class FloatingText {
        private final String text;
        private final int x;
        private float y;
        private final Color color;
        private int life = 60;
        private final float vy = 1.2f;

        private FloatingText(String text, int x, int y, Color color) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }
}
