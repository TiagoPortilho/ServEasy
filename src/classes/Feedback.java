package classes;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
    public List<String> feedback = new ArrayList<>();

    public void addList(String _feedback){
        feedback.add(_feedback);
    }
}
