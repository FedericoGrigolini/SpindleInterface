package view;
import java.io.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import Controller.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FileChooserUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Controller.TerrainTuple;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;    


public class Vista {
	Random r= new Random(); 
    JFrame f;    
    LinkedList<TerrainTuple> listO=new LinkedList<TerrainTuple>();
    LinkedList<Sea> listO2=new LinkedList<Sea>();
    LinkedList<TerrainTuple> listE=new LinkedList<TerrainTuple>();
    LinkedList<Sea> listE2=new LinkedList<Sea>();
    LinkedList<TerrainTuple> listG=new LinkedList<TerrainTuple>();
    LinkedList<Sea> listG2=new LinkedList<Sea>();
    boolean OfromFile=false;
    
    String path = "src/Output.txt";
    String pathT = "src/Spindle.dfl";
    boolean appendToFile = false;
    JFileChooser fc;
    String modello="";
    String superiority = "e";
    
    int gTime = 0;
    
    Vista() {    
	    f=new JFrame();    
	    f.setResizable(false);
	    String data[][]={ {"","","","","",""},    
	                          {"","","","","",""},    
	                          {"","","","","",""}};    
	    String column[]={"Time","Place","Cloudness", "Wind", "Directrion"};
	    String column2[] = {"Time", "cm"};
	    DefaultTableModel  table=new DefaultTableModel (data,column);
	    DefaultTableModel table1 = new DefaultTableModel(data, column2);
	    f.getContentPane().setLayout(null);
	    
	    JComboBox comboBox = new JComboBox();
	    comboBox.setBounds(12, 23, 183, 22);
	    comboBox.setModel(new DefaultComboBoxModel(new String[] {"Modello", "O"}));
	    f.getContentPane().add(comboBox);
	    
	    JButton addRow = new JButton("Aggiungi riga");
	    addRow.setBounds(12, 97, 183, 22);
	    addRow.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        table.addRow(new Object[] {});
	        table1.addRow(new Object[] {});
	      }
	    });
	    f.getContentPane().add(addRow);
	    
	    JLabel label = new JLabel("");
	    label.setBounds(0, 230, 885, 114);
	    f.getContentPane().add(label);
	    
	    JLabel label_1 = new JLabel("");
	    label_1.setBounds(0, 458, 885, 114);
	    f.getContentPane().add(label_1);
	    
	    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	    tabbedPane.setBounds(12, 132, 873, 419);
	    f.getContentPane().add(tabbedPane);
	    JTable jt = new JTable(table);
	    JTable jt2 = new JTable(table1);
	    jt.setBounds(30,40,200,300);          
	    JScrollPane sp=new JScrollPane(jt);
	    tabbedPane.addTab("Terrain", null, sp, null);
	    
	    JScrollPane scrollPane = new JScrollPane(jt2);
	    tabbedPane.addTab("Sea", null, scrollPane, null);
	    
	    
	    JButton btnGenera = new JButton("Genera"); 
	    btnGenera.setBounds(761, 548, 97, 25);
	    btnGenera.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        //lettura delle tabelle e popolamento delle linkedlist, contenenti tutti i paramentri necessari.
	    	  
	    	  if(jt.isEnabled() && jt2.isEnabled()) {
	    		  if (comboBox.getSelectedItem().toString() != "Modello") {
		    	  	  TableModel dtm = jt.getModel();
			    	  int nRow = dtm.getRowCount();
			    	  //Prendiamo le tuple dalla prima tabella
			    	  for (int i = 0 ; i < nRow ; i++) { 
			    		  if (!(dtm.getValueAt(i, 1) == "")) {
				    		  listO.add(new TerrainTuple((String)dtm.getValueAt(i,0),
				    				  (String)dtm.getValueAt(i,1), 
				    				  (String)dtm.getValueAt(i,2),
				    				  (String)dtm.getValueAt(i,3),
				    				  (String)dtm.getValueAt(i,4))
				    				  );
			    		  }
			    	  }
			    	  
			    	  dtm = jt2.getModel();
			    	  nRow = dtm.getRowCount();
			    	  
			    	  //prendiamo i valori dalla seconda tabella
			    	  for (int i = 0; i < nRow; i++) { 
			    		  if (!(dtm.getValueAt(i, 1) == ""))
				    		  listO2.add(new Sea((String)dtm.getValueAt(i, 0),
				    				  	(String)dtm.getValueAt(i, 1))
				    				  	);
			    	  }
		    	  //Chiamiamo il metodo converter per convertire le tuple in testo
						converter(comboBox.getSelectedItem().toString(), "" + gTime);
						translate(comboBox.getSelectedItem().toString(), "" + gTime, listO, listO2);
						ruleCombiner(""+gTime);
						JOptionPane.showMessageDialog(null, "Generazione Completata!");
		    	  } else JOptionPane.showMessageDialog(null, "Seleziona Modello valido");  
	    	  }else {
	    		  converter(comboBox.getSelectedItem().toString(), "" + gTime);
	    		  translate(comboBox.getSelectedItem().toString(), "" + gTime, listO, listO2);
	    		  ruleCombiner(""+gTime);
	    		  JOptionPane.showMessageDialog(null, "Generazione Completata!");
	    	  }
	    	  
	    	  
	      }
	    });
	    f.getContentPane().add(btnGenera);
	    
	    JButton btnReset = new JButton("Reset");
	    btnReset.setBounds(645, 547, 97, 25);
	    f.getContentPane().add(btnReset);
	    
	    JButton minus = new JButton("-");
	    minus.setBounds(247, 23, 51, 23);
	    f.getContentPane().add(minus);
	    
	    JTextPane globalTime = new JTextPane();
	    globalTime.setText("0");
	    globalTime.setBounds(310, 25, 51, 20);
	    f.getContentPane().add(globalTime);
	    
	    JButton peter = new JButton("+");
	    peter.setBounds(371, 23, 51, 23);
	    f.getContentPane().add(peter);
	    
	    JButton btnCaricaE = new JButton("Carica Mesoscala");
	    btnCaricaE.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    btnCaricaE.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent arg0) {
	    	//###################################################################################################################################
	    	// CARICA MODELLO E
	    		
    			fc= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    			int returnVal = fc.showOpenDialog(null);

    	        if (returnVal == JFileChooser.APPROVE_OPTION) {
    	            File file = fc.getSelectedFile();
    	            //This is where a real application would open the file.
    	            ModelReader mr=new ModelReader(file.getAbsolutePath(), "E");
    	            mr.getValues("t0", globalTime.getText());
    	            modello+=mr.testo;
    	            for (TerrainTuple s : mr.list) {
						listE.add(s);
					}
    	            for (Sea s : mr.list2) {
						listE2.add(s);
					}
    	            translate("E", "" + gTime, listE, listE2);
    	        } 
	    	}
	    });
	    btnCaricaE.setBounds(448, 22, 131, 25);
	    f.getContentPane().add(btnCaricaE);
	    
	    JButton btnCaricaG = new JButton("Carica Globale");
	    btnCaricaG.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
    		//###################################################################################################################################
	    	// CARICA MODELLO G
	    		
    			fc= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    			int returnVal = fc.showOpenDialog(null);

    	        if (returnVal == JFileChooser.APPROVE_OPTION) {
    	            File file = fc.getSelectedFile();
    	            //This is where a real application would open the file.
    	            ModelReader mr=new ModelReader(file.getAbsolutePath(), "G");
    	            mr.getValues("t0", globalTime.getText());
    	            modello+=mr.testo;
    	            for (TerrainTuple s : mr.list) {
						listG.add(s);
					}
    	            for (Sea s : mr.list2) {
						listG2.add(s);
					}
    	            translate("G", "" + gTime, listG, listG2);
    	        }
	    		
	    	}
	    });
	    btnCaricaG.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    	}
	    });
	    btnCaricaG.setBounds(587, 22, 131, 25);
	    f.getContentPane().add(btnCaricaG);
	    
	    JRadioButton rdbtnE = new JRadioButton("Mesoscala");
	    
	    rdbtnE.setSelected(true);
	    rdbtnE.setBounds(515, 63, 102, 23);
	    f.getContentPane().add(rdbtnE);
	    
	    JRadioButton rdbtnG = new JRadioButton("Globale");
	    rdbtnG.setBounds(621, 63, 97, 23);
	    f.getContentPane().add(rdbtnG);
	    
	    rdbtnE.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		rdbtnE.setSelected(true);
	    		superiority = "e";
	    		if (rdbtnE.isSelected()) {
	    			rdbtnG.setSelected(false);
	    		}
	    	}
	    });
	    rdbtnG.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		rdbtnG.setSelected(true);
	    		superiority = "g";
	    		if (rdbtnG.isSelected()) {
	    			rdbtnE.setSelected(false);
	    		}
	    	}
	    });
	    
	    JLabel lblNewLabel = new JLabel("Superiorit\u00E0");
	    lblNewLabel.setBounds(420, 67, 74, 14);
	    f.getContentPane().add(lblNewLabel);
	    
	    JButton btnCaricaDatiLocali = new JButton("Carica Dati Locali");
	    btnCaricaDatiLocali.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent arg0) {
	    		//###################################################################################################################################
		    	// CARICA MODELLO O DA FILE
		    		
    			fc= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

    			int returnVal = fc.showOpenDialog(null);
    			
    	        if (returnVal == JFileChooser.APPROVE_OPTION) {
    	            jt.setEnabled(false);
    	            jt2.setEnabled(false);
    	        	File file = fc.getSelectedFile();
    	            //This is where a real application would open the file.
    	            ModelReader mr=new ModelReader(file.getAbsolutePath(), "O");
    	            mr.getValues("t0", globalTime.getText());
    	            modello+=mr.testo;
    	            for (TerrainTuple s : mr.list) {
						listO.add(s);
					}
    	            for (Sea s : mr.list2) {
						listO2.add(s);
					}
    	            translate("O", "" + gTime, listO, listO2);
    	        }
	    	}
	    });
	    btnCaricaDatiLocali.setBounds(727, 22, 131, 25);
	    f.getContentPane().add(btnCaricaDatiLocali);
	    f.setSize(903,621);    
	    f.setVisible(true);  
	    
	    
	    peter.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gTime++;
				globalTime.setText("" + gTime);
			}
		});
	    minus.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (gTime > 0) {
					gTime--;
					globalTime.setText("" + gTime);
				}
			}
		});
	    //Aggungiungiamo un actionlistener su un tasto calcola
	    //prende tutti gli elementi delle tabelle e va a creare una lista di tuple (terrain e sea)
    
	} 
    
    public void ruleCombiner(String time) {
    	try {
    		FileWriter write = new FileWriter(pathT, true);
			PrintWriter printLine = new PrintWriter(write);
    		String textLine="";
    		String textSup = "";
        	String tmp1 = "";
        	String tmp2 = "";
        	int indexT = 1;
        	int valueA = 1, valueB=1;
        	for (TerrainTuple e: listE) {
        		for (TerrainTuple g : listG) {
        			if	(!e.cloudness.equals("") && !g.cloudness.equals("") ) {
        				if(e.compareTo(g)==1 && !e.cloudness.equals(g.cloudness)) {
        					//Gestire generazione casuale. Di norma G è più grande di E.
        					tmp1 = "C" + g.place+"t" + g.time 
        							+ (((Integer.parseInt(g.cloudness) + Integer.parseInt(e.cloudness)) / 2) + 2);
        					tmp2 = "C" + e.place+"t" + e.time 
        							+ (((Integer.parseInt(g.cloudness) + Integer.parseInt(e.cloudness)) / 2) - 2);
        					
    		    	    	String formulaA = "rcg" + (indexT) + returnNumber(e.place);
    		    	    	String formulaB = "rce" + (indexT + 1) + returnNumber(e.place);
    		    	    	indexT += 2; 
    		    	    	
        					textLine += formulaA + " : C" + returnPlace(e.place) + "et" + e.time + e.cloudness + ", "
        							+ "C" + returnPlace(g.place) + "gt" + g.time + g.cloudness + " => " + tmp1 + "\n";
        					
        					textLine += formulaB + " : C" + returnPlace(e.place) + "et" + e.time + e.cloudness + ", "
        							+ "C" + returnPlace(g.place) + "gt" + g.time + g.cloudness + " => " + tmp2 + "\n";
        					
        					//modificare se possiamo mettere modello o superiori a tempo 0
        					String formulaAridotta = "rc" + (indexT) + returnNumber(e.place);
        					String formulaBridotta = "rc" + (indexT+1) + returnNumber(e.place);
        					
        					textLine += formulaAridotta + " : " + tmp1 + " => -" + tmp2 + "\n";
        					textLine += formulaBridotta + " : " + tmp2 + " => -" + tmp1 + "\n";
        					
        					
        					if (superiority.equals("g")) {
        						textSup += formulaAridotta + " > " + formulaB + "\n";
        						textSup += formulaA + " > " + formulaBridotta + "\n";
        					} else {
        						textSup += formulaBridotta + " > " + formulaA + "\n";
        						textSup += formulaB + " > " + formulaAridotta + "\n";
        					}
        					if(valueA < 7) {
        						valueA++;
        					}
        					else {
        						valueA = 1;
        						valueB = valueB + 2;
        					}
        				}
        				if(e.compareTo(g)==1 && !e.wind.equals(g.wind)) {
        					//Gestire generazione casuale. Di norma G è più grande di E.
        					
        					tmp1 = "W" + g.place+"t" + g.time + g.direction + g.wind;
        					tmp2 = "W" + e.place+"t" + e.time + e.direction + e.wind;
        					
        					String formulaA = "rwg" + (indexT) + returnNumber(e.place);
        					String formulaB = "rwe" + (indexT) + returnNumber(e.place);
        					
        					textLine += formulaA + " : W" + returnPlace(e.place) + "et" + e.time + e.direction + e.wind + ", "
        							+ "W" + returnPlace(g.place) + "gt" + g.time + g.direction + g.wind + " => " + tmp1 + "\n";
    		    	    	
        					textLine += formulaB + " : W" + returnPlace(e.place) + "et" + e.time + e.direction + e.wind + ", "
        							+ "W" + returnPlace(g.place) + "gt" + g.time + g.direction + g.wind + " => " + tmp2 + "\n";
        					
        					String formulaAridotta = "rw" + (indexT) + returnNumber(e.place);
        					String formulaBridotta = "rw" +  (indexT + 1) + returnNumber(e.place);
    		    	    	indexT += 2; 
        					
        					textLine += formulaAridotta + " : " + tmp1 + " => -" + tmp2 + "\n";
        					textLine += formulaBridotta + " : " + tmp2 + " => -" + tmp1 + "\n";
        					
        					if (superiority.equals("g")) {
        						textSup +=  formulaAridotta + " > " + formulaB + "\n";
        						textSup +=  formulaA + " > " + formulaBridotta + "\n";
        					} else {
        						textSup += formulaBridotta + " > " + formulaA + "\n";
        						textSup += formulaB + " > " + formulaAridotta + "\n";
        					}
        					if(valueA < 7) {
        						valueA++;
        					}
        					else {
        						valueA = 1;
        						valueB = valueB + 2;
        					}
        				}
        			}
        		}
        	}
        	
        	for (Sea e: listE2) {
        		for (Sea g: listG2) {
        			if	(!e.value.equals("") && !g.value.equals("") ) {
        				if (e.time.equals(g.time) && !e.value.equals(g.value)) {
        					//stesso controllo sul random che dobbiamo fare sopra  va fatto qui
        					tmp1 =  "Seat" + g.time
        							+ (((Integer.parseInt(g.value) + Integer.parseInt(e.value)) / 2) + 2);
    		    	    	tmp2 = "Seat" + e.time
        							+ (((Integer.parseInt(g.value) + Integer.parseInt(e.value)) / 2) - 2);
        					
    		    	    	String formulaA = "rsg" + (indexT) + "1";
    		    	    	String formulaB = "rse" + (indexT + 1) + "1";
    		    	    	indexT += 2; 
    		    	    	
    		    	    	textLine += formulaA + " : Seaet" + e.time + e.value + ", "
        							+ "Seagt" + g.time + g.value + " => " + tmp1 + "\n";
        					textLine += formulaB + " : Seaet" + e.time + e.value + ", "
        							+ "Seagt" + g.time + g.value + " => " + tmp2 + "\n";
        					
        					String formulaAridotta = "vs" + (indexT) + "1";
        					String formulaBridotta = "vs" + (indexT + 1) + "1";
        					
        			    	textLine += formulaAridotta + " : " + tmp1 + " => -" + tmp2 + "\n";
        			    	textLine += formulaBridotta + " : " + tmp2 + " => -" + tmp1 + "\n";
        			    	
        			    	if (superiority.equals("g")) {
        						textSup +=  formulaAridotta + " > " + formulaB + "\n";
        						textSup +=  formulaA + " > " + formulaBridotta + "\n";
        					} else {
        						textSup +=  formulaBridotta + " > " + formulaA + "\n";
        						textSup +=  formulaB + " > " + formulaAridotta + "\n";
        					}
        					if(valueA < 7) {
        						valueA++;
        					}
        					else {
        						valueA = 1;
        						valueB = valueB + 2;
        					}
        				}
        			}
        		}

        	}
        	printLine.printf("%s" + "%n", textLine);
        	printLine.printf("%s" + "%n", textSup);
	    	
	    	printLine.close();
	    	textLine = "";
        	
		} catch (IOException e) {
			//Errore nella creazione del file
			e.printStackTrace();
		}
    	
    }
    
    public String returnNumber(String s) {
    	if (s.equalsIgnoreCase("North"))
    		return "1";
    	if (s.equalsIgnoreCase("Center"))
    		return "2";
    	if (s.equalsIgnoreCase("South"))
    		return "3";
    	return "0";
    }
    
    public String returnPlace(String s) {
    	
    	if (s.equals("North"))
    		return "N";
    	if (s.equals("Center"))
    		return "C";
    	return "S";
    }
    
    public void translate(String s, String t, LinkedList<TerrainTuple> list, LinkedList<Sea> list2) {
    	//<G, t0> : C(North, t0, 90) 
    	//rfcg11 : => CNgt090
    	//rwg11 : => WNgt0N18
    	if (!list.isEmpty() && !list2.isEmpty()) {
    		
	    	try {
				FileWriter write = new FileWriter(pathT, true);
				PrintWriter printLine = new PrintWriter(write);
				String textLine = "";
				
				for (int i = 0; i < list.size(); i++) {
		    	    TerrainTuple tmp = list.get(i);
		    	    //<G,to>:C(North, t0, 90)
		    	    //<G,to>:W(North, t0, [N, 18])
		    	    if (tmp.place != "") {
		    	    	int time = Integer.parseInt(tmp.time) + 1;
		    	    	if (s.equals("O")) {
		    	    		textLine += "rc" + s.toLowerCase() + (time) + returnNumber(tmp.place) + " : -> C" + returnPlace(tmp.place) + s.toLowerCase() + "t" + tmp.time + tmp.cloudness + "\n";
			    	    	textLine += "rw" + s.toLowerCase() + (time) + returnNumber(tmp.place) + " : -> W"+ returnPlace(tmp.place) + s.toLowerCase() + "t" + tmp.time + tmp.direction + tmp.wind + "\n";
		    	    	} else {
				    	    //textLine += "<" + s + ", " + t + ">:W(" + tmp.place + ", " + tmp.time + ", [" + tmp.direction + ", " + tmp.wind +"])\n";
			    	    	textLine += "rfc" + s.toLowerCase() + (time) + returnNumber(tmp.place) + " : => C" + returnPlace(tmp.place) + s.toLowerCase() + "t" + tmp.time + tmp.cloudness + "\n";
			    	    	textLine += "rfw" + s.toLowerCase() + (time) + returnNumber(tmp.place) + " : => W"+ returnPlace(tmp.place) + s.toLowerCase() + "t" + tmp.time + tmp.direction + tmp.wind + "\n";
		    	    	}
		    	    }
		    	}
		    	
		    	for (int i = 0; i < list2.size(); i++) {
		    	    Sea tmp = list2.get(i);
		    	    //<G,to>:S(Sea, t0, 90)
	
		    	    if (tmp.value != "") {    	    	
		    	    	int time = Integer.parseInt(tmp.time) + 1;
		    	    	if (s.equals("O"))
		    	    		textLine += "rs" + s.toLowerCase() + (time) + "1 : -> Sea" + s.toLowerCase() + "t" + tmp.time + tmp.value + "\n";
		    	    	else 
		    	    		textLine += "rs" + s.toLowerCase() + (time) + "1 : => Sea" + s.toLowerCase() + "t" + tmp.time + tmp.value + "\n";
		    	    }
		    	}
	
	    	    printLine.printf("%s" + "%n", textLine);
		    	
		    	printLine.close();
		    	textLine = "";
		    	
			} catch (IOException e) {
				//Errore nella creazione del file
				e.printStackTrace();
			}

    	}
    }
    
    public void converter(String s, String t) {
    	try {
			FileWriter write = new FileWriter(path, true);
			PrintWriter printLine = new PrintWriter(write);
			String textLine = "";
			
	    	for (int i = 0; i < listO.size(); i++) {
	    	    TerrainTuple tmp = listO.get(i);
	    	    //<G,to>:C(North, t0, 90)
	    	    //<G,to>:W(North, t0, [N, 18])
	    	    if (tmp.place != "") {
		    	    textLine += "<" + s + ", " + t + ">:C(" + tmp.place + ", " + tmp.time + ", " + tmp.cloudness + ")\n";
		    	    textLine += "<" + s + ", " + t + ">:W(" + tmp.place + ", " + tmp.time + ", [" + tmp.direction + ", " + tmp.wind +"])\n";
	    	    }
	    	}
	    	
	    	for (int i = 0; i < listO2.size(); i++) {
	    	    Sea tmp = listO2.get(i);
	    	    //<G,to>:S(Sea, t0, 90)
	    	    if (tmp.value != "") {
	    	    	textLine += "<" + s + ", " + t + ">:S(Sea, " + tmp.time + ", " + tmp.value + ")\n";
	    	    }
	    	}
	    	
    	    printLine.printf("%s" + "%n", textLine);
	    	
    	    printLine.println(modello);
    	    
	    	printLine.close();
	    	textLine = "";
	    	
		} catch (IOException e) {
			//Errore nella creazione del file
			e.printStackTrace();
		}
   
    }
	    
	public static void main(String[] args) {    
	    new Vista();    
	}    
}  