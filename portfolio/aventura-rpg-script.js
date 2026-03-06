const SAVE_KEY = "aventura_rpg_web_save_v1";
const logEl = document.getElementById("log");
const statusLine = document.getElementById("statusLine");
const shopPanel = document.getElementById("shopPanel");
const invPanel = document.getElementById("invPanel");
const shopList = document.getElementById("shopList");
const invList = document.getElementById("invList");
const battleCard = document.getElementById("battleCard");
const hubActions = document.getElementById("hubActions");
const battleActions = document.getElementById("battleActions");
const enemyName = document.getElementById("enemyName");
const enemyStats = document.getElementById("enemyStats");
const enemyHpFill = document.getElementById("enemyHpFill");

const state = {
  hero: null,
  inventory: [],
  battle: null
};

const shopItems = [
  { type: "potion", name: "Pocao Pequena", cost: 15, heal: 25 },
  { type: "potion", name: "Pocao Media", cost: 25, heal: 45 },
  { type: "potion", name: "Pocao Grande", cost: 40, heal: 70 },
  { type: "potion", name: "Pocao Suprema", cost: 85, heal: 130 },
  { type: "weapon", name: "Espada de Ferro", cost: 60, atk: 8 },
  { type: "weapon", name: "Espada Lendaria", cost: 120, atk: 15 },
  { type: "weapon", name: "Espada do Apocalipse", cost: 230, atk: 24 },
  { type: "shield", name: "Escudo de Madeira", cost: 45, def: 5 },
  { type: "shield", name: "Escudo de Aco", cost: 90, def: 10 },
  { type: "shield", name: "Escudo Titanico", cost: 210, def: 18 },
  { type: "armor", name: "Armadura de Couro", cost: 55, def: 6 },
  { type: "armor", name: "Armadura Pesada", cost: 110, def: 12 },
  { type: "armor", name: "Armadura Draconica", cost: 240, def: 20 }
];

function rnd(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}

function writeLog(msg) {
  logEl.textContent += (logEl.textContent ? "\n" : "") + msg;
  logEl.scrollTop = logEl.scrollHeight;
}

function setPanels({ shop = false, inv = false } = {}) {
  shopPanel.classList.toggle("active", shop);
  invPanel.classList.toggle("active", inv);
}

function updateStatus() {
  const h = state.hero;
  if (!h) return;
  statusLine.textContent =
    `${h.name} | Nv ${h.level} | XP ${h.xp}/${h.xpNext} | HP ${h.hp}/${h.maxHp} | Ouro ${h.gold} | ATQ ${heroAtk()} | DEF ${heroDef()} | Arma: ${h.weaponName} | Escudo: ${h.shieldName} | Armadura: ${h.armorName}${h.solarTurns > 0 ? " | Solar " + h.solarTurns + "t" : ""}`;
}

function heroAtk() {
  const h = state.hero;
  return h.baseAtk + h.weaponAtk + (h.solarTurns > 0 ? 10 : 0);
}

function heroDef() {
  const h = state.hero;
  return h.shieldDef + h.armorDef;
}

function newGame() {
  const name = prompt("Nome do heroi:", "Heroi");
  if (!name) return;
  state.hero = {
    name: name.trim() || "Heroi",
    hp: 120,
    maxHp: 120,
    gold: 50,
    level: 1,
    xp: 0,
    xpNext: 100,
    baseAtk: 20,
    weaponAtk: 0,
    shieldDef: 0,
    armorDef: 0,
    weaponName: "Nenhuma",
    shieldName: "Nenhum",
    armorName: "Nenhuma",
    solarTurns: 0
  };
  state.inventory = [];
  state.battle = null;
  logEl.textContent = "";
  writeLog(`Novo jogo iniciado com ${state.hero.name}.`);
  refreshUI();
}

function saveGame() {
  if (!state.hero) return;
  localStorage.setItem(SAVE_KEY, JSON.stringify(state));
  writeLog("Jogo salvo no navegador.");
}

function loadGame() {
  const raw = localStorage.getItem(SAVE_KEY);
  if (!raw) {
    writeLog("Nenhum save encontrado.");
    return;
  }
  try {
    const data = JSON.parse(raw);
    if (!data.hero) throw new Error("save invalido");
    state.hero = data.hero;
    state.inventory = Array.isArray(data.inventory) ? data.inventory : [];
    state.battle = data.battle || null;
    writeLog("Jogo carregado.");
  } catch {
    writeLog("Save invalido.");
  }
  refreshUI();
}

function gainXp(amount) {
  const h = state.hero;
  h.xp += amount;
  while (h.xp >= h.xpNext) {
    h.xp -= h.xpNext;
    h.level += 1;
    h.maxHp += 10;
    h.baseAtk += 2;
    h.hp = h.maxHp;
    h.xpNext += 55;
    writeLog(`LEVEL UP! Agora no nivel ${h.level}.`);
  }
}

function startBattle(enemy) {
  state.battle = enemy;
  setPanels();
  writeLog(`Um ${enemy.name} apareceu!`);
  refreshUI();
}

function scaleNormalEnemy(enemy, heroLevel) {
  const initialLevels = Math.min(Math.max(0, heroLevel - 1), 2);
  const advancedLevels = Math.max(0, heroLevel - 3);
  enemy.hp += (initialLevels * 6) + (advancedLevels * 16);
  enemy.atk += (initialLevels * 1) + (advancedLevels * 3);
  enemy.maxHp = enemy.hp;
}

function scaleBossEnemy(enemy, heroLevel) {
  const initialLevels = Math.min(Math.max(0, heroLevel - 1), 2);
  const advancedLevels = Math.max(0, heroLevel - 3);

  if (enemy.name === "Lula") {
    enemy.hp += (initialLevels * 10) + (advancedLevels * 28);
    enemy.atk += (initialLevels * 1) + (advancedLevels * 4);
  } else if (enemy.name.startsWith("Xandao")) {
    enemy.hp += (initialLevels * 12) + (advancedLevels * 30);
    enemy.atk += (initialLevels * 1) + (advancedLevels * 4);
  } else {
    enemy.hp += (initialLevels * 14) + (advancedLevels * 34);
    enemy.atk += (initialLevels * 2) + (advancedLevels * 5);
  }

  enemy.maxHp = enemy.hp;
}

function explore() {
  if (!state.hero || state.battle) return;
  const roll = rnd(1, 100);
  if (roll <= 60) {
    const list = [
      { name: "Goblin", hp: 65, atk: 17, gold: 15, xp: 20 },
      { name: "Esqueleto", hp: 90, atk: 20, gold: 22, xp: 30 },
      { name: "Orc", hp: 120, atk: 24, gold: 28, xp: 40 },
      { name: "Monstro Selvagem", hp: 80, atk: 15, gold: 18, xp: 25 }
    ];
    const e = { ...list[rnd(0, list.length - 1)] };
    scaleNormalEnemy(e, state.hero.level);
    startBattle(e);
    return;
  }

  if (roll <= 90) {
    const gold = rnd(10, 50);
    state.hero.gold += gold;
    writeLog(`Voce encontrou um bau e ganhou ${gold} ouro.`);
    refreshUI();
    return;
  }

  const bosses = [
    { name: "Lula", hp: 200, atk: 30, gold: 70, xp: 90 },
    { name: "Xandao \"o cabeca de ovo\"", hp: 230, atk: 32, gold: 85, xp: 110 },
    { name: "Taxad", hp: 260, atk: 35, gold: 100, xp: 130 }
  ];
  const b = { ...bosses[rnd(0, bosses.length - 1)] };
  scaleBossEnemy(b, state.hero.level);
  startBattle(b);
}

function enemyTurn() {
  const b = state.battle;
  if (!b) return;
  const raw = b.atk + rnd(0, 9);
  const dmg = Math.max(1, raw - heroDef());
  const blocked = raw - dmg;
  state.hero.hp = Math.max(0, state.hero.hp - dmg);
  writeLog(`${b.name} atacou e causou ${dmg} de dano.`);
  if (blocked > 0) {
    writeLog(`Sua defesa bloqueou ${blocked} de dano.`);
  }
  if (state.hero.solarTurns > 0) {
    state.hero.solarTurns -= 1;
    if (state.hero.solarTurns === 0) writeLog("A energia solar se dissipou.");
  }
  if (state.hero.hp <= 0) {
    writeLog("GAME OVER. Inicie um novo jogo.");
    state.battle = null;
  }
}

function battleAttack() {
  if (!state.battle) return;
  const dmg = heroAtk() + rnd(0, 9);
  state.battle.hp = Math.max(0, state.battle.hp - dmg);
  writeLog(`Voce atacou e causou ${dmg} de dano em ${state.battle.name}.`);
  if (state.battle.hp <= 0) {
    writeLog(`Voce venceu ${state.battle.name}! +${state.battle.gold} ouro, +${state.battle.xp} XP.`);
    state.hero.gold += state.battle.gold;
    gainXp(state.battle.xp);
    state.battle = null;
    refreshUI();
    return;
  }
  enemyTurn();
  refreshUI();
}

function battleFlee() {
  if (!state.battle) return;
  if (rnd(1, 100) <= 70) {
    writeLog("Voce fugiu da batalha.");
    state.battle = null;
  } else {
    writeLog("Falha ao fugir.");
    enemyTurn();
  }
  refreshUI();
}

function useSolar() {
  if (!state.hero || state.battle) return;
  if (state.hero.solarTurns > 0) {
    writeLog(`Solar ja ativo por ${state.hero.solarTurns} turnos.`);
  } else {
    state.hero.solarTurns = 3;
    writeLog("Voce ativou acao solar. +10 ATQ por 3 turnos em combate.");
  }
  refreshUI();
}

function openShop() {
  if (!state.hero || state.battle) return;
  setPanels({ shop: true });
  renderShop();
}

function renderShop() {
  shopList.innerHTML = "";
  shopItems.forEach(item => {
    const line = document.createElement("div");
    line.className = "line";
    const left = document.createElement("div");
    left.innerHTML = `<strong>${item.name}</strong><div class="muted">${item.heal ? "+" + item.heal + " HP" : item.atk ? "+" + item.atk + " ATQ" : "+" + item.def + " DEF"} | ${item.cost} ouro</div>`;
    const btn = document.createElement("button");
    btn.textContent = "Comprar";
    btn.onclick = () => buyItem(item);
    line.append(left, btn);
    shopList.appendChild(line);
  });
}

function buyItem(item) {
  const h = state.hero;
  if (h.gold < item.cost) {
    writeLog("Ouro insuficiente.");
    return;
  }
  h.gold -= item.cost;
  if (item.type === "potion") {
    state.inventory.push({ name: item.name, heal: item.heal });
    writeLog(`${item.name} comprada.`);
  } else if (item.type === "weapon") {
    h.weaponName = item.name;
    h.weaponAtk = item.atk;
    writeLog(`Arma equipada: ${item.name}.`);
  } else if (item.type === "shield") {
    h.shieldName = item.name;
    h.shieldDef = item.def;
    writeLog(`Escudo equipado: ${item.name}.`);
  } else if (item.type === "armor") {
    h.armorName = item.name;
    h.armorDef = item.def;
    writeLog(`Armadura equipada: ${item.name}.`);
  }
  refreshUI();
  renderShop();
}

function openInventory() {
  if (!state.hero || state.battle) return;
  setPanels({ inv: true });
  renderInventory(false);
}

function renderInventory(inBattle) {
  invList.innerHTML = "";
  if (!state.inventory.length) {
    const empty = document.createElement("div");
    empty.className = "line";
    empty.textContent = "Inventario vazio.";
    invList.appendChild(empty);
    return;
  }
  state.inventory.forEach((item, idx) => {
    const line = document.createElement("div");
    line.className = "line";
    const left = document.createElement("div");
    left.innerHTML = `<strong>${item.name}</strong><div class="muted">Recupera ${item.heal} HP</div>`;
    const btn = document.createElement("button");
    btn.textContent = "Usar";
    btn.onclick = () => usePotion(idx, inBattle);
    line.append(left, btn);
    invList.appendChild(line);
  });
}

function usePotion(index, inBattle) {
  const item = state.inventory[index];
  if (!item) return;
  state.inventory.splice(index, 1);
  state.hero.hp = Math.min(state.hero.maxHp, state.hero.hp + item.heal);
  writeLog(`Voce usou ${item.name} e recuperou ${item.heal} HP.`);
  refreshUI();
  renderInventory(inBattle);
  if (inBattle && state.battle) {
    enemyTurn();
    refreshUI();
  }
}

function battlePotion() {
  if (!state.battle) return;
  setPanels({ inv: true });
  renderInventory(true);
}

function battleSolar() {
  if (!state.battle) return;
  if (state.hero.solarTurns > 0) {
    writeLog(`Solar ja ativo por ${state.hero.solarTurns} turnos.`);
  } else {
    state.hero.solarTurns = 3;
    writeLog("Voce ativou acao solar. +10 ATQ por 3 turnos.");
  }
  enemyTurn();
  refreshUI();
}

function refreshUI() {
  const inBattle = Boolean(state.battle);
  hubActions.style.display = inBattle ? "none" : "flex";
  battleActions.style.display = inBattle ? "flex" : "none";
  battleCard.classList.toggle("active", inBattle);
  updateStatus();

  if (inBattle) {
    const b = state.battle;
    enemyName.textContent = b.name;
    enemyStats.textContent = `ATQ ${b.atk} | Recompensa ${b.gold} ouro, ${b.xp} XP`;
    const pct = Math.max(0, Math.min(100, (b.hp / b.maxHp) * 100));
    enemyHpFill.style.width = pct + "%";
  }
}

document.getElementById("btnNew").onclick = newGame;
document.getElementById("btnLoad").onclick = loadGame;
document.getElementById("btnSave").onclick = saveGame;
document.getElementById("btnExplore").onclick = explore;
document.getElementById("btnSolar").onclick = useSolar;
document.getElementById("btnShop").onclick = openShop;
document.getElementById("btnInv").onclick = openInventory;
document.getElementById("btnAttack").onclick = battleAttack;
document.getElementById("btnBattlePotion").onclick = battlePotion;
document.getElementById("btnBattleSolar").onclick = battleSolar;
document.getElementById("btnFlee").onclick = battleFlee;
document.getElementById("btnCloseShop").onclick = () => setPanels();
document.getElementById("btnCloseInv").onclick = () => setPanels();

newGame();