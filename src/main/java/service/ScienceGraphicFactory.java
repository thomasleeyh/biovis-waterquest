package service;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

/** Scalable, animated diagrams based on the supplied SB015 lecture notes. */
public final class ScienceGraphicFactory {
    private static final Color O=Color.web("#ff6685"),H=Color.web("#83eaff"),W=Color.web("#43cce8");
    private ScienceGraphicFactory(){}

    /** Returns the supplied generated topic artwork; notes deliberately have no zoom gesture. */
    public static Node forTopic(String id){return imageTopic(id);}
    public static Node forQuestion(String prompt){String p=prompt.toLowerCase();String id=p.contains("sodium")||p.contains("chloride")||p.contains("na⁺")?"solvent":p.contains("sweat")||p.contains("evaporation")?"vaporisation":p.contains("dense")||p.contains("ice")?"density":p.contains("cohesion")?"cohesion":p.contains("sand")||p.contains("warm")||p.contains("specific heat")?"heat":"molecule";return build(id);}
    private static Node build(String id){return switch(id){case"molecule"->molecule();case"solvent"->hydration();case"heat"->breezes();case"vaporisation"->sweating();case"density"->density();case"cohesion"->surfaceTension();default->canvas();};}

    private static Node imageTopic(String id){
        int number=switch(id){case "molecule"->3;case "solvent"->1;case "heat"->2;case "vaporisation"->4;case "density"->5;case "cohesion"->6;default->1;};
        StackPane frame=new StackPane();frame.setMinHeight(285);frame.setPrefHeight(285);frame.setMaxWidth(Double.MAX_VALUE);frame.getStyleClass().add("image-topic-frame");
        var stream=ScienceGraphicFactory.class.getResourceAsStream("/images/Generated image "+number+".png");
        if(stream!=null){ImageView image=new ImageView(new Image(stream));image.setFitWidth(790);image.setFitHeight(270);image.setPreserveRatio(true);image.setSmooth(true);frame.getChildren().add(image);}
        else frame.getChildren().add(build(id));
        return frame;
    }

    private static Node zoomable(Node graphic){StackPane frame=new StackPane(graphic);frame.setMinHeight(285);frame.setPrefHeight(285);frame.getStyleClass().add("zoom-frame");frame.setCursor(Cursor.HAND);graphic.setScaleX(1.22);graphic.setScaleY(1.22);Rectangle clip=new Rectangle(1000,285);clip.setArcWidth(28);clip.setArcHeight(28);frame.setClip(clip);Label hint=tag("CLICK TO ZOOM • GENERAL ↔ DETAIL");StackPane.setAlignment(hint,Pos.TOP_RIGHT);StackPane.setMargin(hint,new javafx.geometry.Insets(12));frame.getChildren().add(hint);final boolean[] zoomed={false};frame.setOnMouseClicked(e->{zoomed[0]=!zoomed[0];ScaleTransition s=new ScaleTransition(Duration.millis(420),graphic);s.setToX(zoomed[0]?1.7:1.22);s.setToY(zoomed[0]?1.7:1.22);s.setInterpolator(Interpolator.EASE_BOTH);s.play();hint.setText(zoomed[0]?"CLICK TO RETURN TO GENERAL VIEW":"CLICK TO ZOOM • GENERAL ↔ DETAIL");});Tooltip.install(frame,new Tooltip("Click to smoothly zoom between the full diagram and molecular detail"));return frame;}
    private static Pane canvas(){Pane p=new Pane();p.setPrefSize(760,205);p.setMinHeight(205);p.getStyleClass().add("science-graphic");return p;}

    private static Node molecule(){
        Pane p=canvas();
        ImageView illustration=new ImageView(new Image(ScienceGraphicFactory.class.getResourceAsStream("/images/water-molecule-animated-panel.png")));
        illustration.setFitWidth(760); illustration.setFitHeight(205); illustration.setPreserveRatio(false); illustration.setOpacity(.94);
        FadeTransition glow=new FadeTransition(Duration.seconds(2.2),illustration);glow.setFromValue(.78);glow.setToValue(1);glow.setAutoReverse(true);glow.setCycleCount(Animation.INDEFINITE);glow.play();
        p.getChildren().add(illustration);
        Label oxygen=label("O  (δ−)","image-atom-label");oxygen.relocate(357,91);
        Label hydrogenLeft=label("H  (δ+)","image-hydrogen-label");hydrogenLeft.relocate(294,145);
        Label hydrogenRight=label("H  (δ+)","image-hydrogen-label");hydrogenRight.relocate(445,145);
        Label caption=tag("H₂O • BENT SHAPE • 104.5° • FOUR HYDROGEN-BOND PARTNERS");caption.relocate(16,14);
        p.getChildren().addAll(oxygen,hydrogenLeft,hydrogenRight,caption);return p;
    }
    private static Node hydration(){Pane p=canvas();p.getChildren().addAll(ion("Na⁺",205,105,"positive-ion"),ion("Cl⁻",555,105,"negative-ion"));for(int i=0;i<8;i++){double a=i*Math.PI/4;p.getChildren().add(fullMiniWater(205+78*Math.cos(a),105+78*Math.sin(a),Math.toDegrees(a)+180,true));p.getChildren().add(fullMiniWater(555+78*Math.cos(a),105+78*Math.sin(a),Math.toDegrees(a)+180,false));}Label note=tag("HYDRATION SHELLS: Oδ− FACES Na⁺ • TWO Hδ+ FACE Cl⁻");note.relocate(190,14);p.getChildren().add(note);return p;}
    private static Node breezes(){Pane p=canvas();panel(p,20,"DAY • SEA BREEZE",true);panel(p,390,"NIGHT • LAND BREEZE",false);return p;}
    private static void panel(Pane p,double x,String title,boolean day){Rectangle land=new Rectangle(x+20,125,150,38);land.setFill(Color.web("#d9a760"));Rectangle sea=new Rectangle(x+170,125,150,38);sea.setFill(W);p.getChildren().addAll(land,sea);Label t=tag(title);t.relocate(x+95,18);Label l=label("LAND", "graphic-caption");l.relocate(x+74,168);Label s=label("SEA", "graphic-caption");s.relocate(x+226,168);p.getChildren().addAll(t,l,s);double from=day?x+250:x+95,to=day?x+95:x+250;Line wind=arrow(from,92,to,92);p.getChildren().add(wind);Label flow=label(day?"cool air → land":"cool air → sea","graphic-caption");flow.relocate(x+112,72);p.getChildren().add(flow);Circle sky=new Circle(x+300,43,14,day?Color.web("#ffd35c"):Color.web("#dcecff"));p.getChildren().add(sky);}
    private static Node sweating(){Pane p=canvas();Circle head=new Circle(375,62,30,Color.web("#d89574"));Line body=new Line(375,92,375,158);Line arm1=new Line(375,105,325,137),arm2=new Line(375,105,425,137);Line leg1=new Line(375,158,340,190),leg2=new Line(375,158,410,190);for(Line line:new Line[]{body,arm1,arm2,leg1,leg2}){line.getStyleClass().add("person-line");}p.getChildren().addAll(head,body,arm1,arm2,leg1,leg2);for(int i=0;i<5;i++){Circle d=new Circle(325+i*25,48+(i%2)*20,7,W);p.getChildren().add(d);TranslateTransition t=new TranslateTransition(Duration.seconds(1.4+i*.15),d);t.setByY(-32);t.setAutoReverse(true);t.setCycleCount(Animation.INDEFINITE);t.play();}for(int i=0;i<3;i++)p.getChildren().add(arrow(250+i*125,145,250+i*125,83));Label heat=tag("AFTER EXERCISE: SWEAT ABSORBS BODY HEAT AND EVAPORATES");heat.relocate(175,15);Label cool=label("BODY HEAT ↓  •  OVERHEATING PREVENTED", "graphic-caption");cool.relocate(245,178);p.getChildren().addAll(heat,cool);return p;}
    private static Node density(){Pane p=canvas();Rectangle top=new Rectangle(45,34,670,68);top.setFill(Color.web("#dffaff33"));Rectangle bottom=new Rectangle(45,105,670,75);bottom.setFill(Color.web("#168daf88"));p.getChildren().addAll(top,bottom);double[][] loose={{100,58},{185,82},{285,54},{390,80},{510,55},{635,82}};for(double[]a:loose)p.getChildren().add(moleculeDot(a[0],a[1]));for(int row=0;row<3;row++)for(int col=0;col<12;col++)p.getChildren().add(moleculeDot(75+col*55+(row%2)*20,120+row*23));Label ice=tag("ICE • OPEN LATTICE • LOOSELY PACKED • LESS DENSE");ice.relocate(185,38);Label liquid=tag("LIQUID WATER AT 4°C • CLOSELY PACKED • MAXIMUM DENSITY");liquid.relocate(155,153);p.getChildren().addAll(ice,liquid);return p;}
    private static Node surfaceTension(){Pane p=canvas();Label air=tag("AIR / ENVIRONMENT • NO WATER MOLECULES ABOVE");air.relocate(220,12);p.getChildren().add(air);for(int row=0;row<3;row++)for(int col=0;col<8;col++){double x=95+col*82,y=72+row*52;p.getChildren().add(moleculeDot(x,y));if(col<7){Line hb=new Line(x+18,y,x+64,y);hb.getStyleClass().add("hydrogen-bond");p.getChildren().add(hb);}if(row>0){Line up=new Line(x,y-18,x,y-34);up.getStyleClass().add("balanced-force");p.getChildren().add(up);}if(row==0)p.getChildren().add(arrow(x,y-18,x,y+17));}Label top=label("UNEQUAL ATTRACTION → NET INWARD FORCE → HIGH SURFACE TENSION","surface-label");top.relocate(150,42);Label body=label("BODY OF WATER: MOLECULES ATTRACTED EQUALLY IN ALL DIRECTIONS","graphic-caption");body.relocate(180,183);p.getChildren().addAll(top,body);return p;}

    private static Node water(double x,double y,double scale){Pane g=new Pane();g.setPrefSize(100,80);Line l1=new Line(50,31,22,62),l2=new Line(50,31,78,62);l1.getStyleClass().add("mini-bond");l2.getStyleClass().add("mini-bond");Circle o=new Circle(50,28,20,O),h1=new Circle(18,65,13,H),h2=new Circle(82,65,13,H);Label oxygen=label("O","molecule-oxygen-label");oxygen.relocate(44,17);Label hydrogen1=label("H","molecule-hydrogen-label");hydrogen1.relocate(13,57);Label hydrogen2=label("H","molecule-hydrogen-label");hydrogen2.relocate(77,57);Label neg=label("δ−  δ−","tiny-charge");neg.relocate(28,0);Label pos=label("δ+                 δ+","tiny-charge");pos.relocate(1,70);g.getChildren().addAll(l1,l2,o,h1,h2,oxygen,hydrogen1,hydrogen2,neg,pos);g.setScaleX(scale);g.setScaleY(scale);g.relocate(x-50,y-40);return g;}
    private static Node fullMiniWater(double x,double y,double rotation,boolean oxygenInward){Node n=water(x,y,.38);n.setRotate(rotation+(oxygenInward?0:180));return n;}
    private static StackPane moleculeDot(double x,double y){Circle c=new Circle(15,W);c.getStyleClass().add("graphic-atom");Label l=label("H₂O","dot-label");StackPane s=new StackPane(c,l);s.relocate(x-15,y-15);return s;}
    private static StackPane ion(String text,double x,double y,String style){Circle c=new Circle(27);c.getStyleClass().add(style);StackPane s=new StackPane(c,label(text,"ion-label"));s.relocate(x-27,y-27);return s;}
    private static Line arrow(double x1,double y1,double x2,double y2){Line l=new Line(x1,y1,x2,y2);l.getStyleClass().add("force-arrow");return l;}
    private static Label tag(String t){return label(t,"graphic-tag");}private static Label label(String t,String c){Label l=new Label(t);l.getStyleClass().add(c);return l;}
}
