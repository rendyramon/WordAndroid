package com.riotapps.word.ui;

import java.util.ArrayList;
import java.util.List;

import com.riotapps.word.R;
import com.riotapps.word.hooks.TileLayout;
import com.riotapps.word.hooks.TileLayoutService;
import com.riotapps.word.utils.Constants;

import android.content.Context;
 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

public class GameSurfaceView extends SurfaceView  implements SurfaceHolder.Callback {

	GameSurfaceView me = this;
	Context _context;
	GameThread gameThread = null;
	SurfaceHolder surfaceHolder;
	Typeface _typeface;
	private int currentX = 0;
    private int currentY = 20;
    private int fullWidth;
    private int smallTileWidth;
    private int top;
    private int left;
    private boolean isZoomed = false;
    private int excessWidth;
    private boolean isZoomAllowed = true; //if width of board greater than x disable zooming.  it means we are on a tablet and zooming not needed.
    private int activeTileWidth; 
    private long tapCheck = 0;
    
    
    List<GameTile> list = new ArrayList<GameTile>();
    TileLayout layout;
    TileLayoutService layoutService;

 
	public GameSurfaceView(Context context) {
		super(context);
		this.construct(context);
	}

	public GameSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.construct(context);
		
		// TODO Auto-generated constructor stub
	}

	private void construct(Context context) {
		this._context = context;
		this.layoutService = new TileLayoutService();
		this.layout = layoutService.GetDefaultLayout(context);
		//
		this.setZOrderOnTop(true);
		 SurfaceHolder holder = getHolder();
		 holder.addCallback(this);
		 gameThread = new GameThread(holder, this);
		 setFocusable(true);
		 _typeface = Typeface.createFromAsset(context.getAssets(), Constants.MAIN_FONT);
		  
		 holder.setFormat(PixelFormat.TRANSPARENT);// necessary
		 
		 
		 this.post(new Runnable() 
		    {   
		        @Override
		        public void run() {
		        	
		        me.fullWidth = me.getWidth();
		   		me.smallTileWidth = (int) Math.round(fullWidth/15) - 1; //-1 for the space between each tile
		   		me.top = me.getTop();
		   		me.left = me.getLeft();
		   		me.excessWidth = me.fullWidth - ((me.smallTileWidth * 15) + 14);
		   	 Toast t = Toast.makeText(me._context, String.valueOf(me.smallTileWidth)  + " " + String.valueOf(me.left)  + " " + String.valueOf(me.top) + " " + String.valueOf(me.fullWidth), Toast.LENGTH_LONG);  
			    t.show();            
		        }
		    });
	

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.gameThread.setRunning(true);
		this.gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	    // simply copied from sample application LunarLander:
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
	    boolean retry = true;
	    this.gameThread.setRunning(false);
	    while (retry) {
	        try {
	        	this.gameThread.join();
	            retry = false;
	        } catch (InterruptedException e) {
	            // we will try it again and again...
	        }
	    }
		
	}

	public void onResume() {
	//	  random = new Random();
		  surfaceHolder = getHolder();
		  getHolder().addCallback(this);
		   
		  //Create and start background Thread
		  gameThread = new GameThread(this, 500);
		  gameThread.setRunning(true);
		  gameThread.start();
		
	}

	public void onPause() {
		  //Kill the background Thread
		  boolean retry = true;
		  gameThread.setRunning(false);
		   
		  while(retry){
		   try {
			   gameThread.join();
		    retry = false; 
		   } catch (InterruptedException e) {
		    e.printStackTrace(); 
		   } 
		  }
		
	}
	
	 @Override
	 protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		// this.setLayoutParams(params)
		 //canvas.co
		 
		 canvas.drawColor(0, Mode.CLEAR);
		 int tileFontSize;
		 Bitmap _scratch;
		 Bitmap _scaled;
		 int left = 0;
		 int top = 0;
		 
		 if (this.isZoomed == false || this.isZoomAllowed == false){
			 this.activeTileWidth = this.smallTileWidth;
	        _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile);
	        _scaled = Bitmap.createScaledBitmap(_scratch, smallTileWidth , smallTileWidth, false);
	      //  canvas.drawColor(Color.TRANSPARENT);
	        left = 1 + (this.excessWidth / 2);
	        top = 1;
		 }
		 else {
			// canvas.drawColor(Color.CYAN);
			 this.activeTileWidth = this.smallTileWidth * 2;
			 _scratch = BitmapFactory.decodeResource(getResources(), R.drawable.blank_tile);
		     _scaled = Bitmap.createScaledBitmap(_scratch, activeTileWidth , activeTileWidth, false);
			 //calculate left and top based on pointer
		     
		     left = this.currentX;
		     top = this.currentY;
		     
		     //left needs to be centered, top needs to be centered in zoomed view
		 }
     	 
    	// Toast t = Toast.makeText(me._context, "width: " + String.valueOf(activeTileWidth), Toast.LENGTH_LONG);  
		 //   t.show(); 
	     //make sure full view is centered so grab remainder of 15 division 
	     //determine if font text can be used so that fewer images must be maintained
	     //use font size based on 80% of tile size
	       //keep array of tiles
	       
	     tileFontSize = (int) Math.round(this.activeTileWidth * .8);
	  //   canvas.drawColor(Color.GREEN);
	     for (int x = 0; x<15 ;x++){
	    	 this.temp(_scaled,canvas,x, left, top);
	    	 
	     }
	//     this.temp(_scaled,canvas,0, left);
	//     this.temp(_scaled,canvas,1);
	//     this.temp(_scaled,canvas,2);
	//     this.temp(_scaled,canvas,3);
	//     this.temp(_scaled,canvas,4);
	//     this.temp(_scaled,canvas,5);
	//     this.temp(_scaled,canvas,6);
	//     this.temp(_scaled,canvas,7);
	//     this.temp(_scaled,canvas,8);
	//     this.temp(_scaled,canvas,9);
	//     this.temp(_scaled,canvas,10);
	//     this.temp(_scaled,canvas,11);
	//     this.temp(_scaled,canvas,12);
	//     this.temp(_scaled,canvas,13);
	//     this.temp(_scaled,canvas,14);
		 
	 
	       
	       // canvas.drawBitmap(_scaled, _x  - (_scaled.getWidth() / 2), _y - (_scaled.getWidth() / 2), null);
	       // canvas.drawBitmap(_scratch, _x + 22 - (_scratch.getWidth() / 2), _y - (_scaled.getWidth() / 2), null);
	        // Paint p = new Paint();
	       // p.setTextSize(24);
	       // p.setAntiAlias(true);
	       // p.setTypeface(_typeface);
	       // canvas.drawText("4L", 50, 50, p);
	//        canvas.d
		 
	 }
	 
	 private void temp(Bitmap bm, Canvas canvas, int x, int left, int top){
	      canvas.drawBitmap(bm,left  + (this.activeTileWidth * x) + x, top, null);
	      canvas.drawBitmap(bm, left + (this.activeTileWidth * x) + x, top + activeTileWidth + 1, null);
	      canvas.drawBitmap(bm, left + (this.activeTileWidth * x) + x,top + (activeTileWidth * 2) + 2, null);
	      canvas.drawBitmap(bm, left + (this.activeTileWidth * x) + x,top + (activeTileWidth * 3) + 3, null);
	      canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x,top + (activeTileWidth * 4) + 4, null);
	      canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x,top + (activeTileWidth * 5) + 5, null);
		  canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 6) + 6, null);
		  canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 7) + 7, null);
		  canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 8) + 8, null);
		  canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 9) + 9, null);
		  canvas.drawBitmap(bm, left   + (this.activeTileWidth * x) + x, top + (activeTileWidth * 10) + 10, null);
		  canvas.drawBitmap(bm, left + (this.activeTileWidth * x) + x, top + (activeTileWidth * 11) + 11, null);
		  canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 12) + 12, null);
          canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 13) + 13, null);
		  canvas.drawBitmap(bm, left  + (this.activeTileWidth * x) + x, top + (activeTileWidth * 14) + 14, null);
		  canvas.drawBitmap(bm, left   + (this.activeTileWidth * x) + x, top + (activeTileWidth * 15) + 15, null);
	 
	 
	 }
	 
	 
	 @Override
	 public boolean onTouchEvent(MotionEvent event) {
	     this.currentX = (int) event.getX();
	     this.currentY = (int) event.getY();
	     //return true;
	     
	     synchronized (this.gameThread.getSurfaceHolder()) {
             switch (event.getAction()) {
             
             case MotionEvent.ACTION_DOWN:
            
                 //where is the click, which object within view???
            	 //get tile from coordinates.  if tile is null, do nothing
            	 //for now act like this is a click/tap...
            	 
            	 this.tapCheck = System.nanoTime();
            	// this.invalidate();
            	 
            	 break;
             case MotionEvent.ACTION_UP:
            	 if (this.tapCheck > 0 && System.nanoTime() - this.tapCheck <= 500000000) {
            		 this.isZoomed = !this.isZoomed;
            	 }
            	 this.tapCheck = 0;
            	 break;
             case MotionEvent.ACTION_MOVE:
             }
             
             return true;
         }

	 }
	 
	 public void updateStates(){
	  //Dummy method() to handle the States
	 }
	 
	 public void updateSurfaceView(){
	  //The function run in background thread, not ui thread.
	   
	  Canvas canvas = null;
	    
	  try{
	   canvas = surfaceHolder.lockCanvas();
	 
	   synchronized (surfaceHolder) {
	    updateStates();
	    onDraw(canvas);
	   }
	  }
	  finally{
	   if(canvas != null){
	    surfaceHolder.unlockCanvasAndPost(canvas);
	   }
	  } 
	 }
	 
 

}