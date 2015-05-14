import java.awt.*;

import javax.swing.*;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class Arrangementvindu extends JApplet {
	private static final long serialVersionUID = 1L;
	private JLabel placeholder;
	private JTextField navnFelt, beskFelt, prisFelt, refFelt,altFelt1,altFelt2,bildeNavnFelt;
	private JButton finnKnapp, slettKnapp, regKnapp, listeKnapp,bildeKnapp;
	private JTextArea tekstomr�de;
	private JScrollPane utskriftomr�de;
	private BorderLayout layout,centerLayout,centerPageStartLayout;
	private Container c;
	private GridLayout bottomGrid,topGrid,centerBot;
	public Kulturhus k;
	private JComboBox<String> lokalvelger,kontaktvelger;
	public Bildehandler bildehandler;
	private JCheckBox checkbox;
	private EmptyBorder border;
	private StretchIcon bildeIcon;
	private String[] lokalvalg,kontaktvalg;
	private BufferedImage bilde = null;
	private BufferedImage placeholder_img;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private String lokalnavn, kontaktnavn;
	private JComponent north,south,center,centerLineEnd,centerPageStart,centerPageStartTopPanel;
	private JLabel bildeLabel;
	private Kalenderpanel kalenderpanel;
	private Date dato;
	public Personregister preg;
	
	private String[] ekstraInputL() { // S�rger for � legge til nye unike oppf�ringer av lokaler.
		HashSet<String> a = new HashSet<>(Arrays.asList(k.lokalListe()));
		HashSet<String> b = new HashSet<>(Arrays.asList(lokalvalg));
		lokalvalg = k.lokalListe();
		a.removeAll(b);
		String[] ab = a.toArray(new String[a.size()]);
		return ab;
	}
	private String[] ekstraInputK() { // S�rger for � legge til nye unike oppf�ringer av lokaler.
		HashSet<String> a = new HashSet<>(Arrays.asList(preg.listKontaktpersoner()));
		HashSet<String> b = new HashSet<>(Arrays.asList(kontaktvalg));
		kontaktvalg = preg.listKontaktpersoner();
		a.removeAll(b);
		String[] ab = a.toArray(new String[a.size()]);
		return ab;
	}
	
	private void addSpecificC(String l) { // Maler opp spesifikke felt med beskrivende tekst, avhengig av type lokale man velger.
		
		Lokale lok = k.finnType(l);
		
		if (l.equalsIgnoreCase("oppdater lokal-liste")) {
			north.setLayout(new GridLayout(7, 1)); // 7 rows 2 columns; no gaps);
			
			for (String s : ekstraInputL()) {
				lokalvelger.addItem(s);
			}
			lokalvelger.revalidate();
			System.out.println(ekstraInputL());
			north.add(new JLabel(""));
			north.add(new JLabel("Lokal-liste er oppdatert!"));
		}
		else if (lok instanceof Cafe) {
			north.setLayout(new GridLayout(7, 2)); // 5 rows 2 columns; no gaps);
			north.add(new JLabel(" Hvor mange gjester er det plass til: "));
			north.add(altFelt1);
			north.add(new JLabel(" Velg dato og tidspunkt: "));
			north.add(kalenderpanel.makePanels());
		}
		else if (lok instanceof Konferanse) {
			north.setLayout(new GridLayout(8, 2)); // 6 rows 2 columns; no gaps);
			north.add(new JLabel(" Antall gjester det er plass til: "));
			north.add(altFelt1);
			north.add(new JLabel(" Hvilken type konferanse er det: "));
			north.add(altFelt2);
			north.add(new JLabel(" Velg dato og tidspunkt: "));
			north.add(kalenderpanel.makePanels());
		}
		else if (lok instanceof Selskapslokale) {
			north.setLayout(new GridLayout(7, 2)); // 5 rows 2 columns; no gaps);
			north.add(new JLabel(" Ytterligere info: "));
			north.add(altFelt1);
			north.add(new JLabel(" Velg dato og tidspunkt: "));
			north.add(kalenderpanel.makePanels());
		}
		else if (lok instanceof Scene) {
			north.setLayout(new GridLayout(7, 2)); // 5 rows 2 columns; no gaps);
			north.add(new JLabel(" Forestilling som skal holdes: "));
			north.add(altFelt1);
			north.add(new JLabel(" Velg dato og tidspunkt: "));
			north.add(kalenderpanel.makePanels());
		}
		else if (lok instanceof Kino) {
			north.setLayout(new GridLayout(7, 2)); // 5 rows 2 columns; no gaps);
			north.add(new JLabel(" Hvilken som skal vises: "));
			north.add(altFelt1);
			north.add(new JLabel(" Velg dato og tidspunkt: "));
			north.add(kalenderpanel.makePanels());
		}
		else {
			System.out.println("Aner ikke hvorfor du endte opp her");
		}
	}
	private void addSpecificK(String k) {
		
		if (k.equalsIgnoreCase("oppdater kontaktliste")) {
			north.setLayout(new GridLayout(7, 1)); // 7 rows 2 columns; no gaps);
			
			for (String s : ekstraInputK()) {
				kontaktvelger.addItem(s);
			}
			kontaktvelger.revalidate();
			north.add(new JLabel(""));
			north.add(new JLabel("Liste over kontaktpersoner er oppdatert!"));
		}
		
	}
	private void repainter() {	// Maler opp grunnelementer p� GUI ved behov ( removeAll() )	
		north.add(new JLabel(" Referansenummer:"));
		north.add(refFelt);
		north.add(new JLabel(" Navn p� arrangement:"));
		north.add(navnFelt);
		north.add(new JLabel(" Arrangementsbeskrivelse:"));
		north.add(beskFelt);
		north.add(new JLabel(" Velg type lokale:"));
		north.add(lokalvelger);
		north.add(new JLabel("Velg kontaktperson"));
		north.add(kontaktvelger);
	}
	public void setPlaceHolderImg() {
		try {
			placeholder_img = ImageIO.read(new File("./images/placeholder_img.png"));
			bildeIcon.setImage(placeholder_img);
			bildeLabel.repaint();
  	  } catch(Exception ex) {
		  tekstomr�de.setText("Noe gikk galt.");
	  }
	}
	public boolean slettFil(Arrangement a) {
    	try{
    		 
    		File file = new File(a.get_bildeSti());
 
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    			return true;
    		}else{
    			System.out.println("Delete operation is failed.");
    			return false;
    		}
 
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	public void clearFields() {
		navnFelt.setText("");
		beskFelt.setText("");
		prisFelt.setText("");
		refFelt.setText("");
		altFelt1.setText("");
		altFelt2.setText("");
		bildeNavnFelt.setText("");
		setPlaceHolderImg();
	}
	public void visBilde(Arrangement a) {
		try {
			File bildeFil = new File(a.get_bildeSti());
			bilde = ImageIO.read(bildeFil);
			bildeIcon.setImage(bilde);
			bildeNavnFelt.setText(bildeFil.getName());
			bildeLabel.repaint();
  	  } catch(Exception ex) {
		  tekstomr�de.setText("Noe gikk galt.");
	  }
	}
	public Arrangementvindu(Kulturhus kH, Personregister pr) {
		
			k = kH;
			preg = pr;
			lokalvalg = k.lokalListe();
			kontaktvalg = preg.listKontaktpersoner();
			kalenderpanel = new Kalenderpanel();
			
			lokalvelger = new JComboBox<String>(lokalvalg);
			kontaktvelger = new JComboBox<String>(kontaktvalg);

			navnFelt = new JTextField( 18 );
			beskFelt = new JTextField( 18 );
			checkbox = new JCheckBox("Er arrangementet betalbart?");
			prisFelt = new JTextField( 18 ); 
			refFelt = new JTextField( 18 );
			altFelt1 = new JTextField( 18 );
			altFelt2 = new JTextField( 18 );
			bildeNavnFelt = new JTextField( 18 );
			placeholder = new JLabel(" ");
			border = new EmptyBorder(5,5,5,5);
			
			bildeNavnFelt.setEditable(false);
			bildeNavnFelt.setForeground(Color.BLACK);
			bildeNavnFelt.setMargin(new Insets(10,10,10,10));
			
			finnKnapp = new JButton("Finn arrangement.");
			slettKnapp = new JButton( "Slett arrangement" );
			regKnapp = new JButton( "Registrer arrangement" );
			listeKnapp = new JButton( "List arrangement" );
			bildeKnapp = new JButton("Last inn bilde");
			
			//////////////////////////////////////////
			/////////// GUI LAYOUT START /////////////
			
			// DECLARATIONS START
			layout = new BorderLayout(5, 5);
			centerLayout = new BorderLayout(5,5);
			centerPageStartLayout = new BorderLayout(6,5);
			
			bottomGrid = new GridLayout(1, 4);
			topGrid = new GridLayout(5, 2);
			centerBot = new GridLayout(1,1);
			// DECLARATIONS END
			
			// TOP GRID START
			north = new JPanel();
			north.setLayout(topGrid);
			north.add(new JLabel(" Referansenummer:"));
			north.add(refFelt);
			north.add(new JLabel(" Navn:"));
			north.add(navnFelt);
			north.add(new JLabel(" Beskrivelse:"));
			north.add(beskFelt);
			north.add(new JLabel(" Velg type lokale:"));
			north.add(lokalvelger);
			north.add(new JLabel("Velg kontaktperson"));
			north.add(kontaktvelger);
			north.setBorder(border);
			// TOP GRID END
			
			// CENTER GRID START
			center = new JPanel();
			centerLineEnd = new JPanel();
			centerPageStartTopPanel = new JPanel();
			
			centerPageStart = new JPanel();
			center.setBorder(new EmptyBorder(0,0,5,5));
			
			center.setLayout(centerLayout);
			centerLineEnd.setLayout(centerBot);
			centerPageStart.setLayout(centerPageStartLayout);
			centerPageStartTopPanel.setLayout(new GridLayout(2,2));

      		bildeIcon = new StretchIcon("");
      		bildeLabel = new JLabel(bildeIcon);
      		centerPageStart.add(bildeLabel,BorderLayout.CENTER);

			centerPageStartTopPanel.add(bildeKnapp);
			centerPageStartTopPanel.add(bildeNavnFelt);

			tekstomr�de = new JTextArea();
			utskriftomr�de = new JScrollPane(tekstomr�de);
			tekstomr�de.setEditable(false);
			utskriftomr�de.setForeground(Color.BLACK);
			tekstomr�de.setMargin(new Insets(10,10,10,10));
			
			centerPageStart.add(centerPageStartTopPanel,BorderLayout.PAGE_START);
			
			center.add(utskriftomr�de, BorderLayout.CENTER);
			center.add(centerLineEnd, BorderLayout.PAGE_END);
			center.add(centerPageStart,BorderLayout.LINE_START);
			centerLineEnd.add(checkbox,placeholder);
			// CENTER GRID END
			
			//BOTTOM GRID START
			south = new JPanel();
			south.setLayout(bottomGrid);
			south.add(finnKnapp);
			south.add(slettKnapp);
			south.add(regKnapp);
			south.add(listeKnapp);
			// BOTTOM GRID END
			
			c = getContentPane();
			c.setLayout(layout);
			c.add(north, BorderLayout.PAGE_START);
			c.add(center, BorderLayout.CENTER);
			c.add(south, BorderLayout.PAGE_END);
			setPlaceHolderImg();
			
			/////////// GUI LAYOUT SLUTT /////////////
			//////////////////////////////////////////
				
			
	
			Knappelytter lytter = new Knappelytter();
			Typelytter tLytter = new Typelytter();
			
			finnKnapp.addActionListener( lytter );
			slettKnapp.addActionListener( lytter );
			regKnapp.addActionListener( lytter );
			lokalvelger.addActionListener( lytter );
			kontaktvelger.addActionListener( lytter );
			listeKnapp.addActionListener(lytter);
			bildeKnapp.addActionListener(lytter);			
			
			checkbox.addItemListener(tLytter);
			
			setSize( 550, 500 );
			setVisible( true );
		}
	
	
    private class Typelytter implements ItemListener
    {
      public void itemStateChanged( ItemEvent e )
      {
    	  if ( checkbox.isSelected() ) {
    		  try {
    	 		centerLineEnd.remove(placeholder);
    	 		centerLineEnd.revalidate();
    	 		centerLineEnd.repaint();
    	 		centerLineEnd.add(prisFelt);
    	 		centerLineEnd.revalidate();
    	 		centerLineEnd.repaint();
    		  } catch(Exception ex) {
    			  tekstomr�de.setText("Her oppsto det en feil gitt.");
    		  }
    	 } else {
    		 try {
     			centerLineEnd.remove(prisFelt);
     			centerLineEnd.revalidate();
     			centerLineEnd.repaint();
     			centerLineEnd.add(placeholder);
     			centerLineEnd.revalidate();
     			centerLineEnd.repaint();
     		  } catch(Exception ex) {
     			  tekstomr�de.setText("Her oppsto det en feil gitt.");
     		  }
    	 }
      }
    }
    
    
	private class Knappelytter implements ActionListener
	  {
	    public void actionPerformed( ActionEvent e )
	    {
	    	// Legger til arrangement
	      if ( e.getSource() == regKnapp ) {
	    	  try {
	    		  String navn = navnFelt.getText();
	    		  String besk = beskFelt.getText();
	    		  if (navn.equals("") || besk.equals("")) {
	    			  tekstomr�de.setText("Du m� fylle ut navn og beskrivelse.");
	    			  return;
	    		  }
	    		  dato = kalenderpanel.hentDato();
	    		  String lokNavn = (String) lokalvelger.getSelectedItem();
	    		  String kontaktNavn = (String) kontaktvelger.getSelectedItem();
	    		  Lokale lokale = k.finnType(lokNavn);
	    		  if (!lokNavn.equalsIgnoreCase("oppdater liste")){
	    			  if (!bildeNavnFelt.equals("")) {
	    				  String bildenavn = "./images/"+navn+"-"+"bilde.png";
	    				  try {
	    					    File outputfile = new File(bildenavn);
	    					    ImageIO.write(bilde, "png", outputfile);
	    			    		Kontaktperson kontakt = k.finnKontaktpersonViaNavn(kontaktNavn);
	    			    		Arrangement arr = new Arrangement(navn,kontakt,dato,besk,bildenavn);
	    			    		lokale.leggTilArrangement(arr);
	    			    		tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    			    		clearFields();
	    			    		setPlaceHolderImg();
	    			    		return;
	    					} catch (IOException ex) {
	    					    tekstomr�de.setText("Vi kunne ikke bruke dette bilde, noe gikk galt");
	    					}
	    			  }
			    	Kontaktperson kontakt = k.finnKontaktpersonViaNavn(kontaktNavn);
			    	Arrangement arr = new Arrangement(navn,kontakt);
			    	lokale.leggTilArrangement(arr);
			    	clearFields();
	    		  } else {
	    			  tekstomr�de.setText("Velg hvilket lokale det skal holdes p�!");
	    			  return;
	    		  }
	    		  
	    	  } catch (Exception ex) {
	    		  tekstomr�de.setText("Det oppsto en feil, vennligst pr�v p� nytt" + e.getClass());
	    	  }
	      }

	      else if ( e.getSource() == slettKnapp ) {
	    	  if (refFelt.getText().equals("")) {
	    		  tekstomr�de.setText("Du m� bruke referansenummer for � slette.");
	    		  
	    	  } else {
				try {
					int refNr = Integer.parseInt(refFelt.getText());
					Lokale lokFunnet = k.arrangementViaK(refNr);
					if (lokFunnet != null) {
						Object[] options = {"Ja",
						                    "Avbryt",};
						int n = JOptionPane.showOptionDialog(null,
						    "Er du sikker p� at du vil slette dette arrangement?",
						    "Advarsel",
						    JOptionPane.OK_CANCEL_OPTION,
						    JOptionPane.WARNING_MESSAGE,
						    null,
						    options,
						    options[0]);
						if (n == 1) {
							tekstomr�de.setText("Arrangementet ble ikke slettet");
							return;
						}
					}
					Arrangement arrFunnet = lokFunnet.finnArrangement(refNr);
					if (lokFunnet.slettArrangement(refNr)) {
						slettFil(arrFunnet);
						clearFields();
						setPlaceHolderImg();
						tekstomr�de.setText("Arrangement med navn "
								+ arrFunnet.get_Navn() + " og referanse "
								+ arrFunnet.get_aId()
								+ " er slettet fra kulturhuset.");
					} else {
						tekstomr�de
								.setText(" Kunne ikke slette arrangement nr. "
										+ refNr + " siden det har solgt billetter, eller ikke finnes. ");
					}
				} catch (Exception ex) {
					tekstomr�de.setText("En feil har oppst�tt, pr�v igjen.");
				}
	    	  }
	      }
	      else if ( e.getSource() == listeKnapp ) {
	    	  System.out.println("Knappen er trykket og jeg ber om k.listArrangementer.");
	    	  	tekstomr�de.setText(k.listArrangementerILokaler());
	      }
	      else if ( e.getSource() == finnKnapp ) {
	    	  try {
	    		  int refNr = Integer.parseInt(refFelt.getText());
					Lokale lokFunnet = k.arrangementViaK(refNr);
					if (lokFunnet != null) {
						Arrangement arrFunnet = lokFunnet.finnArrangement(refNr);
						tekstomr�de.setText(arrFunnet.toString());
		    			if (arrFunnet.get_bildeSti() != null)
		    				visBilde(arrFunnet);
					}
	    	  } catch(Exception ex) {
	    		  tekstomr�de.setText("Fant ikke Arrangement med dette referansenummer.");
	    	  }
	      }
	      else if ( e.getSource() == bildeKnapp ) {
	    	  try {
	      		bildehandler = new Bildehandler();
	      		File bildeFil = bildehandler.hentFil();
	      		bilde = ImageIO.read(bildeFil);
	      		bildeIcon.setImage(bilde);
	      		bildeNavnFelt.setText(bildeFil.getName());
	      		bildeLabel.repaint();
	      		return;
	    	  } catch(Exception ex) {
	    		  tekstomr�de.setText("Noe gikk galt.");
	    	  }
	      }
	      
		    int n = lokalvelger.getSelectedIndex();
		    lokalnavn = lokalvelger.getItemAt(n);
		    int n1 = kontaktvelger.getSelectedIndex();
		    kontaktnavn = kontaktvelger.getItemAt(n1);

		    
		    north.removeAll();
		    north.revalidate();
		    north.repaint();
		    repainter();
			c.add(north, BorderLayout.PAGE_START);
			
			if (e.getSource() == lokalvelger)
				addSpecificC(lokalnavn);
			else if (e.getSource() == kontaktvelger)
				addSpecificK(kontaktnavn);
			
			c.add(center, BorderLayout.CENTER);
			c.add(south, BorderLayout.PAGE_END);
		    c.repaint();
	    }
	  }
}
