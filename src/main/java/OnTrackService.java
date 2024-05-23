import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OnTrackService {

    // Task Service
    public static class TaskService {
        private Map<Integer, Task> tasks = new HashMap<>();
        private int taskIdCounter = 1;

        public Task createTask(String title, String description, String creator) {
            Task task = new Task(taskIdCounter++, title, description, creator);
            tasks.put(task.getId(), task);
            return task;
        }

        public boolean addCollaborator(int taskId, String collaborator) {
            Task task = tasks.get(taskId);
            if (task != null) {
                return task.addCollaborator(collaborator);
            }
            return false;
        }
    }

    public static class Task {
        private int id;
        private String title;
        private String description;
        private String creator;
        private List<String> collaborators = new ArrayList<>();

        public Task(int id, String title, String description, String creator) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.creator = creator;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getCreator() {
            return creator;
        }

        public boolean addCollaborator(String collaborator) {
            if (!collaborators.contains(collaborator)) {
                collaborators.add(collaborator);
                return true;
            }
            return false;
        }

        public List<String> getCollaborators() {
            return collaborators;
        }
    }

    // Feedback Service
    public static class FeedbackService {
        private Map<Integer, Feedback> feedbacks = new HashMap<>();
        private int feedbackIdCounter = 1;

        public Feedback provideFeedback(int taskId, String tutor, String comments) {
            Feedback feedback = new Feedback(feedbackIdCounter++, taskId, tutor, comments);
            feedbacks.put(feedback.getId(), feedback);
            return feedback;
        }
    }

    public static class Feedback {
        private int id;
        private int taskId;
        private String tutor;
        private String comments;

        public Feedback(int id, int taskId, String tutor, String comments) {
            this.id = id;
            this.taskId = taskId;
            this.tutor = tutor;
            this.comments = comments;
        }

        public int getId() {
            return id;
        }

        public int getTaskId() {
            return taskId;
        }

        public String getTutor() {
            return tutor;
        }

        public String getComments() {
            return comments;
        }
    }

    // Tutoring Service
    public static class TutoringService {
        private Map<Integer, Session> sessions = new HashMap<>();
        private int sessionIdCounter = 1;

        public Session scheduleSession(String tutor, String student, Date date, String time) {
            if (!isValidTime(time)) {
                throw new IllegalArgumentException("Invalid time format");
            }
            Session session = new Session(sessionIdCounter++, tutor, student, date, time);
            sessions.put(session.getId(), session);
            return session;
        }

        private boolean isValidTime(String time) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            sdf.setLenient(false);
            try {
                sdf.parse(time);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
    }

    public static class Session {
        private int id;
        private String tutor;
        private String student;
        private Date date;
        private String time;

        public Session(int id, String tutor, String student, Date date, String time) {
            this.id = id;
            this.tutor = tutor;
            this.student = student;
            this.date = date;
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public String getTutor() {
            return tutor;
        }

        public String getStudent() {
            return student;
        }

        public Date getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }
    }

    // Study Group Service
    public static class StudyGroupService {
        private Map<Integer, StudyGroup> studyGroups = new HashMap<>();
        private int groupIdCounter = 1;

        public StudyGroup createStudyGroup(String groupName, String creator) {
            StudyGroup group = new StudyGroup(groupIdCounter++, groupName, creator);
            studyGroups.put(group.getId(), group);
            return group;
        }

        public boolean joinStudyGroup(String student, int groupId) {
            StudyGroup group = studyGroups.get(groupId);
            if (group != null) {
                return group.addMember(student);
            }
            return false;
        }
    }

    public static class StudyGroup {
        private int id;
        private String groupName;
        private String creator;
        private Set<String> members = new HashSet<>();

        public StudyGroup(int id, String groupName, String creator) {
            this.id = id;
            this.groupName = groupName;
            this.creator = creator;
            members.add(creator);
        }

        public int getId() {
            return id;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getCreator() {
            return creator;
        }

        public boolean addMember(String member) {
            return members.add(member);
        }

        public Set<String> getMembers() {
            return members;
        }
    }

    // Progress Report Service
    public static class ProgressReportService {
        private Map<String, ProgressReport> reports = new HashMap<>();

        public ProgressReport generateReport(String student) {
            ProgressReport report = new ProgressReport(student, 90, 20);
            reports.put(student, report);
            return report;
        }
    }

    public static class ProgressReport {
        private String student;
        private int averageScore;
        private int tasksCompleted;

        public ProgressReport(String student, int averageScore, int tasksCompleted) {
            this.student = student;
            this.averageScore = averageScore;
            this.tasksCompleted = tasksCompleted;
        }

        public String getStudent() {
            return student;
        }

        public int getAverageScore() {
            return averageScore;
        }

        public int getTasksCompleted() {
            return tasksCompleted;
        }
    }

    // Tutor Feedback Service
    public static class TutorFeedbackService {
        private Map<String, List<FeedbackForm>> feedbacks = new HashMap<>();

        public void submitFeedback(String tutor, FeedbackForm feedback) {
            feedbacks.computeIfAbsent(tutor, k -> new ArrayList<>()).add(feedback);
        }

        public List<FeedbackForm> getFeedback(String tutor) {
            return feedbacks.getOrDefault(tutor, new ArrayList<>());
        }
    }

    public static class FeedbackForm {
        private String tutor;
        private String student;
        private int rating;
        private String comments;

        public FeedbackForm(String tutor, String student, int rating, String comments) {
            this.tutor = tutor;
            this.student = student;
            this.rating = rating;
            this.comments = comments;
        }

        public String getTutor() {
            return tutor;
        }

        public String getStudent() {
            return student;
        }

        public int getRating() {
            return rating;
        }

        public String getComments() {
            return comments;
        }
    }
}
