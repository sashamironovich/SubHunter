package com.gamecodeschool.subhunter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.util.Log;
import android.widget.ImageView;
import java.util.Random;


public class SubHunter extends Activity {

    int numberHorizontalPixels;
    int numberVerticalPixels;
    int blockSize;
    int gridWidth = 40;
    int gridHeight;
    float horizontalTouched = -100;
    float verticalTouched = -100;
    int subHorizontalPosition;
    int subVerticalPosition;
    boolean hit = false;
    int shotsTaken;
    int distanceFromSub;
    boolean debugging = true;

    //Here are all the  objects
    //of classes that we need to do some drawing
    ImageView gameView;
    Bitmap blankBitmap;
    Canvas canvas;
    Paint paint;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the current device's screen resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Initialize our size based variable
        //based on the screen resolution
        numberHorizontalPixels = size.x;
        numberVerticalPixels = size.y;
        blockSize = numberHorizontalPixels/gridWidth;
        gridHeight = numberVerticalPixels / blockSize;

        //initialize all the objects ready for drawing
        blankBitmap = Bitmap.createBitmap(numberHorizontalPixels,numberVerticalPixels,Bitmap.Config.ARGB_8888);

        canvas = new Canvas(blankBitmap);
        gameView = new ImageView(this);
        paint = new Paint();

        //tell android to set our drawing as the view for this app

        setContentView(gameView);

        Log.d("Debugging", "In onCreate");
        newGame();
        draw();
    }

    /*
           This code will execute when a new
           game needs to be started. It will
           happen when the app is first started
           and after the player wins a game.
    */
    void newGame(){
        Random random = new Random();
        subHorizontalPosition = random.nextInt(gridWidth);
        subVerticalPosition = random.nextInt(gridHeight);
        shotsTaken = 0;

        Log.d("Debugging", "In newGame");

    }



    /*
           Here we will do all the drawing.
           The grid lines, the HUD and
           the touch indicator
    */

    void draw(){

        gameView.setImageBitmap(blankBitmap);

        //wipe the screen with white color
        canvas.drawColor(Color.argb(255,255,255,255));

        //change the paint color to black
        paint.setColor(Color.argb(255,0,0,0));

        //draw the vertical lines of the grid
        for(int i = 0; i < gridWidth; i++){
            canvas.drawLine(blockSize * i, 0,
                    blockSize * i, numberVerticalPixels,
                    paint); }
        //draw the horizontal lines

        for(int i = 0; i < gridHeight; i++){
            canvas.drawLine(0, blockSize * i,
                    numberHorizontalPixels, blockSize * i,
                    paint); }

        //draw the player's shot
        canvas.drawRect(horizontalTouched*blockSize,verticalTouched*blockSize,(horizontalTouched*blockSize)+blockSize,(verticalTouched*blockSize)+blockSize,paint);


        // Re-size the text appropriate for the
        // score and distance text
        paint.setTextSize(blockSize * 2);
        paint.setColor(Color.argb(255, 0, 0, 255));
        canvas.drawText(
                "Shots Taken: " + shotsTaken +
                        "  Distance: " + distanceFromSub,
                blockSize, blockSize * 1.75f,
                paint);

        Log.d("Debugging", "In draw");
        if(debugging) {


            printDebuggingText();
        }
    }


    /*
           This part of the code will
           handle detecting that the player
           has tapped the screen
    */

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        Log.d("Debugging", "In onTouchEvent");

        //has the player removed their finger from the screen?
        if((motionEvent.getAction()& MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP){
            // Process the player's shot by passing the
            // coordinates of the player's finger to takeShot
            takeShot(motionEvent.getX(), motionEvent.getY());
        }



    return true;
    }


    /*
           The code here will execute when
           the player taps the screen. It will
           calculate the distance from the sub'
           and decide a hit or miss
    */

    void takeShot(float touchX, float touchY){
        Log.d("Debugging","In takeShot");

        //Add one to the shotsTaken variable
        shotsTaken++;

        // Convert the float screen coordinates
        // into int grid coordinates
        horizontalTouched = (int)touchX/ blockSize;
        verticalTouched = (int)touchY/ blockSize;

        // Did the shot hit the sub?
        hit = horizontalTouched == subHorizontalPosition
                && verticalTouched == subVerticalPosition;


        // How far away horizontally and vertically
        // was the shot from the sub
        int horizontalGap = (int)horizontalTouched -
                subHorizontalPosition;
        int verticalGap = (int)verticalTouched -
                subVerticalPosition;

        // Use Pythagoras's theorem to get the
        // distance travelled in a straight line
        distanceFromSub = (int)Math.sqrt(
                ((horizontalGap * horizontalGap) +
                        (verticalGap * verticalGap)));

        // If there is a hit call boom
        if(hit)
            boom();
            // Otherwise call draw as usual
        else draw();
    }

    // This code says "BOOM!"

    void boom(){
        gameView.setImageBitmap(blankBitmap);

        //wipe the screen with a red color
        canvas.drawColor(Color.argb(255,255,0,0));

        //draw some huge white text
        paint.setColor(Color.argb(255,255,255,255));
        paint.setTextSize(blockSize*10);

        canvas.drawText("BOOM!",blockSize*4,blockSize*14,paint);

        //draw some text to prompt restarting
        paint.setTextSize(blockSize*2);
        canvas.drawText("Tap anywhere to start again",blockSize*8,blockSize*18,paint);

        //start a new game
        newGame();


    }

    // This code prints the debugging text

    void printDebuggingText(){
        Log.d("numberHoizontalPixels", " " + numberHorizontalPixels);
        Log.d("numberVerticalPixels"," " + numberVerticalPixels);

        Log.d("blockSize", "" + blockSize);
        Log.d("gridWidth" ,  "" + gridWidth);
        Log.d("gridHeight", "" + gridHeight);
        Log.d("horizontalTouched", "" + horizontalTouched);
        Log.d("verticalTouched", "" + verticalTouched);
        Log.d("subHorizontalPosition" , "" + subHorizontalPosition);
        Log.d("subVerticalPosition","" + subVerticalPosition);
        Log.d("hit", "" + hit);
        Log.d("shotsTaken", "" + shotsTaken);
        Log.d("debugging", "" + debugging);
        Log.d("distanceFromSub",
                "" + distanceFromSub);


    }
}
