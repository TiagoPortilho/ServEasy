package serveasy.model;

import java.util.List;

public class Feedback {
    private List<String> feedback;

    public Feedback(List<String> feedback) {
        this.feedback = feedback;
    }

    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }

    public void adicionarFeedback(String comentario) {
        this.feedback.add(comentario);
    }

    public void removerFeedback(String comentario) {
        this.feedback.remove(comentario);
    }
}