package base;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import org.ini4j.Wini;

public class Bacs {
	public static String title = "Bacs v1.12";
	
	public static int iternum = 0;
	
	public static int itermax = 40000;
	
	public static int scale = 1;
	
	public static int cores = 2;//Runtime.getRuntime().availableProcessors();
	
	public static int dimension = 300;
	
	public static int actlim; 
	
	static long start = System.nanoTime();
	
	public static volatile BacUnit[][] battlefield;// = new BacUnit[dimension][dimension];
	
	public static JFrame window;
	
	
	public static void main(String[] args) throws IOException { 
		
		getParameters();
		initpainting();

		
		window=new JFrame(title);
		window.setSize(dimension*scale+15, dimension*scale+38);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	/*	Менеджер определяет
	 *  каким образом в окне расположены объекты.*/
		window.setLayout(new BorderLayout(1,1));
		
		Canvas playfield=new Canvas();
		playfield.setSize(dimension*scale, dimension*scale);
        window.add(playfield);		
        window.setVisible(true);
        
        //w.addKeyListener(null);
        
    	initbattle();

    	Iteration[] processes = new Iteration[cores];
    	
    	for(int i=0; i<cores; i++){
    		processes[i] = new Iteration();	//Создание потока
    	}
    	
    	for(int i=0; i<cores; i++){
    		processes[i].isDaemon();
    		processes[i].start();
    	}
    	
    	while(iternum<itermax){
    		try {
    			Thread.sleep(16);
        		window.setTitle(title+" Iteration "+iternum+" out of "+itermax+"("+iternum*100/itermax+"% done)");
        		playfield.repaint();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		} 		
		}
    	
    	window.setTitle(title+" Iteration "+iternum+" out of "+itermax+"(100% done)");
    	
    	float passed = (float)(System.nanoTime() - Bacs.start)/1000000000;
    	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
        try(FileWriter writer = new FileWriter("endgame.txt", false))
        {
            String text = "";
            for(int i=0; i<30; i++){
            	BacUnit that = Bacs.battlefield[Bacs.getRandom(0, dimension-1)][Bacs.getRandom(0, dimension-1)];
            	text+= "str="+that.stats.str+" end="+that.stats.end+" clr="+that.stats.clr+" mut="+that.stats.mut+" behaviour={ ";
            	for(int j = 0; j< BacUnit.actlim; j++){text+=that.behaviour[j]+" ";}
            	text+="}"+'\r'+'\n';
            }
	    	text+="time: "+passed;
            writer.write(text);
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
	}
	
	static void initbattle() throws IOException{
		battlefield[dimension/2][dimension/2].stats.clr = "FF0000";
		battlefield[dimension/2][dimension/2].direction = 0;
		battlefield[dimension/2][dimension/2].stats.str = 1;
		battlefield[dimension/2][dimension/2].stats.mut = 250;
		battlefield[dimension/2][dimension/2].stats.end = 100;
		battlefield[dimension/2][dimension/2].energy = 50;
		battlefield[dimension/2][dimension/2].behaviour[0] = BacUnit.actlim+3;
		getMoreParameters();
	}
	
	static void initpainting(){
		battlefield = new BacUnit[dimension][dimension];
		for(int i = 0; i< Bacs.dimension; i++){
			for(int j = 0; j< Bacs.dimension; j++){
				battlefield[i][j] = new BacUnit();
				battlefield[i][j].stats.clr = "000000";
			}
		}
	}
	
	public static int getRandom(int min, int max)
	{
		return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
	}
	
    static void getParameters() throws IOException
    {
        Wini ini = new Wini(new File("conf.ini"));
        //setting
        Bacs.cores = ini.get("settings", "threads", int.class);
        if(Bacs.cores == 0){
			Bacs.cores = Runtime.getRuntime().availableProcessors()/2;}
        
        Bacs.dimension = ini.get("settings", "dimension", int.class);
        
        Bacs.scale = ini.get("settings", "scale", int.class);
      
        Bacs.itermax = ini.get("settings", "iterations", int.class);
        
        //BacUnit
        BacUnit.actlim = ini.get("BacUnit", "actlim", int.class);
        
        BacUnit.relsence = ini.get("BacUnit", "relsence", int.class);
        
        BacUnit.gainbase = ini.get("BacUnit", "gainbase", int.class);
        
        BacUnit.lumus = ini.get("BacUnit", "light", boolean.class);
    }
    
    static void getMoreParameters() throws IOException{//порнография...
    	Wini ini = new Wini(new File("conf.ini"));
    	BacUnit that = battlefield[dimension/2][dimension/2];
    	that.stats.str = ini.get("BacUnit", "str", int.class);
    	that.stats.mut = ini.get("BacUnit", "mut", int.class);
    	that.stats.end = ini.get("BacUnit", "end", int.class);
    	
    	String beh = ini.get("BacUnit", "behaviour", String.class);
    	
    	beh = beh.replace("move", Integer.toString(BacUnit.actlim));
    	beh = beh.replace("turn", Integer.toString(BacUnit.actlim+1));
    	beh = beh.replace("eat", Integer.toString(BacUnit.actlim+2));
    	beh = beh.replace("gain", Integer.toString(BacUnit.actlim+3));
    	beh = beh.replace("attack", Integer.toString(BacUnit.actlim+4));
    	beh = beh.replace("observe", Integer.toString(BacUnit.actlim+5));
    	
    	String[] behraw = beh.split(":");
    	for(int i = 0; i<behraw.length; i++){that.behaviour[i] = Integer.parseInt(behraw[i]);};
    }
}

class Iteration extends Thread
{
	
	public void run()
	{
		while(Bacs.iternum < Bacs.itermax){
			int dimension = Bacs.dimension;
			int x, y;
			for (int i = 0; i<dimension*dimension; i++){
				//System.out.println(i+" "+j+" act="+battlefield[i][j].action+" comm="+battlefield[i][j].behaviour[battlefield[i][j].action % 64]+" dir="+battlefield[i][j].direction+" nrg="+battlefield[i][j].energy);
				x = Bacs.getRandom(0, dimension-1);
				y = Bacs.getRandom(0, dimension-1);
				if((Bacs.battlefield[x][y].stats.clr != "000000")&&(Bacs.battlefield[x][y].stats.clr != "FFFFFF")){
					Bacs.battlefield[x][y].act(x,y);
				}
			}
			Bacs.iternum++;
		}
	}
}