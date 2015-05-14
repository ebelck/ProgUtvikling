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
	private JTextField navnFelt, beskFelt, prisFelt, refFelt,altFelt1,altFelt2,bildeNavnFelt,antallFelt;
	private JButton finnKnapp, slettKnapp, regKnapp, listeKnapp,bildeKnapp,oppdaterKnapp;
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
	private String lokalnavn, kontaktnavn;
	private JComponent north,south,center,centerLineEnd,centerPageStart,centerPageStartTopPanel;
	private JLabel bildeLabel,placeholder1,placeholder2;
	private Kalenderpanel kalenderpanel;
	private Date dato;
	public Personregister preg;
	private File bildeFil;
	private boolean betalbar = false;
	
	private void addSpecificC(String l) {
		contextPainter(lokalnavn);
		if (l.equalsIgnoreCase("oppdater liste")) {
			String[] lokalArray = k.lokalListe();
			if (lokalArray.length > 0) {
				lokalvelger.removeAllItems();
				for (int i = 0; i < lokalArray.length; i++) {
					lokalvelger.addItem(lokalArray[i]);
				}
			}
			lokalvelger.revalidate();
			tekstomr�de.append("\r\n********************************************\r\n"
					+  "Liste over lokaler er oppdatert\r\n");
		}
	}
	private void addSpecificK(String k) {
		contextPainter(lokalnavn);
		if (k.equalsIgnoreCase("oppdater liste")) {
		String[] kontaktArray = preg.listKontaktpersoner();
		if (kontaktArray.length > 0) {
			kontaktvelger.removeAllItems();
			for (int i = 0; i < kontaktArray.length; i++) {
				System.out.println(kontaktArray[i]);
				kontaktvelger.addItem(kontaktArray[i]);
				}
			}
			kontaktvelger.revalidate();
			tekstomr�de.append("\r\n********************************************\r\n"
							+  "Liste over kontaktpersoner er oppdatert\r\n");
		}
	}
			
	private void contextPainter(String l) {
		Lokale lok = k.finnType(l);
		if (lok instanceof Cafe) {
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
    		
    		String bildeSti = a.get_bildeSti();
    		if (bildeSti==null) {
    			return false;
    		}
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
			checkbox.setHorizontalAlignment(JTextField.CENTER);
			prisFelt = new JTextField("Pris");
			prisFelt.setHorizontalAlignment(JTextField.CENTER);
			antallFelt = new JTextField("Antall");
			antallFelt.setHorizontalAlignment(JTextField.CENTER);
			refFelt = new JTextField( 18 );
			altFelt1 = new JTextField( 18 );
			altFelt2 = new JTextField( 18 );
			bildeNavnFelt = new JTextField( 18 );
			placeholder1 = new JLabel(" ");
			placeholder2 = new JLabel(" ");
			border = new EmptyBorder(5,5,5,5);
			
			bildeNavnFelt.setEditable(false);
			bildeNavnFelt.setForeground(Color.BLACK);
			bildeNavnFelt.setMargin(new Insets(10,10,10,10));
			
			finnKnapp = new JButton("Finn arrangement.");
			slettKnapp = new JButton( "Slett arrangement" );
			regKnapp = new JButton( "Registrer arrangement" );
			listeKnapp = new JButton( "List arrangement" );
			bildeKnapp = new JButton("Last inn bilde");
			oppdaterKnapp = new JButton("Oppdater Arrangement");
			
			//////////////////////////////////////////
			/////////// GUI LAYOUT START /////////////
			
			// DECLARATIONS START
			layout = new BorderLayout(5, 5);
			centerLayout = new BorderLayout(5,5);
			centerPageStartLayout = new BorderLayout(6,5);
			
			bottomGrid = new GridLayout(1, 5);
			topGrid = new GridLayout(5, 2);
			centerBot = new GridLayout(1,3);
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
			centerLineEnd.add(checkbox);
			centerLineEnd.add(placeholder1);
			centerLineEnd.add(placeholder2);
			// CENTER GRID END
			
			//BOTTOM GRID START
			south = new JPanel();
			south.setLayout(bottomGrid);
			south.add(finnKnapp);
			south.add(slettKnapp);
			south.add(regKnapp);
			south.add(listeKnapp);
			south.add(oppdaterKnapp);
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
			oppdaterKnapp.addActionListener(lytter);
			
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
    			betalbar = true;
    	 		centerLineEnd.remove(placeholder1);
    	 		centerLineEnd.remove(placeholder2);
    	 		centerLineEnd.revalidate();
    	 		centerLineEnd.repaint();
    	 		centerLineEnd.add(antallFelt);
    	 		centerLineEnd.add(prisFelt);
    	 		centerLineEnd.revalidate();
    	 		centerLineEnd.repaint();
    		  } catch(Exception ex) {
    			  tekstomr�de.setText("Her oppsto det en feil gitt.");
    		  }
    	 } else {
    		 try {
    			betalbar = false;
     			centerLineEnd.remove(prisFelt);
     			centerLineEnd.remove(antallFelt);
     			centerLineEnd.revalidate();
     			centerLineEnd.repaint();
     			centerLineEnd.add(placeholder1);
     			centerLineEnd.add(placeholder2);
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
	    		  if (navn.equals("")) {
	    			  tekstomr�de.setText("Du m� gi arrangementet et navn.");
	    			  return;
	    		  }
	    		  dato = kalenderpanel.hentDato();
	    		  String lokNavn = (String) lokalvelger.getSelectedItem();
	    		  String kontaktNavn = (String) kontaktvelger.getSelectedItem();
	    		  Lokale lokale = k.finnType(lokNavn);
	    		  
	    		  if (!lokNavn.equalsIgnoreCase("oppdater liste")){
	    			  System.out.println("* Starter p� alle if-setningene for � legge til Arrangement.\r\n");
	    			  if (bilde != null) {
	    				  System.out.println("Bilde-objektet var ikke tomt, s� vi er der n�.");
	    				  String bildenavn = "./images/"+navn+"-"+"bilde.png";
	    				  try {
	    					    File outputfile = new File(bildenavn);
	    					    ImageIO.write(bilde, "png", outputfile);
	    			    		Kontaktperson kontakt = preg.finnKontaktpersonViaNavn(kontaktNavn);
	    		    			  if (besk.equals("") && betalbar==false) {
	    		    				  	Arrangement arr = new Arrangement(navn,bildenavn,kontakt,dato);
	    		    				  	lokale.leggTilArrangement(arr);
	    		    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    		    				  	clearFields();
	    		    			  }
	    		    			  if (!besk.equals("") && betalbar==false) {
	    		    				  	Arrangement arr = new Arrangement(navn,kontakt,dato,besk,bildenavn);
	    		    				  	lokale.leggTilArrangement(arr);
	    		    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    		    				  	clearFields();
	    		    			  }
	    		    			  if (besk.equals("") && betalbar==true) {
	    		    				  	int pris = Integer.parseInt(prisFelt.getText());
	    		    				  	int antall = Integer.parseInt(antallFelt.getText());
	    		    				  	Arrangement arr = new Arrangement(navn,kontakt,dato,pris,antall,bildenavn);
	    		    				  	lokale.leggTilArrangement(arr);
	    		    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    		    				  	clearFields();
	    		    			  }
	    		    			  if (!besk.equals("") && betalbar==true) {
	    		    				  	int pris = Integer.parseInt(prisFelt.getText());
	    		    				  	int antall = Integer.parseInt(antallFelt.getText());
	    		    				  	Arrangement arr = new Arrangement(navn,kontakt,dato,besk,pris,antall,bildenavn);
	    		    				  	lokale.leggTilArrangement(arr);
	    		    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    		    				  	clearFields();
	    		    			  }
	    			    		return;
	    					} catch (IOException ex) {
	    					    tekstomr�de.setText("Vi kunne ikke bruke dette bilde, noe gikk galt");
	    					}
	    			  }
	    			  System.out.println("Utenfor bilde-if'en.");
	    			  if (besk.equals("") && betalbar==false) {
	    				  System.out.println("besk, betalbar false");
	    				  	Kontaktperson kontakt = preg.finnKontaktpersonViaNavn(kontaktNavn);
	    				  	Arrangement arr = new Arrangement(navn,kontakt,dato);
	    				  	lokale.leggTilArrangement(arr);
	    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    				  	clearFields();
	    			  }
	    			  if (!besk.equals("") && betalbar==false) {
	    				  System.out.println("ingen besk, betalbar false");
	    				  	Kontaktperson kontakt = preg.finnKontaktpersonViaNavn(kontaktNavn);
	    				  	Arrangement arr = new Arrangement(navn,kontakt,dato,besk);
	    				  	lokale.leggTilArrangement(arr);
	    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    				  	clearFields();
	    			  }
	    			  if (besk.equals("") && betalbar==true) {
	    				  System.out.println("besk, betalbar true");
	    				  	int pris = Integer.parseInt(prisFelt.getText());
	    				  	int antall = Integer.parseInt(antallFelt.getText());
	    				  	Kontaktperson kontakt = preg.finnKontaktpersonViaNavn(kontaktNavn);
	    				  	Arrangement arr = new Arrangement(navn,kontakt,dato,pris,antall);
	    				  	lokale.leggTilArrangement(arr);
	    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    				  	clearFields();
	    			  }
	    			  if (!besk.equals("") && betalbar==true) {
	    				  System.out.println("ingen besk, betalbar true");
	    				  	int pris = Integer.parseInt(prisFelt.getText());
	    				  	int antall = Integer.parseInt(antallFelt.getText());
	    				  	Kontaktperson kontakt = preg.finnKontaktpersonViaNavn(kontaktNavn);
	    				  	Arrangement arr = new Arrangement(navn,kontakt,dato,besk,pris,antall);
	    				  	lokale.leggTilArrangement(arr);
	    				  	tekstomr�de.setText("Arrangementet ble opprettet!\r\nLykke til med " + arr.get_Navn());
	    				  	clearFields();
	    			  }
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
	      		bildeFil = bildehandler.hentFil();
	      		bilde = ImageIO.read(bildeFil);
	      		bildeIcon.setImage(bilde);
	      		bildeNavnFelt.setText(bildeFil.getName());
	      		bildeLabel.repaint();
	      		return;
	    	  } catch(Exception ex) {
	    		  tekstomr�de.setText("Noe gikk galt.");
	    	  }
	      }
	      else if ( e.getSource() == oppdaterKnapp ) {
	    	  if (refFelt.getText().equals("")) {
	    		  tekstomr�de.setText("Du m� bruke referansenummer for � oppdatere ett arrangement");
	    		  return;
	    	  }
	    	  String kontaktNavn = (String) kontaktvelger.getSelectedItem();
	    	  String navn = navnFelt.getText();
	    	  String besk = beskFelt.getText();
	    	  String alt = altFelt1.getText();
	    	  String alt2 = altFelt2.getText();
	    	  String bildeB = bildeNavnFelt.getText();
	    	  int refNr = Integer.parseInt(refFelt.getText());
	    	  Lokale lokFunnet = k.arrangementViaK(refNr);
			  Kontaktperson kontakt = preg.finnKontaktpersonViaNavn(kontaktNavn);
	    	  Arrangement arrFunnet = lokFunnet.finnArrangement(refNr);
	    	  if (!kontakt.equals(arrFunnet.get_Kontaktperson()))
	    		  arrFunnet.set_Kontaktperson(kontakt);
	    	  if (!arrFunnet.get_Billettsalg() && betalbar==true)
	    		  arrFunnet.bliBetalbar(Integer.parseInt(prisFelt.getText()), Integer.parseInt(antallFelt.getText()));
	    	  if (arrFunnet.get_Billettsalg() && !prisFelt.getText().equals(""))
	    		  arrFunnet.set_Pris(Integer.parseInt(prisFelt.getText()));
	    	  if (!navn.equals(""))
	    		  arrFunnet.set_Navn(navn);
	    	  if (!besk.equals(""))
	    		  arrFunnet.set_Beskrivelse(besk);
	    	  if (!alt.equals(""))
	    		  arrFunnet.set_Info(alt);
	    	  if (!alt2.equals(""))
	    		  arrFunnet.set_Info2(alt2);
	    	  if (!bildeB.equals("")) {
					if (arrFunnet != null) {
						Object[] options = {"Ja",
						                    "Nei",};
						int n = JOptionPane.showOptionDialog(null,
						    "Vil du erstatte bildet med et nytt?",
						    "Hei bruker.",
						    JOptionPane.OK_CANCEL_OPTION,
						    JOptionPane.INFORMATION_MESSAGE,
						    null,
						    options,
						    options[0]);
						if (n == 1) {
							tekstomr�de.setText("* Bildet ble ikke erstattet *\r\n");
							tekstomr�de.append(arrFunnet.toString());
			    	    	clearFields();
							return;
						}
						slettFil(arrFunnet);
						String bildenavn = "./images/"+arrFunnet.get_Navn()+"-"+"bilde.png";
	    				File outputfile = new File(bildenavn);
	    	    		arrFunnet.set_Bildesti(bildenavn);
	    				try {
							ImageIO.write(bilde, "png", outputfile);
						} catch (IOException e1) {
							e1.printStackTrace();
						};
					}
	    	  }

	    	  tekstomr�de.setText(arrFunnet.toString());
	    	  clearFields();
	    	  setPlaceHolderImg();
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
			if (e.getSource() == kontaktvelger)
				addSpecificK(kontaktnavn);	
			c.add(center, BorderLayout.CENTER);
			c.add(south, BorderLayout.PAGE_END);
		    c.repaint();
	    }
	  }
}
