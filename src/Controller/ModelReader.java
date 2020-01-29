package Controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class ModelReader {
	
	public String testo;
	public LinkedList<TerrainTuple> list=new LinkedList<TerrainTuple>();
    public LinkedList<Sea> list2=new LinkedList<Sea>();
	FileReader f;
	public BufferedReader b;
	String model;
	
	public ModelReader(String file, String model) {
		testo = "";
	    try {		
	    	f=new FileReader(file);
		    b=new BufferedReader(f);
		    this.model = model;
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			f.close();
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//TODO: gestire caso in cui non ci sono tutti e tre i luoghi nella riga
	public void getValues(String tValue, String t0) {
		try {
			int count = 0;
			String s = "";
			while ((s=b.readLine()) != null) {
				String[] s_1=s.split("}, ");
				String[] s_2=s_1[0].split(": ");
				String[] s_3=s_2[0].split("at time ");
				String t1 = "t" + count;
				//System.out.println(s_2[1]);
				String[] s_4=s_2[1].split(":\\{");
				String place=s_4[0];
				//System.out.println(palce);
				String[] s_5=s_4[1].split("%, ");
				String cloud=s_5[0];
				//System.out.println(s_5[1]);
				String[] s_6=s_5[1].split(" ");
				String wind=s_6[0];
				String direction=s_6[1];
				/*System.out.println(t1);
				System.out.println(palce);
				System.out.println(cloud);
				System.out.println(wind);
				System.out.println(direction);*/
				String value = "<" + model + "," + t0 + "> : C(" + place + ", " + t1 + ", " + cloud + ")\n";
				value += "<" + model + "," + t0 + "> : W(" + place + ", " + t1 + ", [" + direction + ", " + wind + "])\n";
				
	
				//seconda parte
				s_4=s_1[1].split(":\\{");
				String place1=s_4[0];
				s_5=s_4[1].split("%, ");
				String cloud1=s_5[0];
				s_6=s_5[1].split(" ");
				String wind1=s_6[0];
				String direction1=s_6[1];
	
				value += "<" + model + "," + t0 + "> : C(" + place1 + ", " + t1 + ", " + cloud1 + ")\n";
				value += "<" + model + "," + t0 + "> : W(" + place1 + ", " + t1 + ", [" + direction1 + ", " + wind1 + "])\n";
				
				//tre parte
				//System.out.println(s_1[2]);
				s_2=s_1[2].split("} ");
				s_4=s_2[0].split(":\\{");
				String place2=s_4[0];
				//System.out.println(palce);
				s_5=s_4[1].split("%, ");
				String cloud2=s_5[0];
				//System.out.println(s_5[1]);
				s_6=s_5[1].split(" ");
				String wind2=s_6[0];
				String direction2=s_6[1];
				String sea=s_2[1].split(": ")[0];
				String wave=s_2[1].split(": ")[1].split(",")[0];
				
				value += "<" + model + "," + t0 + "> : C(" + place2 + ", " + t1 + ", " + cloud2 + ")\n";
				value += "<" + model + "," + t0 + "> : W(" + place2 + ", " + t1 + ", [" + direction2 + ", " + wind2 + "])\n";
				value += "<" + model + "," + t0 + "> : S(" + sea + ", " + t1 + ", " + wave + ")\n";
				
				list.add(new TerrainTuple(t1.substring(1),place,cloud,wind,direction));
				list.add(new TerrainTuple(t1.substring(1),place1,cloud1,wind1,direction1));
				list.add(new TerrainTuple(t1.substring(1),place2,cloud2,wind2,direction2));
				
				list2.add(new Sea(t1.substring(1), wave));
				
				testo += value;
				count++;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
