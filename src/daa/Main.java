package daa;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0 || hasFlag(args, "-h", "--help")) {
            printUsage();
            return;
        }
        System.out.println("DAA Assignment CLI initialized. Use --help for usage.");
    }

    private static boolean hasFlag(String[] args, String... flags) {
        for (String a : args) {
            for (String f : flags) {
                if (a.equals(f)) return true;
            }
        }
        return false;
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar daa.jar [command] [options]\n" +
                "Commands:\n" +
                "  sort --algo mergesort|quicksort --n <size> [--seed <s>] [--csv <path>]\n" +
                "  select --k <index> --n <size> [--seed <s>] [--algo mom5|quicksort] [--csv <path>]\n" +
                "  closest --n <size> [--seed <s>] [--csv <path>]\n" +
                "Options:\n" +
                "  --help, -h       Show this help.\n");
    }
}
