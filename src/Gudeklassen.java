 import javax.swing.*;
import java.io.Serializable;

 public class Gudeklassen extends JPanel implements Serializable {

	private static final long serialVersionUID = 8441157301330300870L;
	Kulturhus k;
 	Personregister pr;
 	Lokalvindu lk;
 	Arrangementvindu av;
 	Kontaktvindu kv;
 
	public static void main(String[] args) {
		
 		Kulturhus k = new Kulturhus("Testehuset","This is fucked");
 		Personregister reg = new Personregister();
		
		Adminvindu admin = new Adminvindu(k,reg);
		admin.createAndShowGUI();
		
		Brukervindu bruker = new Brukervindu(k);
		bruker.createAndShowGUI();

	}

 }
