package codingweek;

public class Player {
    /*Classe relative au joueur de la fonctionnalité multijoueur */
    private String name;
    private int score;
    private String imagePath;

    public Player(String name, String imagePath){
        this.name = name;
        this.score = 0;
        this.imagePath = imagePath;
    }

    public String getName(){
        return this.name;
    }

    public int getScore(){
        return this.score;
    }

    public String getImagePath(){
        return this.imagePath;
    }

    public void addPoint(){
        this.score += 1;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public String toString(){
        return this.name + " : " + this.score;
    }
    
}
