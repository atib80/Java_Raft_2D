/**
 * @author Kovács Attila
 * @version 1.0 *
 */

import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.*;


public class Fohos extends JatekElem {

    private static final Random rd = new Random();

    private Raft raft;
    private Capa capa;
    private Landzsa landzsa;
    private boolean isKarakterAlive;
	private boolean isKarakterEatenByTheShark;
    private int ehsegSzint;
    private int szomjusagSzint;
    private boolean isFohosLepett;
    private boolean isFohosAktivkodott;
    private Direction elsoLepesIrany;
    private Direction masodikLepesIrany;

    private ArrayList<Position> szabadSzomszedosMezok;
    private ArrayList<Position> talaltNyersanyagok;
    private HashMap<String, ArrayList<JatekElem>> inventory;


    public Fohos(int x, int y, Raft r, Capa c, Landzsa l) {

        this(x, y, r, c, l, 100, 100);

    }

    public Fohos(int x, int y, Raft r, Capa c, Landzsa l, int ehsegSzint, int szomjusagSzint) {

		super(x, y, 'X', Raft.fohosImg);
        this.raft = r;
        this.capa = c;
        this.landzsa = l;
        this.isKarakterAlive = true;
		this.isKarakterEatenByTheShark = false;
        this.ehsegSzint = ehsegSzint;
        this.szomjusagSzint = szomjusagSzint;
        this.setIsFohosLepett(false);


        szabadSzomszedosMezok = new ArrayList<>();
        talaltNyersanyagok = new ArrayList<>();
        this.inventory = new HashMap<String, ArrayList<JatekElem>>();
        this.inventory.put("burgonya", new ArrayList<JatekElem>());
        this.inventory.put("deszka", new ArrayList<JatekElem>());
        this.inventory.put("etel", new ArrayList<JatekElem>());
        this.inventory.put("hal", new ArrayList<JatekElem>());
        this.inventory.put("halo", new ArrayList<JatekElem>());
        this.inventory.put("hulladek", new ArrayList<JatekElem>());
        this.inventory.put("landzsa", new ArrayList<JatekElem>());
        this.inventory.put("level", new ArrayList<JatekElem>());
        this.inventory.put("tutaj", new ArrayList<JatekElem>());
        this.inventory.put("tuzhely", new ArrayList<JatekElem>());
        this.inventory.put("viztisztito", new ArrayList<JatekElem>());

    }


    public boolean getIsFohosLepett() {
        return this.isFohosLepett;
    }

    public void setIsFohosLepett(boolean isFohosLepett) {

        if (!isFohosLepett) {

            this.elsoLepesIrany = Direction.NONE;
            this.masodikLepesIrany = Direction.NONE;

        }

        this.isFohosLepett = isFohosLepett;


        if (!(raft.getIsRealTimeModeOn()) && isFohosLepett) {

            raft.handleNonRealTimeMode();
        }

    }


    public void setIsFohosCarriedOutAction(boolean isFohosAktivkodott) {

        if (isFohosAktivkodott) {


            if (!(raft.getIsRealTimeModeOn())) {

                raft.handleNonRealTimeMode();
            }

		}

        this.setIsFohosAktivkodott(isFohosAktivkodott);

    }

    public void csokkenEhseg(int d) {

        ehsegSzint += d;

        if (ehsegSzint > 100) ehsegSzint = 100;
    }

    public void novelEhseg(int d) {

        ehsegSzint -= d;

        if (ehsegSzint <= 0) {

            ehsegSzint = 0;

            this.isKarakterAlive = false;			

        }

    }

    public void csokkenSzomjusag(int d) {

        szomjusagSzint += d;

        if (szomjusagSzint > 100) szomjusagSzint = 100;
    }

    public void novelSzomjusag(int d) {

        szomjusagSzint -= d;

        if (szomjusagSzint <= 0) {

            szomjusagSzint = 0;

            this.isKarakterAlive = false;			
        }
    }

    public Direction getElsoLepesIrany() {
        return this.elsoLepesIrany;
    }

    public void setElsoLepesIrany(Direction elsoLepesIrany) {
        this.elsoLepesIrany = elsoLepesIrany;
    }

    public Direction getMasodikLepesIrany() {
        return this.masodikLepesIrany;
    }

    public void setMasodikLepesIrany(Direction masodikLepesIrany) {
        this.masodikLepesIrany = masodikLepesIrany;
    }

    public boolean getIsKarakterAlive() {
        return isKarakterAlive;
    }

    public void setIsKarakterAlive(boolean isKarakterAlive) {

        this.isKarakterAlive = isKarakterAlive;

    }
	
	public boolean getIsKarakterEatenByTheShark() {
		
		return this.isKarakterEatenByTheShark;
		
	}
	
	public void setIsKarakterEatenByTheShark(boolean isFohosEatenByTheShark) {
		
		this.isKarakterEatenByTheShark = isFohosEatenByTheShark;
		
	}

    public int getEhsegSzint() {
        return ehsegSzint;
    }


    public int getSzomjusagSzint() {
        return szomjusagSzint;
    }


    public HashMap<String, ArrayList<JatekElem>> getInventory() {
        return inventory;
    }


    public boolean addJatekElemToInventory(String jatekElemNeve, JatekElem ujEpitettElem, int amount, int xPos, int yPos) {

        if (jatekElemNeve.equals("") || (amount < 1)) return false;

        switch (jatekElemNeve) {

            case "burgonya":

                for (int i = 0; i < amount; i++) this.inventory.get("burgonya").add(new Burgonya(-1, -1));

                return true;


            case "deszka":

                for (int i = 0; i < amount; i++) this.inventory.get("deszka").add(new Deszka(-1, -1));

                return true;

            case "hal":

                for (int i = 0; i < amount; i++) this.inventory.get("hal").add(new Hal(-1, -1));

                return true;

            case "halo":
                if (ujEpitettElem != null) this.inventory.get("halo").add(ujEpitettElem);

                return true;

            case "hulladek":

                for (int i = 0; i < amount; i++) this.inventory.get("hulladek").add(new Hulladek(-1, -1));

                return true;

            case "landzsa":

                for (int i = 0; i < amount; i++) this.inventory.get("landzsa").add(new Landzsa(-1, -1));

                return true;

            case "level":

                for (int i = 0; i < amount; i++) this.inventory.get("level").add(new Level(-1, -1));

                return true;

            case "tutaj":

                if (ujEpitettElem != null) this.inventory.get("tutaj").add(ujEpitettElem);

                return true;

            case "tuzhely":

                if (ujEpitettElem != null) this.inventory.get("tuzhely").add(ujEpitettElem);

                return true;

            case "viztisztito":

                if (ujEpitettElem != null) this.inventory.get("viztisztito").add(ujEpitettElem);

                return true;

            default:
                return false;

        }

    }

    public boolean removeJatekElemFromInventory(String jatekElemNeve, int amount) {

        if (jatekElemNeve.equals("") || (amount < 1)) return false;

        switch (jatekElemNeve) {

            case "burgonya":

                for (int i = 0; i < amount; i++)
                    this.inventory.get("burgonya").remove(this.inventory.get("burgonya").size() - 1);

                return true;


            case "deszka":

                for (int i = 0; i < amount; i++)
                    this.inventory.get("deszka").remove(this.inventory.get("deszka").size() - 1);

                return true;

            case "hal":

                for (int i = 0; i < amount; i++) this.inventory.get("hal").remove(this.inventory.get("hal").size() - 1);

                return true;

            case "halo":

                this.inventory.get("halo").remove(this.inventory.get("halo").size() - 1);
				
                return true;

            case "hulladek":

                for (int i = 0; i < amount; i++)
                    this.inventory.get("hulladek").remove(this.inventory.get("hulladek").size() - 1);

                return true;

            case "landzsa":

                for (int i = 0; i < amount; i++)
                    this.inventory.get("landzsa").remove(this.inventory.get("landzsa").size() - 1);

                return true;

            case "level":

                for (int i = 0; i < amount; i++)
                    this.inventory.get("level").remove(this.inventory.get("level").size() - 1);

                return true;

            case "tutaj":

                this.inventory.get("tutaj").remove(this.inventory.get("tutaj").size() - 1);

                return true;

            case "tuzhely":

                this.inventory.get("tuzhely").remove(this.inventory.get("tuzhely").size() - 1);

                return true;

            case "viztisztito":

                this.inventory.get("viztisztito").remove(this.inventory.get("viztisztito").size() - 1);

                return true;

            default:
                return false;

        }

    }


    public int getInventoryJatekElemCount(String itemName) {

        // if (itemName.equals("")) return -1;

        switch (itemName) {

            case "burgonya":
                return this.inventory.get("burgonya").size();

            case "deszka":
                return this.inventory.get("deszka").size();

            case "hal":
                return this.inventory.get("hal").size();

            case "halo":
                return this.inventory.get("halo").size();

            case "hulladek":
                return this.inventory.get("hulladek").size();

            case "landzsa":
                return this.inventory.get("landzsa").size();

            case "level":
                return this.inventory.get("level").size();

            case "tutaj":
                return this.inventory.get("tutaj").size();

            case "tuzhely":
                return this.inventory.get("tuzhely").size();

            case "viztisztito":
                return this.inventory.get("viztisztito").size();

            default:
                return -1;

        }
    }

    public void lep(char irany) {

        switch (irany) {

            case 'w':
            case 'W':


                if (this.getElsoLepesIrany() != Direction.N) {

                    if (this.moveFohos(-1, 0)) {

                        if (this.getElsoLepesIrany() == Direction.NONE) this.setElsoLepesIrany(Direction.N);

                        else if (this.getMasodikLepesIrany() == Direction.NONE) {

                            this.setMasodikLepesIrany(Direction.N);
                            this.setIsFohosLepett(true);
                        }

                    }
                }

                break;

            case 's':
            case 'S':


                if (this.getElsoLepesIrany() != Direction.S) {

                    if (this.moveFohos(1, 0)) {

                        if (this.getElsoLepesIrany() == Direction.NONE) this.setElsoLepesIrany(Direction.S);

                        else if (this.getMasodikLepesIrany() == Direction.NONE) {

                            this.setMasodikLepesIrany(Direction.S);
                            this.setIsFohosLepett(true);
                        }

                    }
                }

                break;

            case 'a':
            case 'A':


                if (this.getElsoLepesIrany() != Direction.W) {

                    if (this.moveFohos(0, -1)) {

                        if (this.getElsoLepesIrany() == Direction.NONE) this.setElsoLepesIrany(Direction.W);

                        else if (this.getMasodikLepesIrany() == Direction.NONE) {

                            this.setMasodikLepesIrany(Direction.W);

                            this.setIsFohosLepett(true);

                        }

                    }
                }

                break;

            case 'd':
            case 'D':


                if (this.getElsoLepesIrany() != Direction.E) {

                    if (this.moveFohos(0, 1)) {

                        if (this.getElsoLepesIrany() == Direction.NONE) this.setElsoLepesIrany(Direction.E);

                        else if (this.getMasodikLepesIrany() == Direction.NONE) {

                            this.setMasodikLepesIrany(Direction.E);

                            this.setIsFohosLepett(true);

                        }

                    }
                }

            default:
                break;

        }

    }

    private boolean moveFohos(int dx, int dy) {

        if (this.getIsFohosLepett()) return false;

        if (dx < -1 || dx > 1) return false;

        if (dy < -1 || dy > 1) return false;

        if ((dx == 1) && (this.position.x == (this.raft.getMaxPalyaX() - 1))) return false;

        if ((dx == -1) && (this.position.x == 0)) return false;

        if ((dy == 1) && (this.position.y == (this.raft.getMaxPalyaY() - 1))) return false;

        if ((dy == -1) && (this.position.y == 0)) return false;

        szabadSzomszedosMezok.clear();

        int minX, maxX, minY, maxY;

        if (this.position.x == 0) minX = 0;
        else minX = this.position.x - 1;

        if (this.position.x == (raft.getMaxPalyaX() - 1)) maxX = raft.getMaxPalyaX() - 1;
        else maxX = this.position.x + 1;

        if (this.position.y == 0) minY = 0;
        else minY = this.position.y - 1;

        if (this.position.y == (raft.getMaxPalyaY() - 1)) maxY = raft.getMaxPalyaY() - 1;
        else maxY = this.position.y + 1;

        for (int i = minX; i <= maxX; i++) {

            for (int j = minY; j <= maxY; j++) {

                if ((this.position.x == i) && (this.position.y == j)) continue;

                if (!(raft.palyaPrev[i][j] instanceof Viz)) continue;

                szabadSzomszedosMezok.add(new Position(i, j));

            }

        }

        Position newPos = new Position(this.position.x + dx, this.position.y + dy);

        boolean fohosLephet = false;

        for (Position p : szabadSzomszedosMezok) {

            if ((p.x == newPos.x) && (p.y == newPos.y)) {

                fohosLephet = true;

                break;
            }

        }

        if (!fohosLephet) return false;

        this.raft.palyaFohos[this.position.x][this.position.y] = null;

        this.position.x = newPos.x;

        this.position.y = newPos.y;

        this.raft.palyaFohos[newPos.x][newPos.y] = this;

        return true;

    }

    public boolean foz() {

        if (!(raft.palyaEpitmenyek[this.position.x][this.position.y] instanceof Tuzhely)) return false;

        if ((this.getInventoryJatekElemCount("burgonya") < 1) || (this.getInventoryJatekElemCount("hal") < 1))
            return false;

        if (((Tuzhely) raft.palyaEpitmenyek[this.position.x][this.position.y]).getIsEtelFozesFolyamatElinditva())
            return false;

        System.out.println("Foz A.");

        ((Tuzhely) raft.palyaEpitmenyek[this.position.x][this.position.y]).setIsEtelFozesFolyamatElinditva(true);

        System.out.println("Foz B.");

        this.inventory.get("burgonya").clear();
        this.inventory.get("hal").clear();

        return true;
    }

    public boolean eszik() {

        if (!(raft.palyaEpitmenyek[this.position.x][this.position.y] instanceof Tuzhely)) return false;

        System.out.println("A. public boolean eszik()");

        return (((Tuzhely) raft.palyaEpitmenyek[this.position.x][this.position.y]).consumeOneMeal());
    }


    public boolean iszik() {

        if (!(raft.palyaEpitmenyek[this.position.x][this.position.y] instanceof Viztisztito)) return false;

        return (((Viztisztito) raft.palyaEpitmenyek[this.position.x][this.position.y]).consumeOneDrink());

    }


    public boolean epit(int targyId) {

        int randomInd;

        if (raft.palyaTutaj[this.position.x][this.position.y] == null) return false;

        switch (targyId) {

            case 1:

                if (this.getSzabadSzomszedosPoziciok(false)) return false;

                if (getInventoryJatekElemCount("deszka") < 2) return false;
                if (getInventoryJatekElemCount("level") < 2) return false;

                this.removeJatekElemFromInventory("deszka", 2);
                this.removeJatekElemFromInventory("level", 2);

                randomInd = rd.nextInt(szabadSzomszedosMezok.size());

                if ((this.position.x != 0) && (raft.palyaTutaj[this.position.x - 1][this.position.y] == null) && (raft.palyaPrev[this.position.x - 1][this.position.y] instanceof Viz)) {

                    raft.palyaTutaj[this.position.x - 1][this.position.y] = new Tutaj(this.position.x - 1, this.position.y);
                    this.addJatekElemToInventory("tutaj", raft.palyaTutaj[this.position.x - 1][this.position.y], 1, this.position.x - 1, this.position.y);
                    return true;


                } else if ((this.position.y != 0) && (raft.palyaTutaj[this.position.x][this.position.y - 1] == null) && (raft.palyaPrev[this.position.x][this.position.y - 1] instanceof Viz)) {


                    raft.palyaTutaj[this.position.x][this.position.y - 1] = new Tutaj(this.position.x, this.position.y - 1);
                    this.addJatekElemToInventory("tutaj", raft.palyaTutaj[this.position.x][this.position.y - 1], 1, this.position.x, this.position.y - 1);
                    return true;


                } else if ((this.position.y != raft.getMaxPalyaY() - 1) && (raft.palyaTutaj[this.position.x][this.position.y + 1] == null) && (raft.palyaPrev[this.position.x][this.position.y + 1] instanceof Viz)) {


                    raft.palyaTutaj[this.position.x][this.position.y + 1] = new Tutaj(this.position.x, this.position.y + 1);
                    this.addJatekElemToInventory("tutaj", raft.palyaTutaj[this.position.x][this.position.y + 1], 1, this.position.x, this.position.y + 1);
                    return true;

                } else if ((this.position.x != raft.getMaxPalyaX() - 1) && (raft.palyaTutaj[this.position.x + 1][this.position.y] == null) && (raft.palyaPrev[this.position.x + 1][this.position.y] instanceof Viz)) {

                    raft.palyaTutaj[this.position.x + 1][this.position.y] = new Tutaj(this.position.x + 1, this.position.y);
                    this.addJatekElemToInventory("tutaj", raft.palyaTutaj[this.position.x + 1][this.position.y], 1, this.position.x + 1, this.position.y);
                    return true;


                } else {

                    raft.palyaTutaj[szabadSzomszedosMezok.get(randomInd).x][szabadSzomszedosMezok.get(randomInd).y] = new Tutaj(szabadSzomszedosMezok.get(randomInd).x, szabadSzomszedosMezok.get(randomInd).y);
                    this.addJatekElemToInventory("tutaj", raft.palyaTutaj[szabadSzomszedosMezok.get(randomInd).x][szabadSzomszedosMezok.get(randomInd).y], 1, szabadSzomszedosMezok.get(randomInd).x, szabadSzomszedosMezok.get(randomInd).y);
                    return true;

                }

            case 2:

                if (raft.palyaTutaj[this.position.x][this.position.y] == null) return false;

                if (getInventoryJatekElemCount("deszka") < 4) return false;
                if (getInventoryJatekElemCount("level") < 4) return false;
                if (getInventoryJatekElemCount("hulladek") < 4) return false;

                this.removeJatekElemFromInventory("deszka", 4);
                this.removeJatekElemFromInventory("level", 4);
                this.removeJatekElemFromInventory("hulladek", 4);

                inventory.get("landzsa").add(new Landzsa(-1, -1));

                return true;


            case 3:

                if (raft.palyaTutaj[this.position.x][this.position.y] == null) return false;

                if (raft.palyaEpitmenyek[this.position.x][this.position.y] != null) return false;

                if (getInventoryJatekElemCount("deszka") < 2) return false;
                if (getInventoryJatekElemCount("level") < 4) return false;
                if (getInventoryJatekElemCount("hulladek") < 3) return false;

                this.removeJatekElemFromInventory("deszka", 2);
                this.removeJatekElemFromInventory("level", 4);
                this.removeJatekElemFromInventory("hulladek", 3);

                raft.palyaEpitmenyek[this.position.x][this.position.y] = new Tuzhely(this.position.x, this.position.y, this);

                this.addJatekElemToInventory("tuzhely", raft.palyaEpitmenyek[this.position.x][this.position.y], 1, this.position.x, this.position.y);

                return true;

            case 4:

                if (raft.palyaTutaj[this.position.x][this.position.y] == null) return false;

                if (raft.palyaEpitmenyek[this.position.x][this.position.y] != null) return false;

                if (getInventoryJatekElemCount("level") < 2) return false;
                if (getInventoryJatekElemCount("hulladek") < 4) return false;

                this.removeJatekElemFromInventory("level", 2);
                this.removeJatekElemFromInventory("hulladek", 4);

                raft.palyaEpitmenyek[this.position.x][this.position.y] = new Viztisztito(this.position.x, this.position.y, this);
                this.addJatekElemToInventory("viztisztito", raft.palyaEpitmenyek[this.position.x][this.position.y], 1, this.position.x, this.position.y);

                return true;


            case 5:

                if (this.getSzabadSzomszedosPoziciok(false)) return false;

                if (getInventoryJatekElemCount("deszka") < 2) return false;
                if (getInventoryJatekElemCount("level") < 6) return false;

                this.removeJatekElemFromInventory("deszka", 2);
                this.removeJatekElemFromInventory("level", 6);

                randomInd = rd.nextInt(szabadSzomszedosMezok.size());

                if ((this.position.x != 0) && (raft.palyaTutaj[this.position.x - 1][this.position.y] == null) && (raft.palyaPrev[this.position.x - 1][this.position.y] instanceof Viz)
                        && (raft.palyaEpitmenyek[this.position.x - 1][this.position.y] == null)) {

                    raft.palyaEpitmenyek[this.position.x - 1][this.position.y] = new Halo(this.position.x - 1, this.position.y);
                    this.addJatekElemToInventory("halo", raft.palyaEpitmenyek[this.position.x - 1][this.position.y], 1, this.position.x - 1, this.position.y);
                    return true;


                } else if ((this.position.y != 0) && (raft.palyaTutaj[this.position.x][this.position.y - 1] == null) && (raft.palyaPrev[this.position.x][this.position.y - 1] instanceof Viz)
                        && (raft.palyaEpitmenyek[this.position.x][this.position.y - 1] == null)) {


                    raft.palyaEpitmenyek[this.position.x][this.position.y - 1] = new Halo(this.position.x, this.position.y - 1);
                    this.addJatekElemToInventory("halo", raft.palyaEpitmenyek[this.position.x][this.position.y - 1], 1, this.position.x, this.position.y - 1);
                    return true;


                } else if ((this.position.y != raft.getMaxPalyaY() - 1) && (raft.palyaTutaj[this.position.x][this.position.y + 1] == null)
                        && (raft.palyaPrev[this.position.x][this.position.y + 1] instanceof Viz) && (raft.palyaEpitmenyek[this.position.x][this.position.y + 1] == null)) {


                    raft.palyaEpitmenyek[this.position.x][this.position.y + 1] = new Halo(this.position.x, this.position.y + 1);
                    this.addJatekElemToInventory("halo", raft.palyaEpitmenyek[this.position.x][this.position.y + 1], 1, this.position.x, this.position.y + 1);
                    return true;

                } else if ((this.position.x != raft.getMaxPalyaX() - 1) && (raft.palyaTutaj[this.position.x + 1][this.position.y] == null)
                        && (raft.palyaPrev[this.position.x + 1][this.position.y] instanceof Viz) && (raft.palyaEpitmenyek[this.position.x + 1][this.position.y] == null)) {

                    raft.palyaEpitmenyek[this.position.x + 1][this.position.y] = new Halo(this.position.x + 1, this.position.y);
                    this.addJatekElemToInventory("halo", raft.palyaEpitmenyek[this.position.x + 1][this.position.y], 1, this.position.x + 1, this.position.y);
                    return true;

                } else {

                    raft.palyaEpitmenyek[szabadSzomszedosMezok.get(randomInd).x][szabadSzomszedosMezok.get(randomInd).y] =
                            new Halo(szabadSzomszedosMezok.get(randomInd).x, szabadSzomszedosMezok.get(randomInd).y);
                    this.addJatekElemToInventory("halo", raft.palyaEpitmenyek[szabadSzomszedosMezok.get(randomInd).x][szabadSzomszedosMezok.get(randomInd).y],
                            1, szabadSzomszedosMezok.get(randomInd).x, szabadSzomszedosMezok.get(randomInd).y);
                    return true;

                }

            default:
                return false;
        }


    }

    public boolean horgaszik() {


        if (!raft.isFohosVizbenVan()) return false;

        if (1 + rd.nextInt(100) < 26) {
            this.addJatekElemToInventory("hal", null, 1, -1, -1);

        }

        return true;
    }

    public boolean felvesz() {

        talaltNyersanyagok.clear();


        int minX, maxX, minY, maxY;

        if (this.position.x == 0) minX = 0;
        else minX = this.position.x - 1;

        if (this.position.x == (this.raft.getMaxPalyaX() - 1)) maxX = this.raft.getMaxPalyaX() - 1;
        else maxX = this.position.x + 1;

        if (this.position.y == 0) minY = 0;
        else minY = this.position.y - 1;

        if (this.position.y == (this.raft.getMaxPalyaY() - 1)) maxY = this.raft.getMaxPalyaY() - 1;
        else maxY = this.position.y + 1;

        for (int i = minX; i <= maxX; i++) {

            for (int j = minY; j <= maxY; j++) {

                if (this.position.x == i && this.position.y == j) continue;

                if ((raft.palyaTutaj[i][j] != null) && (raft.palyaTutaj[this.position.x][this.position.y] != null))
                    continue;

                if (this.raft.palyaPrev[i][j] instanceof Deszka ||
                        this.raft.palyaPrev[i][j] instanceof Hordo ||
                        this.raft.palyaPrev[i][j] instanceof Hulladek ||
                        this.raft.palyaPrev[i][j] instanceof Level) {

                    talaltNyersanyagok.add(new Position(i, j));

                }

            }
        }

        if (talaltNyersanyagok.isEmpty()) return false;

        for (Position p : talaltNyersanyagok) {

            if (this.raft.palyaPrev[p.x][p.y] instanceof Hordo) {

                raft.statusString.append("A h\u00E1l\u00F3nk fogott egy hord\u00F3t!");

                this.kinyitHordot();

                this.raft.palyaPrev[p.x][p.y] = null;

                this.raft.palyaPrev[p.x][p.x] = new Viz(p.x, p.y);

                return true;
            }

        }

        Position randPos = talaltNyersanyagok.get(rd.nextInt(talaltNyersanyagok.size()));

        switch (this.raft.palyaPrev[randPos.x][randPos.y].getDrawChar()) {

            case '/':
                this.addJatekElemToInventory("deszka", null, 1, -1, -1);
                raft.statusString.append("A h\u00E1l\u00F3nk fogott egy deszk\u00E1t!");
                break;

            case '@':
                this.addJatekElemToInventory("hulladek", null, 1, -1, -1);
                raft.statusString.append("A h\u00E1l\u00F3nk fogott egy hullad\u00E9kot!");
                break;

            case 'V':
                this.addJatekElemToInventory("level", null, 1, -1, -1);
                raft.statusString.append("A h\u00E1l\u00F3nk fogott egy levelet!");
                break;

            default:
                break;

        }

        this.raft.palyaPrev[randPos.x][randPos.y] = null;
        this.raft.palyaPrev[randPos.x][randPos.y] = new Viz(randPos.x, randPos.y);

        return true;

    }

    public void kinyitHordot() {

        int d = 0, l = 0, h = 0, b = 0;

        for (int i = 0; i < 5; i++) {

            int whatWeFound = rd.nextInt(4);

            switch (whatWeFound) {

                case 0:
                    d++;
                    this.addJatekElemToInventory("deszka", null, 1, -1, -1);
                    break;

                case 1:
                    l++;
                    this.addJatekElemToInventory("level", null, 1, -1, -1);
                    break;

                case 2:
                    h++;
                    this.addJatekElemToInventory("hulladek", null, 1, -1, -1);
                    break;

                case 3:
                    b++;
                    this.addJatekElemToInventory("burgonya", null, 1, -1, -1);
                    break;

                default:
                    break;
            }
        }

        raft.statusString.append(String.format(" [A hord\u00F3 tartalma: %d burgonya, %d deszka, %d hullad\u00E9k, %d lev\u00E9l]", b, d, h, l));

    }


    public boolean vedekezik() {

        if (this.landzsa.getIsEldobottLandzsaAktiv()) return false;

        if (!isCapaVizbenVan() || !isCapaWithinAllowedRangeForLandzsaAttack(10))
            return false;

        if (getInventoryJatekElemCount("landzsa") < 1) return false;

        this.landzsa.setIsEldobottLandzsaAktiv(true);

        this.landzsa.position = this.capa.position;

        this.capa.setDrawChar('A');

        removeJatekElemFromInventory("landzsa", 1);

        return true;

    }


    private boolean isCapaVizbenVan() {

        return ((raft.palyaTutaj[capa.position.x][capa.position.y] == null) && (raft.palyaEpitmenyek[capa.position.x][capa.position.y] == null));

    }

    public boolean isCapaWithinAllowedRangeForLandzsaAttack(int maxDistance) {

        return (Raft.distance(this.position.x, this.position.y, this.capa.position.x, this.capa.position.y) < maxDistance);


    }

    private boolean getSzabadSzomszedosPoziciok(boolean tutajMezoketKeres) {

        szabadSzomszedosMezok.clear();

        int minX, maxX, minY, maxY;

        if (this.position.x == 0) minX = 0;
        else minX = this.position.x - 1;

        if (this.position.x == (raft.getMaxPalyaX() - 1)) maxX = raft.getMaxPalyaX() - 1;
        else maxX = this.position.x + 1;

        if (this.position.y == 0) minY = 0;
        else minY = this.position.y - 1;

        if (this.position.y == (raft.getMaxPalyaY() - 1)) maxY = raft.getMaxPalyaY() - 1;
        else maxY = this.position.y + 1;

        for (int i = minX; i <= maxX; i++) {

            for (int j = minY; j <= maxY; j++) {

                if ((this.position.x == i) && (this.position.y == j)) continue;

                if (tutajMezoketKeres) {

                    if ((raft.palyaTutaj[i][j] != null) && (raft.palyaEpitmenyek[i][j] == null)) {

                        szabadSzomszedosMezok.add(new Position(i, j));

                    }

                } else if ((raft.palyaPrev[i][j] instanceof Viz)
                        && (raft.palyaEpitmenyek[i][j] == null)
                        && (raft.palyaTutaj[i][j] == null)) {

                    szabadSzomszedosMezok.add(new Position(i, j));

                }

            }
        }

        return (szabadSzomszedosMezok.isEmpty());

    }

	public boolean getIsFohosAktivkodott() {
		return isFohosAktivkodott;
	}

	public void setIsFohosAktivkodott(boolean isFohosAktivkodott) {
		this.isFohosAktivkodott = isFohosAktivkodott;
	}

}
