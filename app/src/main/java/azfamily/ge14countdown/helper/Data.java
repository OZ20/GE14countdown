package azfamily.ge14countdown.helper;

/**
 * Created by Faris on 14/4/2018.
 */

public class Data {

    float[] id = new float[0];
    String[] choice = new String[0];

    public Data(float[] id, String[] choice) {
        this.id = id;
        this.choice = choice;
    }

    public float[] getId() {
        return id;
    }

    public void setId(float[] id) {
        this.id = id;
    }

    public String[] getChoice() {
        return choice;
    }

    public void setChoice(String[] choice) {
        this.choice = choice;
    }
}
