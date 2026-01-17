public class App {
    public static void main(String[] args) throws Exception {
        Schedule[] toExport = Schedule.sort(Schedule.importOptions("Classes2Sem2025"), 2);
        Schedule.exportSchedule("export", toExport);
    }
}
