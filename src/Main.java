/**
 * @author Kovács Attila
 * @EHA:   KOAYABT.SZE
 * @game: Raft
 * @version 1.0
 *
 * A játék irányítására szolgáló gombok:
 *
 *  'W': A karakterünk felfelé való írányítása.
 *  'S': A karakterünk lefelé való irányítása.
 *  'A': A karakterünk balra való irányítása.
 *  'D': A karakterünk jobbra való irányítása.
 *
 *   A karakterünk mehet átlósan is, amennyiben egy másodperc alatt megnyomunk két különböző irányú gombot.
 *
 *  'F': A karakterünk az őt körülvevő nyersanyagok közül tetszőlegesen felvesz egyet.
 *
 *      Az alábbi nyersanyagok fordulnak elő:
 *      deszka (32% eséllyel)
 *      levél (32% eséllyel)
 *      hulladék (32% eséllyel)
 *      hordó (4% eséllyel): ebben 5 db véletlenszerű nyersanyag helyezkedik el az alábbiak közül: deszka, levél, hulladék, burgonya (egy nyersanyag többször is elofordulhat).
 *
 *  Amennyiben rendelkezünk elegendő nyersanyaggal az alábbi dolgokat építhessük meg a következő billentyűk egyike lenyomásával:
 *
 *  '1': A tutajunk területét bővíthejük, a karakterünket körülvevő egyik random szabad helyre,ahol előzőleg víz volt.
 *  '2': A karakterünk egy lándzsát készít magának, amelyet felhasználhatja a cápa ellen,azt lebénítva 5 körre.
 *  '3': A karaterünk  egy tűzhelyet épít ki magának azon a tutajmezőn,amelyen tartózkodik, amennyiben a mezőn nincs más előzőleg megépített játékelem.
 *  '4': A karakterünk egy víztisztítót épit ki magának azon a tutajmezőn, amelyen tartózkodik,amennyiben a mezőn nincs más előzőleg megépített játékelem.
 *  '5': A karakterünk egy hálót készít, amelyet lerakhat a karakterünket körülvevő egyik random szabad helyre,ahol előzőleg víz volt. *
 *
 *
 *  'C': Karakterünkkel a tűzhelyre lépve, -e gombot megnyomva kiüritjük a hal és burgonya inventorynkat, s 25 kör alatt ő elkészíti a karakterünk által elfogyasztható ételt.
 *  'E': Karakterünkkel a tűzhelyre lépve, amennyiben  az étel elkészült már, -e gombot megnyomva elfogyaszthatjuk azt, amely 60 egységgel növeli életerőnket.
 *
 *  'I': Karakterünkkel a víztisztítóra lépve, -e gombot megnyomva vizet ihatunk, amennyiben a víztisztító létrehozása vagy a legutolsó használata óta eltelt 25 kör.
 *       Szomjúságunkat 40 egységgel csökkentjük.
 *
 *  'V': A tutajon vagy a vízben tartózkodva, amennyiben rendelkezünk lándzsával, felhasználhatjuk azt a cápa ellen,mely lebénítja őt 5 körig. Maximum távolság a lándzsa használatára 10 egységkocka.
 *
 *  'R': A játékot Real-time-ból átrakhatjük, hogy a karakterünk iránymegadása vagy különféle cselekvés elvégzése után mozogjon a többi elem is, aszinkron módon, illetve visszatehetjük, amennyiben előzőleg átraktuk.
 *
 *
 *  Amennyiben életerőnk vagy szomjuságunk 0 egységre esik, karakterünk meghal, a játék véget ér.
 *  A cápa veszélyt jelent számunkra, ha elkap minket, a játék véget ér.
 *  A játékot megnyertük, ha 1500 kör letelte után a karakterünk még mindig életben van.
 *
 */

import javax.swing.*;
import java.awt.*;


public class Main {


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				int noOfRows = 25, noOfColumns = 35, capaTamadasEselye = 50, maxMegjelenotUjNyersanyag = 4;	
				
				String szamResz;
				int ind;
				int egeszSzam;
				
				boolean cmdArgsProcessed = false;				
				
				for (int i = 0; i < args.length; i++) {
					
					if (args[i].trim().equals("-h") || args[i].trim().equals("--help") || args[i].trim().equals("/?")) {
					
						JOptionPane.showMessageDialog(null, "Here is a brief help text that describes a couple of optionally specifiable command line arguments:\n" +
								"Correct command syntax for launching the Raft v1 game:\njava Main [[--max-rows=[25,100]] [--mr=[25,100]] [--max-columns=[35,100]] [-mc=[35,100]]\n [--capa-tamadas-eselye=[10,100]] [-cte=[10,100]] [--max-megjeleno-nyersanyag=[4,10]] [-mmny=[4,10]]]", "Seg\u00EDts\u00E9g", JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);						
								System.exit(0);					
					
					} else if (args[i].trim().startsWith("--max-rows=") || args[i].trim().startsWith("--max-rows:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(11);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfRows = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);							
							
							}							
						}					
					
					} else if (args[i].trim().equals("--max-rows") && ((i+1) < args.length)) {
						
						i++;
						
						cmdArgsProcessed = true;						
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfRows = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);							
							
							}							
						}				
					
					} else if (args[i].trim().startsWith("-mr=") || args[i].trim().startsWith("-mr:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(4);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfRows = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);
							
							}						
						}
					
					} else if (args[i].trim().equals("-mr") && ((i+1) < args.length)) {
						
						i++;
						
						cmdArgsProcessed = true;						
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfRows = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);							
							
							}							
						}				
					
					} else if (args[i].trim().startsWith("--max-columns=") || args[i].trim().startsWith("--max-columns:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(14);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfColumns = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"Az oszlopok sz\u00E1ma nem lehet kevesebb 35-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}					
					
					} else if (args[i].trim().equals("--max-columns") && ((i+1) < args.length)) {
						
						i++;
						
						cmdArgsProcessed = true;						
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfColumns = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);							
							
							}							
						}				
					
					} else if (args[i].trim().startsWith("-mc=") || args[i].trim().startsWith("-mc:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(4);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 35) && (egeszSzam <= 100)) {
								
								noOfColumns = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"Az oszlopok sz\u00E1ma nem lehet kevesebb 35-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);
							
							}						
						}
					
					} else if (args[i].trim().equals("-mc") && ((i+1) < args.length)) {
						
						i++;
						
						cmdArgsProcessed = true;						
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 25) && (egeszSzam <= 100)) {
								
								noOfColumns = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l!\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);							
							
							}							
						}				
					
					} 
					// --capa-tamadas-eselye
					else if (args[i].trim().startsWith("--capa-tamadas-eselye=") || args[i].trim().startsWith("--capa-tamadas-eselye:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(22);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 10) && (egeszSzam <= 100)) {
								
								capaTamadasEselye = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A c\u00E1pa t\u00E1mad\u00E1s es\u00E9ly\u00E9nek \u00E9rt\u00E9ke nem lehet kisebb 10-n\u00E9l, vagy nagyobb 100-n\u00E1l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}										
					} else if (args[i].trim().equals("--capa-tamadas-eselye") && ((i+1) < args.length)) {
					
						i++;
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 10) && (egeszSzam <= 100)) {
								
								capaTamadasEselye = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A c\u00E1pa t\u00E1mad\u00E1s es\u00E9ly\u00E9nek \u00E9rt\u00E9ke nem lehet kisebb 10-n\u00E9l, vagy nagyobb 100-n\u00E1l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}										
					}
					// -cte=
					 else if (args[i].trim().startsWith("-cte=") || args[i].trim().startsWith("-cte:")) {
					 
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(5);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 10) && (egeszSzam <= 100)) {
								
								capaTamadasEselye = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A c\u00E1pa t\u00E1mad\u00E1s es\u00E9ly\u00E9nek \u00E9rt\u00E9ke nem lehet kisebb 10-n\u00E9l, vagy nagyobb 100-n\u00E1l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}
					
					} else if (args[i].trim().equals("-cte") && ((i+1) < args.length)) {
					
						i++;
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 10) && (egeszSzam <= 100)) {
								
								capaTamadasEselye = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A c\u00E1pa t\u00E1mad\u00E1s es\u00E9ly\u00E9nek \u00E9rt\u00E9ke nem lehet kisebb 10-n\u00E9l, vagy nagyobb 100-n\u00E1l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}										
					}
					// --max-megjeleno-nyersanyag
					else if (args[i].trim().startsWith("--max-megjeleno-nyersanyag=") || args[i].trim().startsWith("--max-megjeleno-nyersanyag:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(27);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 4) && (egeszSzam <= 10)) {
								
								maxMegjelenotUjNyersanyag = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A legt\u00F6bb soronk\u00E9nt megjelen\u00F4 \u00FAj nyersanyagok sz\u00E1ma nem lehet kisebb 4-n\u00E9l, vagy nagyobb 10-n\u00E9l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}					
					
					} else if (args[i].trim().equals("--max-megjeleno-nyersanyag")) {
					
						i++;
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 4) && (egeszSzam <= 10)) {
								
								maxMegjelenotUjNyersanyag = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A legt\u00F6bb soronk\u00E9nt megjelen\u00F4 \u00FAj nyersanyagok sz\u00E1ma nem lehet kisebb 4-n\u00E9l, vagy nagyobb 10-n\u00E9l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}					
					
					} else if (args[i].trim().startsWith("-mmny=") || args[i].trim().startsWith("-mmny:")) {
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim().substring(6);
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 4) && (egeszSzam <= 10)) {
								
								maxMegjelenotUjNyersanyag = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A legt\u00F6bb soronk\u00E9nt megjelen\u00F4 \u00FAj nyersanyagok sz\u00E1ma nem lehet kisebb 4-n\u00E9l, vagy nagyobb 10-n\u00E9l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);	
							
							}						
						}
					
					} else if (args[i].trim().equals("-mmny")) {
					
						i++;
					
						cmdArgsProcessed = true;
					
						szamResz = args[i].trim();
						
						if (szamResz.matches("^\\d{1,3}$")) {
						
							egeszSzam = Integer.parseInt(szamResz);
							
							if ((egeszSzam >= 4) && (egeszSzam <= 10)) {
								
								maxMegjelenotUjNyersanyag = egeszSzam;
								
							} else {
								
								JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A legt\u00F6bb soronk\u00E9nt megjelen\u00F4 \u00FAj nyersanyagok sz\u00E1ma nem lehet kisebb 4-n\u00E9l, vagy nagyobb 10-n\u00E9l!", "Hiba", JOptionPane.OK_OPTION);
								System.exit(0);						
							
							}							
						}					
					
					}				
				}
				
				if (!cmdArgsProcessed) {

				if (args.length == 1) {
				
				    if (args[0].trim().equals("-h") || args[0].trim().equals("--help") || args[0].trim().equals("/?")) {
							
						JOptionPane.showMessageDialog(null, "Help describing optionally specifiable command line arguments:\n" +
								"Correct command syntax for launching the Raft v1 game:\njava Main [[--max-rows=EGESZ_SZAM_ERTEK[25,100] [--max-columns=EGESZ_SZAM_ERTEK[25,100]]\n [--capa-tamadas-eselye=EGESZ_SZAM_ERTEK[10,100]] --max-megjeleno-nyersanyag=EGESZ_SZAM_ERTEK[4,10]]", "Seg\u00EDts\u00E9g", JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);						
								System.exit(0);
					}					

					else if (args[0].trim().matches("^\\d{1,3}$")) {

						noOfRows = noOfColumns = Integer.parseInt(args[0]);
						
						if (noOfRows < 25 || noOfRows > 100) {
							
							JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok \u00E9s oszlopok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);

							System.exit(0);
							
						}
						
						if (noOfRows < 35) noOfColumns = 35;
						

					} else {

						JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok \u00E9s oszlopok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);

						System.exit(0);
					}

				} else if (args.length == 2) {

					if (args[0].trim().matches("^\\d{1,3}$")) {

						noOfRows = Integer.parseInt(args[0]);
						
						if (noOfRows < 25 || noOfRows > 100) {
							
							JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok \u00E9s oszlopok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);

							System.exit(0);
							
						}						
						

					} else {						
							
							JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok \u00E9s oszlopok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);

							System.exit(0);											

					}

					if (args[1].trim().matches("^\\d{1,3}$")) {

						noOfColumns = Integer.parseInt(args[1]);
						
						if (noOfColumns < 35 || noOfColumns > 100) {
						
							JOptionPane.showMessageDialog(null, "Hiba \u00FCzenet:\n" +
								"A sorok \u00E9s oszlopok sz\u00E1ma nem lehet kevesebb 25-n\u00E9l, vagy nagyobb 100-n\u00E1l\n" +
								"25 <= sorok sz\u00E1ma <= 100, 35 <= oszlopok sz\u00E1ma <= 100", "Hiba", JOptionPane.OK_OPTION);

							System.exit(0);						
						
						}

					} else {

						JOptionPane.showMessageDialog(null, "Error while parsing your command line argument!\n" +
								"You have specified an invalid number for the max. number of columns application property!\nAcceptable values for the maximum number of columns property:\n" +
								"25 <= number of rows <= 100, 35 <= number of columns <= 100", "Error", JOptionPane.OK_OPTION);

						System.exit(0);
					}
				  }
				
				}

				final Raft raftGame = new Raft(noOfRows, noOfColumns, capaTamadasEselye, maxMegjelenotUjNyersanyag);
				raftGame.startGame();
		        raftGame.setVisible(true);				
			
			}          

        });
    }

}
