/**
 * @author Kovács Attila
 * @version 1.1
 */

import java.awt.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Raft extends JFrame implements ActionListener, KeyListener, WindowListener {

	private static final int MIN_NUMBER_OF_ACTIONS_TO_SURVIVE = 1500;
    private static final Random rd = new Random();

    public static final String gameTitle = "Raft";
    public static final String versionNumber = "1.1";
	
    JatekElem[][] palyaPrev;
    JatekElem[][] palyaNext;
    JatekElem[][] palyaTutaj;
    JatekElem[][] palyaEpitmenyek;
    JatekElem[][] palyaFohos;
    JatekElem[][] palyaCapa;

    static int gameWindowWidth, gamewindowHeight;
    static int gameBoardWidth, gameBoardHeight;
    static int gameInfoPanelWidth, gameInfoPanelHeight;

    public static int imageWidth;
    public static int imageHeight;

    private int maxPalyaX;
    private int maxPalyaY;
    private boolean isGameRunning;

    Fohos fohos;
    Capa capa;
    Landzsa landzsa;

    private ArrayList<Position> szabadSzomszedosMezok;
    private ArrayList<Integer> sortedDistances;

    private final int DELAY = 1000;
    private Timer timer;
    private RaftGameBoardPanel gameMapPanel;
    private RaftGameInformationPanel gameInfoPanel;

    StringBuilder statusString;
	StringBuilder gameResultMessage;

    BufferedImage gameBoardImage;
    public static Image burgonyaImg, capaImg, deszkaImg, ehinsegSzintImg, etelImg, fohosImg, halImg, halalfejImg, haloImg, hordoImg, hulladekImg, italImg, landzsaImg, levelImg, szomjusagSzintImg, tutajImg, tuzhelyImg, vizImg, viztisztitoImg;

	private int capaTamadasEselye;
	private int maxSoronkentMegjelenoUjNyersanyagokSzama;   

    private int frameCounter = 0;

    private boolean isRealTimeModeOn;

    private boolean playerWonTheGame;

    public Raft() {

        this(25, 35);
    }
	
	public Raft(int maxPalyaX, int maxPalyaY) {
	
		this(maxPalyaX, maxPalyaY, 50, 4);	
	
	}

    public Raft(int maxPalyaX, int maxPalyaY, int capaTamadasEselye, int maxSoronkentMegjelenoUjNyersanyagokSzama) {

        if (maxPalyaX < 25) throw new IllegalArgumentException("maxPalyaX nem lehet kisebb 25-nel!");

        if (maxPalyaY < 35) throw new IllegalArgumentException("maxPalyaY nem lehet kisebb 35-nel!");

        this.maxPalyaX = maxPalyaX;
        this.maxPalyaY = maxPalyaY;
		this.capaTamadasEselye = capaTamadasEselye;
		this.maxSoronkentMegjelenoUjNyersanyagokSzama = maxSoronkentMegjelenoUjNyersanyagokSzama;
        this.isRealTimeModeOn = true;
        this.playerWonTheGame = false;
		
		Toolkit dt = Toolkit.getDefaultToolkit();
        Dimension dim = dt.getScreenSize();

        gamewindowHeight = dim.height - 30;

        imageWidth = imageHeight = (gamewindowHeight - maxPalyaX - 120) / maxPalyaX;

        gameBoardHeight = (imageHeight * maxPalyaX) + maxPalyaX + 1;

        gameWindowWidth = gameBoardWidth = gameInfoPanelWidth = (imageWidth * maxPalyaY) + maxPalyaY + 1;

        gameInfoPanelHeight = 100;
		
		statusString = new StringBuilder();
		gameResultMessage = new StringBuilder();
		
		gameBoardImage = new BufferedImage(gameBoardWidth, gameBoardHeight, BufferedImage.TYPE_3BYTE_BGR);
        
		loadIconsAndImages();
		
		try {
			
			Thread.sleep(1000);
		
		} catch(InterruptedException ie) {
		
		}
		
		palyaPrev = new JatekElem[maxPalyaX][maxPalyaY];
        palyaNext = new JatekElem[maxPalyaX][maxPalyaY];
        palyaTutaj = new JatekElem[maxPalyaX][maxPalyaY];
        palyaFohos = new JatekElem[maxPalyaX][maxPalyaY];
        palyaCapa = new JatekElem[maxPalyaX][maxPalyaY];
        palyaEpitmenyek = new JatekElem[maxPalyaX][maxPalyaY];

        for (int i = 0; i < maxPalyaX; i++) {

            for (int j = 0; j < maxPalyaY; j++) {

                palyaPrev[i][j] = new Viz(i, j);

            }
        }

        for (int i = 0; i < 2; i++) {

            for (int j = 0; j < 2; j++) {

                palyaTutaj[maxPalyaX / 2 + i][maxPalyaY / 2 + j - 1] = new Tutaj(maxPalyaX / 2 + i, maxPalyaY / 2 + j - 1);

            }
        }

        szabadSzomszedosMezok = new ArrayList<>();
        sortedDistances = new ArrayList<>();

        int randomCapaX, randomCapaY;

        do {

            randomCapaX = 1 + rd.nextInt(maxPalyaX - 1);
            randomCapaY = rd.nextInt(maxPalyaY);

        }
        while ((palyaTutaj[randomCapaX][randomCapaY] != null) || !isCapaTutajDistanceWithinAllowedRange(randomCapaX, randomCapaY, 1, 10));

        capa = new Capa(randomCapaX, randomCapaY);

        palyaCapa[randomCapaX][randomCapaY] = capa;

        landzsa = new Landzsa(-1, -1);
        landzsa.setIsEldobottLandzsaAktiv(false);

        fohos = new Fohos(maxPalyaX / 2, maxPalyaY / 2 - 1, this, capa, landzsa);

        palyaFohos[maxPalyaX / 2][maxPalyaY / 2 - 1] = fohos;

        for (int i = 0; i < 2; i++) {

            for (int j = 0; j < 2; j++) {

                fohos.addJatekElemToInventory("tutaj", palyaTutaj[maxPalyaX / 2 + i][maxPalyaY / 2 + j - 1], 1, maxPalyaX / 2 + i, maxPalyaY / 2 + j - 1);

            }
        }

        Image image = null;

        try {

            URL imgUrl = this.getClass().getResource("Images/raft_game_icon.png");
            image = ImageIO.read(imgUrl);

        } catch (IOException e) {

            System.err.println("Error occured while reading in icon image file: 'Images/raft_game_icon.png'");
            System.err.println(e.getMessage());
            e.printStackTrace();

        }

        if (image != null) this.setIconImage(image);       

        this.gameMapPanel = new RaftGameBoardPanel(this);
        this.gameInfoPanel = new RaftGameInformationPanel(this);

        this.getContentPane().add(gameMapPanel, BorderLayout.NORTH);
        this.getContentPane().add(gameInfoPanel, BorderLayout.SOUTH);

        this.addKeyListener(this);
        this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Raft v1.0");
        this.setResizable(false);
        this.setSize(gameWindowWidth, gamewindowHeight);
        this.pack();
        this.setLocation((dim.width - gameWindowWidth) / 2, 0);        

        timer = new Timer(DELAY, this);


    }

    private void loadIconsAndImages() {

        burgonyaImg = new ImageIcon(this.getClass().getResource("Images/burgonya.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        capaImg = new ImageIcon(this.getClass().getResource("Images/capa2.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        deszkaImg = new ImageIcon(this.getClass().getResource("Images/deszka.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        ehinsegSzintImg = new ImageIcon(this.getClass().getResource("Images/hunger_level.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        etelImg = new ImageIcon(this.getClass().getResource("Images/etel.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        fohosImg = new ImageIcon(this.getClass().getResource("Images/fohos2.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        halImg = new ImageIcon(this.getClass().getResource("Images/hal.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        halalfejImg = new ImageIcon(this.getClass().getResource("Images/death_skull.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        haloImg = new ImageIcon(this.getClass().getResource("Images/halo.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        hordoImg = new ImageIcon(this.getClass().getResource("Images/hordo.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        hulladekImg = new ImageIcon(this.getClass().getResource("Images/hulladek.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        italImg = new ImageIcon(this.getClass().getResource("Images/ital.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        landzsaImg = new ImageIcon(this.getClass().getResource("Images/landzsa.png")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        levelImg = new ImageIcon(this.getClass().getResource("Images/level.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        szomjusagSzintImg = new ImageIcon(this.getClass().getResource("Images/thirst_level.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        tutajImg = new ImageIcon(this.getClass().getResource("Images/tutaj.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        tuzhelyImg = new ImageIcon(this.getClass().getResource("Images/tuzhely2.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        vizImg = new ImageIcon(this.getClass().getResource("Images/viz.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
        viztisztitoImg = new ImageIcon(this.getClass().getResource("Images/viztisztito.jpg")).getImage().getScaledInstance(Raft.imageWidth, Raft.imageHeight, Image.SCALE_SMOOTH);
		
		/*
		origBurgonyaImg = new ImageIcon(this.getClass().getResource("Images/burgonya.jpg")).getImage();
		origDeszkaImg = ;
		origEhinsegSzintImg = ;
		origEtelImg = ;
		origHalImg = ;
		origHaloImg = ;
		origHulladekImg = ;
		origItalImg = ;
		origLandzsaImg = ;
		origLevelImg = ;
		origSzomjusagSzintImg = ;
		origTutajImg = ;
		origTuzhelyImg = ;
		origViztisztitoImg = ;
		*/				
    }


    public boolean getIsPlayerWonTheGame() {
        return playerWonTheGame;
    }

    public void setIsPlayerWonTheGame(boolean playerWonTheGame) {
        this.playerWonTheGame = playerWonTheGame;
    }

    public int getMaxPalyaX() {
        return maxPalyaX;
    }


    public int getMaxPalyaY() {
        return maxPalyaY;
    }


    public int getFrameCount() {
        return this.frameCounter;
    }

    public boolean getIsGameRunning() {

        return isGameRunning;

    }

    public boolean getIsRealTimeModeOn() {
        return isRealTimeModeOn;
    }

    public void startGame() {

        isGameRunning = true;

        this.renderFrame();

        this.gameMapPanel.redrawRaftGameBoard();
        this.gameInfoPanel.updateRaftGameInformationPanel();

        timer.start();

    }

    public void stopGame(boolean isPlayerWon) {
	
		this.timer.stop();
		this.timer = null;	
        
		setIsPlayerWonTheGame(isPlayerWon);

        isGameRunning = false;
		
		// gameBoardImage = null;
		// gameBoardImage = new BufferedImage(gameBoardWidth, gameBoardHeight, BufferedImage.TYPE_3BYTE_BGR);
		// gameBoardImage.flush();
		
		buildFinalGameBoardImage(false);		
		this.gameMapPanel.redrawRaftGameBoard();
        this.gameInfoPanel.updateRaftGameInformationPanel();              

    }

    public void handleNonRealTimeMode() {
	
		this.fohos.setIsFohosLepett(false);
        this.fohos.setIsFohosCarriedOutAction(false);
		
		if (this.checkIfGameHasEnded()) return;

        this.renderFrame();

        this.gameMapPanel.redrawRaftGameBoard();

        this.gameInfoPanel.updateRaftGameInformationPanel();       

    }

    public boolean isFohosVizbenVan() {

        return ((palyaTutaj[fohos.position.x][fohos.position.y] == null) && (palyaEpitmenyek[fohos.position.x][fohos.position.y] == null));

    }

    public boolean checkIfGameHasEnded() {
	
		frameCounter++;
		
		this.fohos.novelEhseg(1);
		
		this.fohos.novelSzomjusag(1);

        if (frameCounter >= MIN_NUMBER_OF_ACTIONS_TO_SURVIVE) {

            this.stopGame(true);			
			return true;

        }
		
		if (this.fohos.getIsKarakterEatenByTheShark()) {
			
			this.gameResultMessage.append("You've got eaten by the shark!\n");
            this.fohos.setIsKarakterAlive(false);
            this.stopGame(false);
            return true;

        }        
		
		if (!this.fohos.getIsKarakterAlive()) {
			this.gameResultMessage.append("You've died of hunger!\n");
            this.stopGame(false);
            return true;
        }       

        if (!this.fohos.getIsKarakterAlive()) {
			this.gameResultMessage.append("You've died of thirst!\n");
            this.stopGame(false);
            return true;
        }	

        return false;
    }


    private void moveCapa() {

        szabadSzomszedosMezok.clear();

        int minX, maxX, minY, maxY;

        if (capa.position.x == 0) minX = 0;
        else minX = capa.position.x - 1;

        if (capa.position.x == (maxPalyaX - 1)) maxX = maxPalyaX - 1;
        else maxX = capa.position.x + 1;

        if (capa.position.y == 0) minY = 0;
        else minY = capa.position.y - 1;

        if (capa.position.y == (maxPalyaY - 1)) maxY = maxPalyaY - 1;
        else maxY = capa.position.y + 1;

        for (int i = minX; i <= maxX; i++) {

            for (int j = minY; j <= maxY; j++) {

                if ((capa.position.x == i) && (capa.position.y == j)) continue;

                if (((palyaFohos[i][j] == null) && ((palyaTutaj[i][j] == null) && (palyaEpitmenyek[i][j] == null))) || ((palyaFohos[i][j] == null) && isFohosVizbenVan())) {

                    szabadSzomszedosMezok.add(new Position(i, j));

                } else if ((palyaFohos[i][j] != null) && isFohosVizbenVan()) {

                    int pr = rd.nextInt(100);

                    if (pr < this.capaTamadasEselye) {

						// amikor a capa vegez a fohosunkkel, a capa poziciojanak x, y koordinatai nem valtoznak meg
					    // palyaCapa[capa.position.x][capa.position.y] = null;
                        // palyaCapa[i][j] = capa;
                        // capa.position.x = i;
                        // capa.position.y = j;	
						
				
						fohos.setIsKarakterEatenByTheShark(true);
                       

                        return;
                    }

                }

            }
        }

        if (!szabadSzomszedosMezok.isEmpty()) {

            Position randPos;

            if (isFohosVizbenVan()) {

                randPos = findNextClosestPositionBetweenCapaAndFohos();

            } else {

                do {

                    randPos = szabadSzomszedosMezok.get(rd.nextInt(szabadSzomszedosMezok.size()));

                } while (!isCapaTutajDistanceWithinAllowedRange(randPos.x, randPos.y, 1, 10));

            }

            this.palyaCapa[randPos.x][randPos.y] = capa;

            this.palyaCapa[capa.position.x][capa.position.y] = null;

            capa.position.x = randPos.x;

            capa.position.y = randPos.y;

        }

    }

    private void renderFrame() {

        int yCoord;

        for (int i = maxPalyaX - 2; i >= 0; i--) {

            for (int j = 0; j < maxPalyaY; j++) {

                if (palyaPrev[i][j] == null) palyaPrev[i][j] = new Viz(i, j);

                palyaPrev[i + 1][j] = palyaPrev[i][j];

                if ((palyaEpitmenyek[i + 1][j] != null) && (palyaEpitmenyek[i + 1][j].getDrawChar() == '#') && (palyaPrev[i + 1][j].getDrawChar() != ' ')) {

                    switch (palyaPrev[i + 1][j].getDrawChar()) {

                        case '/':
                            this.fohos.addJatekElemToInventory("deszka", null, 1, -1, -1);
                            this.statusString.append("A h\u00E1l\u00F3nk fogott egy deszk\u00E1t!");
                            break;

                        case 'U':
                            this.statusString.append("A h\u00E1l\u00F3nk fogott egy hord\u00F3t!");
                            this.fohos.kinyitHordot();
                            break;

                        case '@':
                            this.statusString.append("A h\u00E1l\u00F3nk fogott egy hullad\u00E9kot!");
                            this.fohos.addJatekElemToInventory("hulladek", null, 1, -1, -1);
                            break;

                        case 'V':
                            this.statusString.append("A h\u00E1l\u00F3nk fogott egy levelet!");
                            this.fohos.addJatekElemToInventory("level", null, 1, -1, -1);
                            break;

                        default:
                            break;

                    }

                    palyaPrev[i + 1][j] = null;
                    palyaPrev[i + 1][j] = new Viz(i + 1, j);

                }

            }
        }

        for (int j = 0; j < maxPalyaY; j++) {

            if (palyaPrev[0][j].getDrawChar() != ' ') {
                palyaPrev[0][j] = null;
                palyaPrev[0][j] = new Viz(0, j);
            }
        }

        for (int i = 0; i < rd.nextInt(this.maxSoronkentMegjelenoUjNyersanyagokSzama); i++) {

            do {

                yCoord = rd.nextInt(maxPalyaY);

                if ((this.capa.position.x == 0) && (this.capa.position.y == yCoord)) continue;

                if ((this.fohos.position.x == 0) && (this.fohos.position.y == yCoord)) continue;


            } while (palyaPrev[0][yCoord].getDrawChar() != ' ');

            int prob = 1 + rd.nextInt(100);

            palyaPrev[0][yCoord] = null;

            if (prob < 33) palyaPrev[0][yCoord] = new Deszka(0, yCoord);
            else if ((prob > 32) && (prob < 65)) palyaPrev[0][yCoord] = new Level(0, yCoord);
            else if ((prob > 64) && (prob < 96)) palyaPrev[0][yCoord] = new Hulladek(0, yCoord);
            else palyaPrev[0][yCoord] = new Hordo(0, yCoord);

        }


        if (landzsa.getIsEldobottLandzsaAktiv()) {

            if (landzsa.updateEldobottLandzsaFrameCounter()) {

                capa.setDrawChar('>');
            }

        } else {

            this.moveCapa();

        }

        boolean capaTutajAlatt = false;


        if ((palyaTutaj[this.capa.position.x][this.capa.position.y] != null) || (palyaEpitmenyek[this.capa.position.x][this.capa.position.y] != null)) {

            capaTutajAlatt = true;

        }        
		
		buildFinalGameBoardImage(capaTutajAlatt);
		
    }
	
	private void buildFinalGameBoardImage(boolean capaTutajAlatt) {
	
		Image img;
		Graphics2D g2 = gameBoardImage.createGraphics();
	
		for (int i = 0; i < maxPalyaX; i++) {

            for (int j = 0; j < maxPalyaY; j++) {

                if (palyaFohos[i][j] != null) {
                    palyaNext[i][j] = palyaFohos[i][j];
					img = fohos.getDrawImage();					
					g2.drawImage(img, 1 + j + (img.getWidth(null) * j), 1 + i + (img.getHeight(null) * i), null);
					continue;
                }

                if (!capaTutajAlatt && (palyaCapa[i][j] != null)) {
                    palyaNext[i][j] = this.capa;
                    img = palyaNext[i][j].getDrawImage();
                    g2.drawImage(img, 1 + j + (img.getWidth(null) * j), 1 + i + (img.getHeight(null) * i), null);
                    continue;

                }

                if (palyaEpitmenyek[i][j] != null) {

                    palyaNext[i][j] = palyaEpitmenyek[i][j];
                    img = palyaNext[i][j].getDrawImage();
                    g2.drawImage(img, 1 + j + (img.getWidth(null) * j), 1 + i + (img.getHeight(null) * i), null);
                    if (palyaEpitmenyek[i][j].getDrawChar() == 'W') {
                        ((Tuzhely) palyaEpitmenyek[i][j]).updateTuzhely();
                    } else if (palyaEpitmenyek[i][j].getDrawChar() == '%') {
                        ((Viztisztito) palyaEpitmenyek[i][j]).updateViztisztito();
                    }

                    continue;
                }

                if (palyaTutaj[i][j] != null) {
                    palyaNext[i][j] = palyaTutaj[i][j];
                    img = palyaNext[i][j].getDrawImage();
                    g2.drawImage(img, 1 + j + (img.getWidth(null) * j), 1 + i + (img.getHeight(null) * i), null);
                    continue;
                }


                palyaNext[i][j] = palyaPrev[i][j];
                img = palyaNext[i][j].getDrawImage();
                g2.drawImage(img, 1 + j + (img.getWidth(null) * j), 1 + i + (img.getHeight(null) * i), null);

            }
        }		
		
	}

    public boolean isCapaTutajDistanceWithinAllowedRange(int xCoord, int yCoord, int minDistance, int maxDistance) {

        int min = 100, calculatedDistance;

        for (int i = 0; i < maxPalyaX; i++) {

            for (int j = 0; j < maxPalyaY; j++) {

                if ((palyaTutaj[i][j] != null) || (palyaEpitmenyek[i][j] != null)) {

                    calculatedDistance = distance(xCoord, yCoord, i, j);

                    if (calculatedDistance < min) {

                        min = calculatedDistance;
                    }

                }

            }
        }

        if ((min >= minDistance) && (min <= maxDistance)) return true;

        return false;

    }

    public Position findNextClosestPositionBetweenCapaAndFohos() {

        int calculatedDistance, currentDistance;

        sortedDistances.clear();

        for (Position p : szabadSzomszedosMezok) {

            sortedDistances.add(distance(p.x, p.y, fohos.position.x, fohos.position.y));


        }

        sortedDistances.sort(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return (o1 - o2);
			}     
        
        });

        if (sortedDistances.size() > 2) {

            currentDistance = sortedDistances.get(rd.nextInt(3));

            for (Position p : szabadSzomszedosMezok) {

                calculatedDistance = distance(p.x, p.y, fohos.position.x, fohos.position.y);

                if (calculatedDistance == currentDistance) return p;

            }

        }

        return szabadSzomszedosMezok.get(rd.nextInt(szabadSzomszedosMezok.size()));

    }

    public static int distance(int x1, int y1, int x2, int y2) {

        return (int) Math.floor(Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));

    }

    public void updateGameInformationPanel() {

        this.gameInfoPanel.updateRaftGameInformationPanel();

    }

    public void handleKeyPressedEvent(KeyEvent e) {


        switch (e.getKeyChar()) {

            case KeyEvent.VK_ESCAPE:
                if (!getIsGameRunning()) System.exit(0);
                this.fohos.setIsKarakterAlive(false);
				this.gameResultMessage.append("Game Over!");
				this.stopGame(false);
                break;

            case 'w':
            case 'W':

                this.fohos.lep('w');
                break;

            case 's':
            case 'S':

                this.fohos.lep('s');
                break;

            case 'a':
            case 'A':

                this.fohos.lep('a');
                break;

            case 'd':
            case 'D':

                this.fohos.lep('d');
                break;

            case 'c':
            case 'C':
                if (this.fohos.foz()) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case 'h':
            case 'H':
                if (this.fohos.horgaszik()) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case 'e':
            case 'E':
                if (this.fohos.eszik()) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case 'f':
            case 'F':
                if (this.fohos.felvesz()) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case 'i':
            case 'I':
                if (this.fohos.iszik()) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case 'r':
            case 'R':

                this.isRealTimeModeOn = !this.isRealTimeModeOn;

                if (!this.isRealTimeModeOn) {

                    this.timer.stop();

                } else {

                    this.timer.start();

                }

                break;

            case 'v':
            case 'V':
                if (this.fohos.vedekezik()) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case '1':
                if (this.fohos.epit(1)) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case '2':
                if (this.fohos.epit(2)) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case '3':
                if (this.fohos.epit(3)) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case '4':
                if (this.fohos.epit(4)) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            case '5':
                if (this.fohos.epit(5)) this.fohos.setIsFohosCarriedOutAction(true);
                break;

            default:
                break;

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	
		if (!this.isGameRunning) return;
		
		this.fohos.setIsFohosLepett(false);
        this.fohos.setIsFohosCarriedOutAction(false);	
		
		if (this.checkIfGameHasEnded()) {
		
			return;
		}
		
		this.renderFrame();

        this.gameMapPanel.redrawRaftGameBoard();

        this.gameInfoPanel.updateRaftGameInformationPanel();
        
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

        // if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.fohos.setIsKarakterAlive(false);
			this.gameResultMessage.append("Game Over!");
            this.stopGame(false);
		// }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        handleKeyPressedEvent(e);

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class RaftGameBoardPanel extends JPanel implements KeyListener {

    private RaftGameBoardCanvas rc;
    private Raft r;

    public RaftGameBoardPanel(Raft r) {

        rc = new RaftGameBoardCanvas(r);
        add(rc);

        this.r = r;

        addKeyListener(this);
        setSize(Raft.gameBoardWidth, Raft.gameBoardHeight);
        setPreferredSize(new Dimension(Raft.gameBoardWidth, Raft.gameBoardHeight));
        setFocusable(true);
        setDoubleBuffered(true);
        setVisible(true);
    }

    public void redrawRaftGameBoard() {
		rc.repaint();

    }
	
	@Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

        r.handleKeyPressedEvent(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {


    }
}

class RaftGameBoardCanvas extends Canvas implements KeyListener {

	private Raft r;

    public RaftGameBoardCanvas(Raft r) {

        this.r = r;
        setBackground(Color.BLACK);
        setSize(Raft.gameBoardWidth, Raft.gameBoardHeight);
        setPreferredSize(new Dimension(Raft.gameBoardWidth, Raft.gameBoardHeight));
        addKeyListener(this);
        setFocusable(true);
        setVisible(true);

    }

    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        g2d.drawImage(r.gameBoardImage, 0, 0, Color.BLACK, null);

        if (!r.getIsGameRunning()) {

            String imageFilePath;
            Image img;

            if (!r.getIsPlayerWonTheGame()) {

                imageFilePath = "Images/game_over_lost.jpg";

            } else {

                imageFilePath = "Images/game_over_won.png";

            }

            Rectangle rect = getBounds();

            img = new ImageIcon(imageFilePath).getImage();			
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.drawImage(img, (rect.width - img.getWidth(null)) / 2, (rect.height - img.getHeight(null)) / 2, null);
			
			if (r.gameResultMessage.length() != 0) {				
				String msg = r.gameResultMessage.toString();
				g2d.setColor(Color.RED);
				g2d.setFont(new Font("Arial", Font.BOLD, 18));
				FontMetrics fm = g2d.getFontMetrics();
				int strLen = fm.stringWidth(msg);			
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
				g2d.drawString(msg, (rect.width - strLen)/2, (rect.height - fm.getHeight())/2);				
			}

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

        r.handleKeyPressedEvent(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {


    }

}


class RaftGameInformationPanel extends JPanel {

	private RaftGameInformationCanvas rgic;

    public RaftGameInformationPanel(Raft r) {

        rgic = new RaftGameInformationCanvas(r);
        add(rgic);
        setSize(Raft.gameInfoPanelWidth, Raft.gameInfoPanelHeight);
        setPreferredSize(new Dimension(Raft.gameInfoPanelWidth, Raft.gameInfoPanelHeight));
        setDoubleBuffered(true);

    }

    public void updateRaftGameInformationPanel() {

        rgic.repaint();

    }

}

class RaftGameInformationCanvas extends Canvas {

	private Raft r;
    private Font mainTextFont;

    public RaftGameInformationCanvas(Raft r) {

        this.r = r;        
        mainTextFont = new Font("Arial", Font.BOLD, 18);
        setSize(Raft.gameInfoPanelWidth, Raft.gameInfoPanelHeight);
        setPreferredSize(new Dimension(Raft.gameInfoPanelWidth, Raft.gameInfoPanelHeight));

    }

    public void paint(Graphics g) {         
        
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        int count = 0, xPos = 10;

        g2D.setBackground(Color.MAGENTA);
        g2D.setColor(Color.BLUE);

        g2D.setFont(mainTextFont);
        g2D.setColor(Color.RED);
        g2D.drawString(String.format("Round: %4d", r.getFrameCount()), xPos, 25);

        xPos += 120;

        g2D.setColor(Color.BLUE);
        Line2D lSeg = new Line2D.Double(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.ehinsegSzintImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getEhsegSzint()), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.szomjusagSzintImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getSzomjusagSzint()), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        for (JatekElem t : r.fohos.getInventory().get("tuzhely")) {

            count += ((Tuzhely) t).getElkeszitettEtelekSzama();

        }

        g2D.drawImage(r.etelImg, xPos, 5, null);

        xPos += 40;
        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %2d", count), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        count = 0;

        for (JatekElem t : r.fohos.getInventory().get("viztisztito")) {

            count += ((Viztisztito) t).getPoharItalokSzama();

        }

        g2D.drawImage(r.italImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %2d", count), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.haloImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("halo")), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.tutajImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("tutaj")), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.tuzhelyImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("tuzhely")), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.viztisztitoImg, xPos, 5, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("viztisztito")), xPos, 25);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        lSeg.setLine(xPos, 2, xPos, 98);
        g2D.draw(lSeg);
		
		lSeg.setLine(0, 49, xPos, 49);
		g2D.draw(lSeg);

        xPos = 10;

        g2D.drawImage(r.deszkaImg, xPos, 55, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("deszka")), xPos, 75);

        xPos += 80;

        g2D.setColor(Color.BLUE);
        // lSeg.setLine(xPos, 2, xPos, 98);
        // g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.hulladekImg, xPos, 55, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("hulladek")), xPos, 75);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        // lSeg.setLine(xPos, 2, xPos, 98);
        // g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.levelImg, xPos, 55, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("level")), xPos, 75);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        // lSeg.setLine(xPos, 2, xPos, 98);
        // g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.burgonyaImg, xPos, 55, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("burgonya")), xPos, 75);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        // lSeg.setLine(xPos, 2, xPos, 98);
        // g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.halImg, xPos, 55, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("hal")), xPos, 75);

        xPos += 60;

        g2D.setColor(Color.BLUE);
        // lSeg.setLine(xPos, 2, xPos, 98);
        // g2D.draw(lSeg);

        xPos += 10;

        g2D.drawImage(r.landzsaImg, xPos, 55, null);

        xPos += 40;

        g2D.setColor(Color.RED);
        g2D.drawString(String.format(" : %4d", r.fohos.getInventoryJatekElemCount("landzsa")), xPos, 75);

    }

}






