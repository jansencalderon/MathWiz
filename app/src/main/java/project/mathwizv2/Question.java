package project.mathwizv2;

import com.orm.SugarRecord;

/**
 * Created by ITSO on 5/4/2016.
 */
public class Question extends SugarRecord {

    Long id;
    String quest;
    String a;
    String b;
    String c;
    String d;
    int correct;
    int map;
    int stage;

    public Question(){


    }

    public Question(String quest, String a, String b, String c, String d, int correct, int map, int stage){

        this.quest = quest;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
        this.map = map;
        this.stage = stage;

    }

    public Long getId(){
        return this.id;

    }

    public String getQuest(){
        return this.quest;

    }

    public String getA(){
        return this.a;

    }

    public String getB(){
        return this.b;

}

    public String getC(){
        return this.c;

    }

    public String getD(){
        return this.d;

    }

    public int getCorrect(){
        return this.correct;

    }

    public int getMap(){

        return this.map;
    }

    public int getStage(){

        return this.stage;
    }

}
