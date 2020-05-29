package MaxwellVickyyu;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import java.util.*;

public class Ball2 extends Ellipse
{
	int speed;
	double xDirection, yDirection;
	protected final static int hw = 6; // size of ball
	public static int getHW() { return hw; }
	Random r;
	protected static int spaceW; // dimensions of the space
	protected static int spaceH; // to bounce in ( 0,0 to W,H )
	public static void setSpaceW( int w ) { spaceW = w; }
	public static void setSpaceH( int h ) { spaceH = h; }
	boolean flag;
	boolean flag_alr_set = false;
	Color colorBall;
	
    public Ball2( Random random, Color color, boolean left)
    {
    	super( hw, hw );
    	r=random;
    	flag = left; //checks which side ball is on
    	setFill(color); //color of ball
    	colorBall = color;
    	int rXDir = r.nextInt(1); //direction of ball
		if (rXDir == 0) 
			rXDir--;
		setXDirection(rXDir);
		
		int rYDir = r.nextInt(1); //random direction at start
		if (rYDir == 0)
			rYDir--;
		setYDirection(rYDir);
    	if(left) { //if on the left side, position ball on the left
    		int xPos = random.nextInt(250);
    		setCenterX(xPos);
    	}
    	else { //if on the right side, position ball on the right
    		setCenterX(random.nextInt(250)+250);
    	}
    	if(color == Color.BLUE) {
    		speed = r.nextInt(20) + 80;
    	}
    	else {
    		speed = r.nextInt(30) + 120;
    	}
    	setCenterY(random.nextInt(500));   	
    }
    
    public void setXDirection(double xDir){
		xDirection = xDir;
	}
	public void setYDirection(double yDir){
		yDirection = yDir;
	}
    
    public void move()
    {
    	double x = getCenterX();
    	x += xDirection * speed * 0.05;
    	setCenterX(x);
    	double y = getCenterY();
    	y += yDirection * speed * 0.05;
    	setCenterY(y);
    	if(flag) { //left-side, check bounds and bounce
    		if (x <= 0) {
    			setXDirection(+1);
        	}
    		if (x >= 247) {
    			setXDirection(-1);
    		}
    	}
    	else { //right-side, check bounds and bounce
    		if (x <= 253) {
    			setXDirection(+1);
        	}
    		if (x >= 500) {
    			setXDirection(-1);
    		}
    	}
		if (y <= 0) {
			setYDirection(+1);
		}
		if (y >= 500) {
			setYDirection(-1);
		}
    }
    public Color getColor() {
    	return colorBall;
    }
    public void setFlag() {
    	if(!flag_alr_set) {
	    	if(flag) {
	    		flag = false;
	    		flag_alr_set = true;
	    	}
	    	else {
	    		flag = true;
	    		flag_alr_set = true;
	    	}
    	}
    }
    public boolean getFlag() {
    	return flag;
    }
    public List<Integer> intersects(Rectangle rect, List<Integer> count) {
		if(this.intersects(rect.getBoundsInLocal())) {
			System.out.println("in here");
			if(flag) { //originally in left side, going to right side
				if(colorBall == Color.RED) {
					int value1 = count.get(2) - 1; //numLRed
					int value2 = count.get(0) + 1; //numRRed
					count.set(2, value1) ;
					count.set(0, value2) ;
				}
				else {
					int value1 = count.get(1) + 1; //numRBlue
					int value2 = count.get(3) - 1; //numLBlue
					count.set(1, value1) ;
					count.set(3, value2) ;
				}
			}
			else { //originally in right side, going to left side
				if(colorBall == Color.RED) {
					int value1 = count.get(2) + 1; //numLRed
					int value2 = count.get(0) - 1; //numRRed
					count.set(2, value1) ;
					count.set(0, value2) ;
				}
				else {
					int value1 = count.get(1) - 1; //numRBlue
					int value2 = count.get(3) + 1; //numLBlue
					count.set(1, value1);
					count.set(3, value2);
				}
			}
	        setFlag();
	        flag_alr_set = false;
	    }
		return count;
	}
    public boolean get_flag_alr_set() {
    	return flag_alr_set;
    }
    public void set_flag_alr_set() {
    	flag_alr_set = false;
    }
}
