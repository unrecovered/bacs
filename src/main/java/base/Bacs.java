package base;

import base.ui.Canvas;
import base.ui.MainFrame;

import java.io.FileWriter;
import java.io.IOException;

import static base.utils.Random.*;

public class Bacs {
	private static String title = "Bacs v1.12";
	
	static int iternum = 0;

	static Settings settings;

	static long start = System.nanoTime();
	
	public static volatile BacUnit[][] battlefield;// = new BacUnit[dimension][dimension];
	
	private static MainFrame window;


	public static void main(String[] args) throws IOException {
		settings = Settings.fromProperties("conf.properties");
		Canvas playField = new Canvas(settings.dimension, settings.scale);
        initPainting();

		window = new MainFrame(title, settings, playField);

        initBattle();

    	Iteration[] processes = new Iteration[settings.cores];
    	
    	for(int i=0; i< settings.cores; i++){
            processes[i] = new Iteration(settings);    //Создание потока
        }
    	
    	for(int i=0; i<settings.cores; i++){
    		processes[i].isDaemon();
    		processes[i].start();
    	}
    	
    	while(iternum < settings.maxIterations){
    		try {
    			Thread.sleep(16);
        		window.setTitle(title+ " Iteration " +iternum + " out of " + settings.maxIterations + "(" + iternum * 100 / settings.maxIterations + "% done)");
        		playField.repaint();
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
		
        try(FileWriter writer = new FileWriter("endgame.txt", false)) {
            StringBuilder text = new StringBuilder();
            for(int i=0; i<30; i++){
                BacUnit that = Bacs.battlefield[getRandom(0, settings.dimension - 1)][getRandom(0, settings.dimension - 1)];
                text.append("str=" + that.str + " end=" + that.end + " clr=" +that.clr + " mut=" + that.mut + " behaviour={ ");
            	for(int j = 0; j< BacUnit.actlim; j++) {
            	    Command cmd = Command.fromCode(that.behaviour[j]);
            	    if (cmd != null) {
                        text.append(cmd.getName() + " ");
                    } else {
            	        text.append(that.behaviour[j] + " ");
                    }
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

    static void initBattle() throws IOException {
        BacUnit initial = battlefield[settings.dimension / 2][settings.dimension / 2];
		initial.clr = "FF0000";
        initial.direction = 0;
        initial.str = 1;
        initial.mut = 250;
        initial.end = 100;
        initial.energy = 50;
//        initial.behaviour[0] = settings.actLim + 3;
        String[] behraw = settings.behaviour.split(":");
        for(int i = 0; i<behraw.length; i++) {
            initial.behaviour[i] = Integer.parseInt(behraw[i]);
        }
	}

    static void initPainting() {
        battlefield = new BacUnit[settings.dimension][settings.dimension];
		for(int i = 0; i< Bacs.settings.dimension; i++){
			for(int j = 0; j< Bacs.settings.dimension; j++){
				battlefield[i][j] = new BacUnit();
				battlefield[i][j].clr = "000000";
			}
		}
	}
}