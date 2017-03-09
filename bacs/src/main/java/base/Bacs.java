package base;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

import org.ini4j.Wini;

public class Bacs {
	private static String title = "Bacs v1.12";
	
	public static int iternum = 0;

	static Settings settings;

	static long start = System.nanoTime();
	
	public static volatile BacUnit[][] battlefield;// = new BacUnit[dimension][dimension];
	
	public static JFrame window;
	
	
	public static void main(String[] args) throws IOException { 

		settings = Settings.fromFile("conf.ini");
		initpainting();

		
		window=new JFrame(title);
		int baseSize = settings.dimension * settings.scale;
		window.setSize(baseSize + 15, baseSize + 38);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	/*	Менеджер определяет
	 *  каким образом в окне расположены объекты.*/
		window.setLayout(new BorderLayout(1,1));
		
		Canvas playfield=new Canvas();
		playfield.setSize(baseSize, baseSize);
        window.add(playfield);		
        window.setVisible(true);
        
        //w.addKeyListener(null);
        
    	initbattle();

    	Iteration[] processes = new Iteration[settings.cores];
    	
    	for(int i=0; i< settings.cores; i++){
    		processes[i] = new Iteration();	//Создание потока
    	}
    	
    	for(int i=0; i<settings.cores; i++){
    		processes[i].isDaemon();
    		processes[i].start();
    	}
    	
    	while(iternum < settings.maxIterations){
    		try {
    			Thread.sleep(16);
        		window.setTitle(title+ " Iteration " +iternum + " out of " + settings.maxIterations + "(" + iternum * 100 / settings.maxIterations + "% done)");
        		playfield.repaint();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		} 		
		}
    	
    	window.setTitle(title + " Iteration " + iternum + " out of "+ settings.maxIterations + "(100% done)");
    	
    	float passed = (float)(System.nanoTime() - Bacs.start)/1000000000;
    	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
        try(FileWriter writer = new FileWriter("endgame.txt", false))
        {
            StringBuilder text = new StringBuilder();
            for(int i=0; i<30; i++){
            	BacUnit that = Bacs.battlefield[Bacs.getRandom(0, settings.dimension - 1)][Bacs.getRandom(0, settings.dimension - 1)];
            	text.append("str=" + that.stats.str + " end=" + that.stats.end + " clr=" +that.stats.clr + " mut=" + that.stats.mut + " behaviour={ ");
            	for(int j = 0; j< BacUnit.actlim; j++) {
            	    text.append(that.behaviour[j] + " ");
            	}
            	text.append("}"+'\r'+'\n');
            }
	    	text.append("time: " + passed);
            writer.write(text.toString());
        }
        catch(IOException ex){
             
            System.out.println(ex.getMessage());
        } 
	}
	
	static void initbattle() throws IOException {
	    BacUnit initial = battlefield[settings.dimension / 2][settings.dimension / 2];
		initial.stats.clr = "FF0000";
        initial.direction = 0;
        initial.stats.str = 1;
        initial.stats.mut = 250;
        initial.stats.end = 100;
        initial.energy = 50;
        initial.behaviour[0] = settings.actLim + 3;
        String[] behraw = settings.behaviour.split(":");
        for(int i = 0; i<behraw.length; i++) {
            initial.behaviour[i] = Integer.parseInt(behraw[i]);
        }
//		getMoreParameters();
	}
	
	static void initpainting(){
		battlefield = new BacUnit[settings.dimension][settings.dimension];
		for(int i = 0; i< Bacs.settings.dimension; i++){
			for(int j = 0; j< Bacs.settings.dimension; j++){
				battlefield[i][j] = new BacUnit();
				battlefield[i][j].stats.clr = "000000";
			}
		}
	}
	
	static int getRandom(int min, int max)
	{
		return (int) (Math.floor(Math.random() * (max - min + 1)) + min);
	}

}

class Iteration extends Thread
{
	
	public void run()
	{
		while(Bacs.iternum < Bacs.settings.maxIterations){
			int dimension = Bacs.settings.dimension;
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