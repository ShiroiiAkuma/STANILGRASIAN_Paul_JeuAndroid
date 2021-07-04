package dwm.moi.jeu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/***
 * il faut faire un design
 * deux joeurs comme sur le tp
 * change de chemin apres fin de jeu
 * faire un timer 7s
 * si un joeur fini vite il a 7s+ le temps reste
 * niveau de jeu evolue
 * si le temps est presque fini cacher le chemin (comme il fait nuit -> oppacite)
 * un son
 * 
 */
public class Vue extends View {


    private static final int COLONNES = 7;
    private static final int LIGNES=14;
    private static final float MUR =25;
    private Cell[][] cells;
    private  Cell joueur;
    private  Cell exit;
    private float taille;
    private float hMargin;
    private float vMargin;
    private Paint bg;
    private Paint joueurDesign;
    private Paint goal;


    private Random random;

    public Vue(Context context, @Nullable AttributeSet attrs) {

        //this.bg.sedColor(Color.GREEN);


    /*    Random random = new Random();
        int nb;
        nb = random.nextInt(9);
        byte[] genererBit(int taille){
    Random random = new Random();
    byte[] tbyte = new byte[taille];
    random.nextBytes(tbyte);
    return tbyte;
}
public static void main(String[] args) {
   byte[] tbyte = genererBit(20);
   for(byte bit:tbyte)
   System.out.print(" "+bit);
}
*/

        super(context, attrs);
        this.bg =new Paint();
        this.bg.setColor(Color.BLACK);
        this.bg.setStrokeWidth(MUR);
        random = new Random();
        joueurDesign = new Paint();
        joueurDesign.setColor(Color.RED);
        goal = new Paint();
        goal.setColor(Color.GREEN);
        create();
    }
    private enum  Direction{
        NORD,SUD,OUEST,EST;
    }



//import java.util.ArrayList;
// un ArrayList de chaînes de caractères
//ArrayList<String> liste = new ArrayList<String>();
// on suppose ici que la classe Foobar a déjà été déclarée
//ArrayList<Foobar> liste = new ArrayList<Foobar>();
//private Cell cellNearToYou(Cell uneCellule){
//ArrayList<Cell> nearYou = new ArrayList<>();
//ArrayList<Integer> nombres = new ArrayList<Integer>();
//Foobar foo = new Foobar();
//liste.add(foo);
// ajoute un Integer
//nombres.add(5);
//gauche;

/*
        if(uneCellule.colonne > 0) {
        if (!cells[uneCellule.colonne - 1][uneCellule.ligne].cellulePasse){
            nearYou.add(cells[uneCellule.colonne - 1][uneCellule.ligne]);}
    }
*/
    private Cell cellNearToYou(Cell uneCellule){
        ArrayList<Cell> nearYou = new ArrayList<>();

        //gauche
        if(uneCellule.colonne > 0) {
            if (!cells[uneCellule.colonne - 1][uneCellule.ligne].cellulePasse){
                nearYou.add(cells[uneCellule.colonne - 1][uneCellule.ligne]);}
        }
        //droite
        if(uneCellule.colonne < COLONNES-1) {
            if (!cells[uneCellule.colonne + 1][uneCellule.ligne].cellulePasse) {
                nearYou.add(cells[uneCellule.colonne + 1][uneCellule.ligne]);
            }
        }
        //haut
        if(uneCellule.ligne > 0) {
            if (!cells[uneCellule.colonne][uneCellule.ligne - 1].cellulePasse) {
                nearYou.add(cells[uneCellule.colonne][uneCellule.ligne - 1]);
            }
        }
        //bas
        if(uneCellule.ligne < LIGNES-1) {
            if (!cells[uneCellule.colonne][uneCellule.ligne + 1].cellulePasse) {
                nearYou.add(cells[uneCellule.colonne][uneCellule.ligne + 1]);
            }
        }
        if(nearYou.size()> 0) {
            int index = random.nextInt(nearYou.size());
            return nearYou.get(index);
        }else{
            return null;
        }
    }










    private void cheminDesign(Cell now, Cell suivant)
    {
        if(now.colonne == suivant.colonne && now.ligne == suivant.ligne +1){
            now.haut = false;
            suivant.bas = false;
        }
        if(now.colonne == suivant.colonne && now.ligne == suivant.ligne -1){
            now.bas = false;
            suivant.haut = false;
        }
        if(now.colonne == suivant.colonne +1 && now.ligne == suivant.ligne){
            now.gauche = false;
            suivant.droite = false;
        }
        if(now.colonne == suivant.colonne -1 && now.ligne == suivant.ligne){
            now.droite = false;
            suivant.gauche = false;
        }
    }
    private void create(){
        Stack<Cell> stack = new Stack<>();
        Cell now;
        Cell suivant;
        cells = new Cell[COLONNES][LIGNES];

        for (int i=0;i<COLONNES;i++){
            for (int y=0;y<LIGNES;y++){
                cells[i][y]= new Cell(i,y);
            }
        }

        joueur = cells[0][0];
        exit = cells[COLONNES-1][LIGNES-1];

        now = cells[0][0];
        now.cellulePasse = true;

        do {
            suivant = cellNearToYou(now);
            if (suivant != null) {
                cheminDesign(now, suivant);
                stack.push(now);
                now = suivant;
                now.cellulePasse = true;
            } else {
                now = stack.pop();
            }
        }while (!stack.empty());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        int width = getWidth();
        int height= getHeight();

        if(width/height < COLONNES/LIGNES)
            taille =width/(COLONNES+1);
        else
            taille = height/(LIGNES+1);

        //cest pour margin
        canvas.translate(hMargin,vMargin);


        hMargin = (width-COLONNES* taille)/2;
        vMargin = (height-LIGNES* taille)/2;
        for (int i=0;i<COLONNES;i++){
            for (int y=0;y<LIGNES;y++){
                if(cells[i][y].haut) {
                    canvas.drawLine(
                            i * taille,
                            y * taille,
                            (i + 1) * taille,
                            y * taille,
                            bg);
                }
                if(cells[i][y].gauche) {
                    canvas.drawLine(
                            i * taille,
                            y * taille,
                            i * taille,
                            (y+1) * taille,
                            bg);
                }
                if(cells[i][y].bas) {
                    canvas.drawLine(
                            i * taille,
                            (y+1) * taille,
                            (i + 1) * taille,
                            (y+1) * taille,
                            bg);
                }
                if(cells[i][y].droite) {
                    canvas.drawLine(
                            (i+1) * taille,
                            y * taille,
                            (i + 1) * taille,
                            (y+1) * taille,
                            bg);
                }
            }
        }
        float margin = taille /10;


/*
<canvas id="test2" width="500" height="300"></canvas>

 <script type="text/javascript">
        function draw2() {
            var c2 = document.getElementById('test2')
            var ctx = c2.getContext('2d')

            //head
            ctx.strokeStyle = "red";
            ctx.beginPath();
            ctx.arc(250, 50, 50, 0, 2 * Math.PI);
                         Ctx.stroke(); //Draw the defined path

                         //body
            ctx.strokeStyle = "red";
            ctx.beginPath();
                         ctx.moveTo(250, 100); //starting point
                         ctx.lineTo(250, 250); //end point
            ctx.stroke();

                         //hand
            ctx.beginPath();
                         ctx.moveTo(250, 120); //starting point
                         ctx.lineTo(300, 200); //end point
            ctx.stroke();

            ctx.beginPath();
                         ctx.moveTo(250, 120); //starting point
                         ctx.lineTo(200, 200); //end point
            ctx.stroke();

                         //foot
            ctx.beginPath();
                         ctx.moveTo(250, 250); //starting point
                         ctx.lineTo(300, 350); //end point
            ctx.stroke();

            ctx.beginPath();
                         ctx.moveTo(250, 250); //starting point
                         ctx.lineTo(200, 350); //end point
            ctx.stroke();
            }
            draw2();
    </script>
        }
 */

/*
        Canvas canvas = new Canvas(300, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);

canvas.drawCircle(2,2,1,null);*/



        canvas.drawRect(
                joueur.colonne * taille +margin,
                joueur.ligne * taille +margin,
                (joueur.colonne +1)* taille -margin,
                (joueur.ligne +1)* taille -margin,
                joueurDesign);
        canvas.drawRect(
                exit.colonne * taille +margin,
                exit.ligne * taille +margin,
                (exit.colonne +1)* taille -margin,
                (exit.ligne +1)* taille -margin,
                goal);
    }





    private  void movePlayer(Direction direction){
        switch (direction){
            case NORD:
                if(!joueur.haut)
                    joueur = cells[joueur.colonne][joueur.ligne -1];
                break;
            case SUD:
                if(!joueur.bas)
                    joueur = cells[joueur.colonne][joueur.ligne +1];
                break;
            case OUEST:
                if(!joueur.gauche)
                    joueur = cells[joueur.colonne -1][joueur.ligne];
                break;
            case EST:
                if(!joueur.droite)
                    joueur = cells[joueur.colonne +1][joueur.ligne];
                break;

        }
        checkExit();
        invalidate();
    }

    private void checkExit(){
        if(joueur==exit)
            create();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_DOWN)
            return  true;

        if(event.getAction()== MotionEvent.ACTION_MOVE){
            float x=event.getX();
            float y=event.getY();

            float playerCenterX= hMargin+(joueur.colonne +0.5f)* taille;
            float playerCenterY= vMargin+(joueur.ligne +0.5f)* taille;

            float dx=x-playerCenterX;
            float dy=y-playerCenterY;

            float absDx= Math.abs(dx);
            float absDy=Math.abs(dy);






            if(absDx > taille || absDy > taille){
                if(absDx>absDy){
                    //move in x direction
                    if(dx>0)
                        movePlayer(Direction.EST);
                    else
                        movePlayer(Direction.OUEST);
                }else{
                    //move in y direction
                    if(dy>0)
                        movePlayer(Direction.SUD);
                    else
                        movePlayer(Direction.NORD);
                }
            }
            return true;
        }


        return super.onFilterTouchEventForSecurity(event);
    }

    // une classe prive
    private class Cell{

        int colonne;
        int ligne;
        boolean haut = true;
        boolean gauche = true;
        boolean bas = true;
        boolean droite =true;
        boolean cellulePasse = false;

        public Cell(int colonne, int ligne) {
            this.colonne = colonne;
            this.ligne = ligne;
        }
    }

}
