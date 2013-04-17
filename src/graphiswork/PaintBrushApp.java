/*Required updates:
 * 1. fix the clear operation(clicking on the new menu)
 * 2.save the   workout in panel as image.
 * 3.change the look of mouse pointer after clicking brash button to brash and so on.
 * 4. Use defaultToolkit
 * 5. Fix unnecessary complexities
 */
package graphiswork;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public final class PaintBrushApp extends JFrame{

    JButton BrashBut, EllipseBut, RectangleBut, LineBut, StrokeBut, FillBut,clear;
    int recentActionValue = 1;
    Color fillColor = Color.BLACK;
   Color strokeColor;
    JLabel transLabel;
    JSlider tranSlider;
    DecimalFormat dec=new DecimalFormat("#.##");
    Graphics2D graphicalDrawing;

float transparentVal=1.0f;


PaintBrushApp() {
        
    JFrame frame=new JFrame();
    
    final Container content = frame.getContentPane();
    
    setIconImage(new ImageIcon("./src/images/defaultIcon.png").getImage());
    setTitle("PaintBrush");
        setSize(1800,750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
     

        JPanel thePanel = new JPanel();
       
        JMenuBar mb= new JMenuBar();
        JMenu graphicsMenu=new JMenu("File");
        JMenu helpMenu=new JMenu("Help");
        mb.add(graphicsMenu);
        mb.add(helpMenu);
     setJMenuBar(mb);
      JMenuItem item1=new JMenuItem("New");   
        JMenuItem item2=new JMenuItem("How To Draw");    
         graphicsMenu.add(item1);
         helpMenu.add(item2);
         item1.addActionListener(new ActionListener(){
             
             public void actionPerformed(ActionEvent e){
              
             }
         });
         
         
         
       	final Canvas drawPad = new Canvas();
         
         Box theBox = Box.createVerticalBox();


        BrashBut = addImageToButtons("./src/images/brushIcon.png", 1);
        EllipseBut = addImageToButtons("./src/images/Ellipse.png", 3);
        RectangleBut = addImageToButtons("./src/images/Rectangle.png", 4);
        LineBut = addImageToButtons("./src/images/Line.png", 2);

        FillBut = addImageToColorButtons("./src/images/Fill.png", 5, true);
        StrokeBut = addImageToColorButtons("./src/images/Stroke.png", 7, false);
        clear=new JButton("CLEAR");
        BrashBut.setToolTipText("Brash");
        RectangleBut .setPreferredSize(new Dimension(2, 26));
        EllipseBut.setToolTipText("Ellise");
        RectangleBut.setToolTipText("Rectangle");
        LineBut.setToolTipText("Line");
        FillBut.setToolTipText("Fill");
        StrokeBut.setToolTipText("Stroke");

        theBox.add(BrashBut);
        theBox.add(LineBut);
        theBox.add(EllipseBut);
        theBox.add(RectangleBut);

        theBox.add(StrokeBut);
        theBox.add(FillBut);
         theBox.add(clear);
        transLabel = new JLabel("Transparency: 1 ");
        tranSlider = new JSlider(1, 99, 99);

        ListenForSlider lSlider = new ListenForSlider();
        tranSlider.addChangeListener(lSlider);
        


        theBox.add(transLabel);
        theBox.add(tranSlider);

       

        thePanel.add(theBox);
      thePanel.setBackground(Color.CYAN);
    content.add(drawPad, BorderLayout.CENTER);
     content.setBackground(Color.WHITE); 
     
      content.add(thePanel, BorderLayout.WEST);
    add(content);
   
    //content.add(panel, BorderLayout.CENTER);
    
    clear.addActionListener(new ActionListener(){
        
        public void actionPerformed(ActionEvent e){
   if(e.getSource()==clear){  
       drawPad.clear();
   } 
        }
    });

    }

    public JButton addImageToButtons(String iconfile, final int activeNumber) {

        JButton thebut = new JButton();
        Icon buttonicon = new ImageIcon(iconfile);
        thebut.setIcon(buttonicon);
        thebut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                recentActionValue = activeNumber;
               
            }
        });



        return thebut;

    }

    public JButton addImageToColorButtons(String iconfile, final int activeNumber, final boolean stroke) {

        JButton thebut = new JButton();
        Icon buttonicon = new ImageIcon(iconfile);
        thebut.setIcon(buttonicon);
        thebut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


                if (stroke) {
                    strokeColor = JColorChooser.showDialog(null, "choose a color", Color.BLACK);
                } else {
                    fillColor = JColorChooser.showDialog(null, "choose a color", Color.BLACK);
                }

            }
        });
        return thebut;

    }

    /**
     *
     */
    public class Canvas extends JComponent {
    

        ArrayList<Shape> shapes = new ArrayList<Shape>();
        ArrayList<Color> shapeFill = new ArrayList<Color>();
        ArrayList<Color> shapeStroke = new ArrayList<Color>();
        ArrayList<Float> transPersent = new ArrayList<Float>();
        Point drawingStartingPoint, drawingEndingPoint;

      public  Canvas() {
       
          this.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                 if(recentActionValue!=1){  
                    
                    
                    drawingStartingPoint = new Point(e.getX(), e.getY());
                    drawingEndingPoint=drawingStartingPoint;
                    repaint();
                }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    
                 if(recentActionValue!=1){
                    
                     Shape aShape=null;
                     if(recentActionValue==2){
                     aShape=drawLine(drawingStartingPoint.x ,drawingStartingPoint.y,e.getX(),e.getY());
                 }
     else if(recentActionValue==3){

     aShape=drawEllipse(drawingStartingPoint.x ,drawingStartingPoint.y,e.getX(),e.getY());
 }

     else if(recentActionValue==4){
                      aShape = drawRectangle(drawingStartingPoint.x, drawingStartingPoint.y, e.getX(), e.getY());
                  }

                    shapes.add(aShape);
                    shapeFill.add(fillColor);
                    shapeStroke.add(strokeColor);
                    transPersent.add(transparentVal);


                    drawingStartingPoint = null;
                    drawingEndingPoint= null;
                    repaint();
                }

                }
       });

           this.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {

                if(recentActionValue==1){

                    int x=e.getX();
                    int y=e.getY();

                  Shape aShape=null;
                  strokeColor=fillColor;
                  aShape=drawBrush(x,y,5,5);

                    shapes.add(aShape);
                    shapeFill.add(fillColor);
                    shapeStroke.add(strokeColor);
                    transPersent.add(transparentVal);
                    }

                  drawingEndingPoint= new Point(e.getX(), e.getY());
                    repaint();
                    }
                
            });
        }
   public void paint(Graphics g){
                

                        graphicalDrawing = (Graphics2D) g;
                        graphicalDrawing.setStroke(new BasicStroke(0));

                      graphicalDrawing.setPaint(Color.WHITE);
          Iterator<Color> strokeCounter = shapeStroke.iterator();
          Iterator<Color> fillCounter = shapeFill.iterator();
          Iterator<Float> transCounter = transPersent.iterator();


           for (Shape s : shapes) {
                graphicalDrawing.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,transCounter.next()));
                graphicalDrawing.setPaint(strokeCounter.next());
                graphicalDrawing.draw(s);
                graphicalDrawing.setPaint(fillCounter.next());
                graphicalDrawing.fill(s);

            }
                 if (drawingStartingPoint != null && drawingEndingPoint!= null) {

                 graphicalDrawing.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
                 graphicalDrawing.setPaint(Color.BLACK);

                Shape aShape = null;

                if (recentActionValue == 2) {
                    aShape = drawLine(drawingStartingPoint.x, drawingStartingPoint.y, drawingEndingPoint.x, drawingEndingPoint.y);

                                        }
                else if (recentActionValue == 3) {
                    aShape = drawEllipse(drawingStartingPoint.x, drawingStartingPoint.y, drawingEndingPoint.x, drawingEndingPoint.y);
                                           }
                else if (recentActionValue == 4) {
                    aShape = drawRectangle(drawingStartingPoint.x, drawingStartingPoint.y, drawingEndingPoint.x, drawingEndingPoint.y);
                }

                graphicalDrawing.draw(aShape);

            }



        }

  public void clear(){
		graphicalDrawing.setPaint(Color.white);
		graphicalDrawing.fillRect(0, 0, getSize().width, getSize().height);
		graphicalDrawing.setPaint(Color.black);
		repaint();
	}
   
   
   
   
   
   
   
   
   private Rectangle2D.Float drawRectangle(int x1,int y1,int x2,int y2){

         int x= Math.min(x1, x2);
         int y= Math.min(y1, y2);

         int width=Math.abs(x1-x2);
         int height=Math.abs(y1-y2);

    return new Rectangle2D.Float(x,y,width,height);

    }

  private Ellipse2D.Float drawEllipse(int x1,int y1,int x2,int y2){

         int x= Math.min(x1, x2);
         int y= Math.min(y1, y2);

         int width=Math.abs(x1-x2);
         int height=Math.abs(y1-y2);

    return new Ellipse2D.Float(x,y,width,height);

  }

  private Line2D.Float drawLine(int x1,int y1,int x2,int y2){

             return new Line2D.Float(x1,y1,x2,y2);
}

  private Ellipse2D.Float drawBrush(int x1,int y1,int brashStrokeWeidth,int brushStrokeHeight){

            return new Ellipse2D.Float(x1,y1,brashStrokeWeidth,brushStrokeHeight);

  }


  }

  public class ListenForSlider implements ChangeListener{
      
      
        @Override
      public void stateChanged(ChangeEvent e){
          
    if(e.getSource()==tranSlider){
        transLabel.setText("Transparent :"+dec.format(tranSlider.getValue()* .01));
   
    
    transparentVal=(float)(tranSlider.getValue()* .01);
    
    }
      
      
      
      }
      
  } 
    
    
    
    
    
    public static void main(String[] args) {

        new PaintBrushApp().setVisible(true);;
    }
}
