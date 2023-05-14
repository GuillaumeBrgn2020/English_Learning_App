package codingweek;

public class ToeicPartOneQuestion {

    private String pathToImage;
    private String pathToSong;
    private String goodAnswer;
    private String prop1 = "A";
    private String prop2 = "B";
    private String prop3 = "C";
    private String prop4 = "D";

    
    public ToeicPartOneQuestion(String pathToImage, String pathToSong, String goodAnswer) {
        this.pathToImage = pathToImage;
        this.pathToSong = pathToSong;
        this.goodAnswer = goodAnswer;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public String getPathToSong() {
        return pathToSong;
    }

    public void setPathToSong(String pathToSong) {
        this.pathToSong = pathToSong;
    }

    public String getGoodAnswer() {
        return goodAnswer;
    }

    public void setGoodAnswer(String goodAnswer) {
        this.goodAnswer = goodAnswer;
    }    

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public String getProp2() {
        return prop2;
    }

    public void setProp2(String prop2) {
        this.prop2 = prop2;
    }

    public String getProp3() {
        return prop3;
    }

    public void setProp3(String prop3) {
        this.prop3 = prop3;
    }

    public String getProp4() {
        return prop4;
    }

    public void setProp4(String prop4) {
        this.prop4 = prop4;
    }

    public String getAnswer(){
        return this.goodAnswer;
    }
}
