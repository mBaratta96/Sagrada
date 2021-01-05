package it.polimi.ingsw.view.dice;

import com.jfoenix.controls.JFXSlider;

public class SliderValue extends JFXSlider{



    public SliderValue(int i, int i1){
      setMin(i);
      setMax(i1);
      setValue(i);

      setMajorTickUnit(1);
      setMinorTickCount(0);
      setShowTickMarks(true);
      setShowTickLabels(true);
      setSnapToTicks(true);

    }

    /**
     * This is a getter wich gets the number selected on the slider
     * @return number
     */
    public int getNumber(){
        return (int)getValue();
    }

}
