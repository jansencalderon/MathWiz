package project.mathwizv2;

/**
 * Created by Jansen on 6/29/2016.
 */
public class ButtonObject {
    private String title;
    private int position_x;
    private int position_y;


    public ButtonObject(String title, int position_x,int position_y){
        this.title = title;
        this.position_x = position_x;
        this.position_y = position_y;
    }

    public int getPosition_x() {
        return position_x;
    }

    public int getPosition_y() {
        return position_y;
    }

    public String getTitle() {
        return title;
    }

    public void setPosition_x(int position_x) {
        this.position_x = position_x;
    }

    public void setPosition_y(int position_y) {
        this.position_y = position_y;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
