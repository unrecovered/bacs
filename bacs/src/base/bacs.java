package base;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.lang.reflect.Field;

import javax.swing.JFrame;

import org.ini4j.Wini;

public class bacs{
	public static String title = "Bacs v1.12";
	
	public static int iternum = 0;
	
	public static int itermax = 40000;
	
	public static int scale = 1;
	
	public static int cores = 2;//Runtime.getRuntime().availableProcessors();
	
	public static int dimension = 300;
	
	public static int actlim; 
	
	static long start = System.nanoTime();
	
	public static volatile bacunit[][] battlefield;// = new bacunit[dimension][dimension];
	
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

    	iteration[] processes = new iteration[cores];
    	
    	for(int i=0; i<cores; i++){
    		processes[i] = new iteration();	//Создание потока
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
    	
    	float passed = (float)(System.nanoTime() - bacs.start)/1000000000;
    	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
        try(FileWriter writer = new FileWriter("endgame.txt", false))
        {
            String text = "";
            for(int i=0; i<30; i++){
            	bacunit that = bacs.battlefield[bacs.getRandom(0, dimension-1)][bacs.getRandom(0, dimension-1)];
            	text+= "str="+that.stats.str+" end="+that.stats.end+" clr="+that.stats.clr+" mutstat="+that.stats.mutstat+" mutbeh="+that.stats.mutbeh+" behaviour={ ";
            	for(int j=0; j<bacunit.actlim; j++){text+=that.behaviour[j]+" ";}
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
		battlefield[dimension/2][dimension/2].stats.mutstat = 250;
		battlefield[dimension/2][dimension/2].stats.mutbeh = 250;
		battlefield[dimension/2][dimension/2].stats.end = 100;
		battlefield[dimension/2][dimension/2].energy = 50;
		battlefield[dimension/2][dimension/2].behaviour[0] = bacunit.actlim+3;/*
		Field[] fieldlist = battlefield[dimension/2][dimension/2].getClass().getDeclaredFields();
		for(Field i : fieldlist){
			System.out.println(i.getName()+"="+i+" ");
		} хз пока что*/
		getMoreParameters();
	}
	
	static void initpainting(){
		battlefield = new bacunit[dimension][dimension];
		for(int i = 0; i<bacs.dimension; i++){
			for(int j = 0; j<bacs.dimension; j++){
				battlefield[i][j] = new bacunit();
				//battlefield[i][j].stats.clr = "000000";
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
        bacs.cores = ini.get("settings", "threads", int.class);
        if(bacs.cores == 0){bacs.cores = Runtime.getRuntime().availableProcessors()/2;}
        
        bacs.dimension = ini.get("settings", "dimension", int.class);
        
        bacs.scale = ini.get("settings", "scale", int.class);
      
        bacs.itermax = ini.get("settings", "iterations", int.class);
        
        //bacunit
        bacunit.actlim = ini.get("bacunit", "actlim", int.class);
        
        bacunit.relsence = ini.get("bacunit", "relsence", int.class);
        
        bacunit.gainbase = ini.get("bacunit", "gainbase", int.class);
        
        bacunit.lumus = ini.get("bacunit", "light", boolean.class);
    }
    
    static void getMoreParameters() throws IOException{//порнография...
    	Wini ini = new Wini(new File("conf.ini"));
    	bacunit that = battlefield[dimension/2][dimension/2];
    	that.stats.str = ini.get("bacunit", "str", int.class);
    	that.stats.mutstat = ini.get("bacunit", "mutstat", int.class);
    	that.stats.mutbeh = ini.get("bacunit", "mutbeh", int.class);
    	that.stats.end = ini.get("bacunit", "end", int.class);
    	
    	String beh = ini.get("bacunit", "behaviour", String.class);
    	
    	beh = beh.replace("move", Integer.toString(bacunit.actlim));
    	beh = beh.replace("turn", Integer.toString(bacunit.actlim+1));
    	beh = beh.replace("eat", Integer.toString(bacunit.actlim+2));
    	beh = beh.replace("gain", Integer.toString(bacunit.actlim+3));
    	beh = beh.replace("attack", Integer.toString(bacunit.actlim+4));
    	beh = beh.replace("observe", Integer.toString(bacunit.actlim+5));
    	
    	String[] behraw = beh.split(":");
    	for(int i = 0; i<behraw.length; i++){that.behaviour[i] = Integer.parseInt(behraw[i]);};
    }
}

class iteration extends Thread
{
	
	public void run()
	{
		while(bacs.iternum < bacs.itermax){
			int dimension = bacs.dimension;
			int x, y;
			for (int i = 0; i<dimension*dimension; i++){
				//System.out.println(i+" "+j+" act="+battlefield[i][j].action+" comm="+battlefield[i][j].behaviour[battlefield[i][j].action % 64]+" dir="+battlefield[i][j].direction+" nrg="+battlefield[i][j].energy);
				x = bacs.getRandom(0, dimension-1);
				y = bacs.getRandom(0, dimension-1);
				if((bacs.battlefield[x][y].stats.clr != "000000")&&(bacs.battlefield[x][y].stats.clr != "FFFFFF")){
					bacs.battlefield[x][y].act(x,y);
				}
			}
			bacs.iternum++;
		}
	}
}