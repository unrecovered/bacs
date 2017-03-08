package base;

import java.awt.*;

import javax.swing.JComponent;

class Canvas extends JComponent{
	 
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Graphics2D g2d;
  
  public void paintComponent(Graphics g){	

	g2d=(Graphics2D)g;
	//g2d.setBackground(Color.BLACK);
	//g2d.clearRect(0, 0, Bacs.dimension-1, Bacs.dimension-1);
	
	for(int i = 0; i<bacs.dimension; i++){
		for(int j = 0; j<bacs.dimension; j++){
			g2d.setPaint(Color.decode("#"+bacs.battlefield[i][j].stats.clr));
			//if(Bacs.battlefield[i][j].stats.clr != "#000000"){System.out.println(i+" "+j+" "+Bacs.battlefield[i][j].stats.clr);}
			if(bacs.scale>3){
				g2d.drawRect(i*bacs.scale, j*bacs.scale, bacs.scale-1, bacs.scale-1);
				g2d.fillRect(i*bacs.scale, j*bacs.scale, bacs.scale-1, bacs.scale-1);
			}else{
				g2d.drawRect(i*bacs.scale, j*bacs.scale, bacs.scale-1, bacs.scale-1);
			}
		}
	}
  }	
}
