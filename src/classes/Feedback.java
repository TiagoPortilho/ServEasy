package classes;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
    public List<String> feedback = new ArrayList<>();

    public void addList(String _feedback){
        feedback.add(_feedback);
    }

    public void verFeedbacks(){
        int count = 0;
        for (String _feedback : feedback){
            System.out.println("\nFeedback - " + count);
            System.out.println(_feedback);
            count++;
        }
    }
}
