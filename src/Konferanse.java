import javax.swing.*;


/* 
 * Valg 3 
 * Subklasse av Lokale.java
 * 
 * */
public class Konferanse extends Lokale {
	
	private int antStoler, antBord, antPersoner; 			// Antall stoler, bord og personer
	private String typeArrangement; 						// Type arrangement
	private String typeRom;
	private int pris;
	private final static int MIN_LITEN = 1, MAX_LITEN = 50;	// Min og max personer for lite rom
	private final static int MIN_STOR = 50; // Min og max personer for stort rom
	
	
	public Konferanse(String n, String b, String tA, int aP) {
		super(n,b);
		antPersoner= aP;
		typeArrangement = tA;
		
		if(antPersoner >= MIN_LITEN && antPersoner <= MAX_LITEN) {
			antStoler = MAX_LITEN;
			antBord = 13;
			typeRom = "Lite konferanserom";
			pris = 1000;
		} else if (antPersoner >= MIN_STOR) {
			antStoler = 100;
			antBord = 25;
			typeRom = "Stort konferanserom";
			pris = 1999;
		} else {
			JOptionPane.showMessageDialog(null, "Du kan ikke ");
		}
	}
	
	
	 /*//////////////////////
	 Get og Set metoder start
	 *//////////////////////
	
	public int get_AntPersoner() {
		return antPersoner;
	}
	
	public String get_TypeArrangement() {
		return typeArrangement;
	}
	
	public int get_AntStoler() {
		return antStoler;
	}
	
	public int get_AntBord() {
		return antBord;
	}
	
	public String get_TypeRom() {
		return typeRom;
	}
	
	public int get_Pris() {
		return pris;
	}
	
	public void set_AntStoler(int aS) {
		antStoler = aS;
	}
	
	public void set_AntBord(int aB) {
		antBord = aB;
	}
	
	
	 /*//////////////////////
	 Get og Set metoder finish
	 *//////////////////////
	
	
	public String toString() {
		String meld = super.toString();
		meld += "Antall personer det er bestilt for: " + get_AntPersoner() + "\n";
		meld += "Type arrangement: " + get_TypeArrangement() + "\n";
		meld += "Type rom: " + get_TypeRom() + "\n";
		meld += "Antall stoler: " + get_AntStoler() + "\n";
		meld += "Antall bord: " + get_AntBord() + "\n";
		meld += "Pris per d�gn: " + get_Pris() + "\n";
		return meld;
	}
}
