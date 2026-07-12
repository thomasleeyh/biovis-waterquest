package service;

import model.Question;import java.util.List;
public final class QuestionLoader {private QuestionLoader(){} public static List<Question> load(){return List.of(
 q("How many hydrogen atoms are in one water molecule?",1,"One oxygen atom bonds with two hydrogen atoms.","1","2","3","4"),
 q("What is the H–O–H bond angle?",1,"Water's bent molecular angle is 104.5°.","90°","104.5°","120°","180°"),
 q("Which attraction links neighbouring water molecules?",2,"Partial charges allow hydrogen bonds to form.","Ionic bond","Covalent bond","Hydrogen bond","Peptide bond"),
 q("Why can water dissolve sodium chloride?",0,"Polar water forms hydration shells that separate ions.","Hydration shells separate ions","Water is non-polar","Salt evaporates","Oxygen reacts with sodium"),
 q("Which end of water points towards Na⁺?",1,"The partially negative oxygen faces a positive ion.","Hydrogen","Oxygen","Neither","Both equally"),
 q("At the beach, which heats faster?",0,"Sand has a lower specific heat capacity than water.","Sand","Sea water","Both equally","Neither"),
 q("Why does water warm relatively slowly?",2,"Much energy is needed for a small temperature rise.","It is always cold","It has low density","It has high specific heat capacity","It cannot absorb heat"),
 q("Why does sweating cool the body?",1,"Evaporation absorbs latent heat from the skin.","Sweat freezes","Evaporation absorbs heat","Water produces cold","Salt blocks heat"),
 q("Where is liquid water most dense?",2,"Water reaches maximum density at 4°C.","0°C","2°C","4°C","100°C"),
 q("Why does ice float?",0,"Its open crystal lattice makes ice less dense than liquid water.","Ice is less dense","Ice is warmer","Ice contains air only","Water repels ice"),
 q("What causes cohesion between water molecules?",3,"Hydrogen bonds create water-water attraction.","Gravity","Covalent bonds between molecules","Ionic bonds","Hydrogen bonds"),
 q("What is the shape of a water molecule?",1,"Two lone pairs produce a bent shape.","Linear","Bent","Trigonal planar","Tetrahedral molecule") );}
 private static Question q(String p,int answer,String e,String...choices){return new Question(p,List.of(choices),answer,e);}}
