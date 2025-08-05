public class App {
    public static void main(String[] args) throws Exception {
        Schedule schedule = new Schedule();

        schedule.importOptions("Classes2Sem2025");
        schedule.exportSchedule("balls");
    }
}
