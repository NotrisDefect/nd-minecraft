package in.cabbageroll.notrisdefect.minecraft;

public class SemVer implements Comparable<SemVer> {
    private final int[] values;

    public SemVer(String v) {
        String[] split = v.split("\\.");
        values = new int[3];
        for (int i = 0; i < split.length; i++) {
            values[i] = Integer.parseInt(split[i]);
        }
    }

    @Override
    public int compareTo(SemVer v2) {
        for (int i = 0; i < 3; i++) {
            if (values[i] < v2.values[i]) {
                return -1;
            } else if (values[i] > v2.values[i]) {
                return 1;
            }
        }
        return 0;
    }

    public String compareToFriendly(SemVer v2) {
        switch (compareTo(v2)) {
            case -1:
                return "outdated";
            case 0:
                return "latest";
            case 1:
                return "newer";
        }
        throw new InternalError();
    }
}
