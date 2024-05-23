import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OnTrackServiceTest {

    // TaskService Tests
    @Test
    public void testCreateTask() {
        OnTrackService.TaskService taskService = new OnTrackService.TaskService();
        OnTrackService.Task task = taskService.createTask("Title", "Description", "Creator");
        assertNotNull(task);
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals("Creator", task.getCreator());
    }

    @Test
    public void testAddCollaborator() {
        OnTrackService.TaskService taskService = new OnTrackService.TaskService();
        OnTrackService.Task task = taskService.createTask("Title", "Description", "Creator");
        assertTrue(taskService.addCollaborator(task.getId(), "Collaborator"));
        assertEquals(1, task.getCollaborators().size());
    }

    // Boundary condition test
    @Test
    public void testAddDuplicateCollaborator() {
        OnTrackService.TaskService taskService = new OnTrackService.TaskService();
        OnTrackService.Task task = taskService.createTask("Title", "Description", "Creator");
        assertTrue(taskService.addCollaborator(task.getId(), "Collaborator"));
        assertFalse(taskService.addCollaborator(task.getId(), "Collaborator")); // Should not add duplicate
        assertEquals(1, task.getCollaborators().size());
    }

    // FeedbackService Tests
    @Test
    public void testProvideFeedback() {
        OnTrackService.FeedbackService feedbackService = new OnTrackService.FeedbackService();
        OnTrackService.Feedback feedback = feedbackService.provideFeedback(1, "Tutor", "Comments");
        assertNotNull(feedback);
        assertEquals("Comments", feedback.getComments());
    }

    // Inverse relationship test
    @Test
    public void testFeedbackInverse() {
        OnTrackService.FeedbackService feedbackService = new OnTrackService.FeedbackService();
        OnTrackService.Feedback feedback = feedbackService.provideFeedback(1, "Tutor", "Comments");
        assertNotNull(feedback);
        assertEquals("Comments", feedback.getComments());
    }

    // TutoringService Tests
    @Test
    public void testScheduleSession() {
        OnTrackService.TutoringService tutoringService = new OnTrackService.TutoringService();
        Date date = new Date();
        OnTrackService.Session session = tutoringService.scheduleSession("Tutor", "Student", date, "10:00 AM");
        assertNotNull(session);
        assertEquals("Tutor", session.getTutor());
        assertEquals("Student", session.getStudent());
        assertEquals(date, session.getDate());
        assertEquals("10:00 AM", session.getTime());
    }

    // Error condition test
    @Test(expected = IllegalArgumentException.class)
    public void testScheduleSessionWithInvalidTime() {
        OnTrackService.TutoringService tutoringService = new OnTrackService.TutoringService();
        Date date = new Date();
        tutoringService.scheduleSession("Tutor", "Student", date, "25:00 PM"); // Invalid time
    }

    // StudyGroupService Tests
    @Test
    public void testCreateStudyGroup() {
        OnTrackService.StudyGroupService studyGroupService = new OnTrackService.StudyGroupService();
        OnTrackService.StudyGroup group = studyGroupService.createStudyGroup("Math Group", "Creator");
        assertNotNull(group);
        assertEquals("Math Group", group.getGroupName());
        assertEquals("Creator", group.getCreator());
        assertTrue(group.getMembers().contains("Creator"));
    }

    @Test
    public void testJoinStudyGroup() {
        OnTrackService.StudyGroupService studyGroupService = new OnTrackService.StudyGroupService();
        OnTrackService.StudyGroup group = studyGroupService.createStudyGroup("Math Group", "Creator");
        assertTrue(studyGroupService.joinStudyGroup("NewMember", group.getId()));
        assertTrue(group.getMembers().contains("NewMember"));
    }

    // ProgressReportService Tests
    @Test
    public void testGenerateReport() {
        OnTrackService.ProgressReportService reportService = new OnTrackService.ProgressReportService();
        OnTrackService.ProgressReport report = reportService.generateReport("Student1");
        assertNotNull(report);
        assertEquals("Student1", report.getStudent());
        assertEquals(90, report.getAverageScore());
        assertEquals(20, report.getTasksCompleted());
    }

    // Performance test
    @Test(timeout = 1000)
    public void testGenerateReportPerformance() {
        OnTrackService.ProgressReportService reportService = new OnTrackService.ProgressReportService();
        reportService.generateReport("Student1");
    }

    // TutorFeedbackService Tests
    @Test
    public void testSubmitFeedback() {
        OnTrackService.TutorFeedbackService feedbackService = new OnTrackService.TutorFeedbackService();
        OnTrackService.FeedbackForm feedbackForm = new OnTrackService.FeedbackForm("Tutor1", "Student1", 5, "Excellent");
        feedbackService.submitFeedback("Tutor1", feedbackForm);
        List<OnTrackService.FeedbackForm> feedbackList = feedbackService.getFeedback("Tutor1");
        assertNotNull(feedbackList);
        assertEquals(1, feedbackList.size());
        assertEquals("Excellent", feedbackList.get(0).getComments());
    }

    // Cross-check test
    @Test
    public void testGetFeedback() {
        OnTrackService.TutorFeedbackService feedbackService = new OnTrackService.TutorFeedbackService();
        OnTrackService.FeedbackForm feedbackForm = new OnTrackService.FeedbackForm("Tutor1", "Student1", 5, "Excellent");
        feedbackService.submitFeedback("Tutor1", feedbackForm);
        feedbackForm = new OnTrackService.FeedbackForm("Tutor1", "Student2", 4, "Good");
        feedbackService.submitFeedback("Tutor1", feedbackForm);
        List<OnTrackService.FeedbackForm> feedbackList = feedbackService.getFeedback("Tutor1");
        assertNotNull(feedbackList);
        assertEquals(2, feedbackList.size());
        assertEquals("Excellent", feedbackList.get(0).getComments());
        assertEquals("Good", feedbackList.get(1).getComments());
    }
}
