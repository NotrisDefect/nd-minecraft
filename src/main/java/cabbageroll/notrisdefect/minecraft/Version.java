package cabbageroll.notrisdefect.minecraft;

public class Version implements Comparable<Version> {
    private final int[] values;

    public Version(String v) {
        String[] split = v.split("\\.");
        values = new int[4];
        for (int i = 0; i < split.length; i++) {
            values[i] = Integer.parseInt(split[i]);
        }
        for (int i = split.length; i < 4; i++) {
            values[i] = 0;
        }
    }

    @Override
    public int compareTo(Version v2) {
        for (int i = 0; i < 4; i++) {
            if (values[i] < v2.values[i]) {
                return -1;
            } else if (values[i] > v2.values[i]) {
                return 1;
            }
        }
        return 0;
    }

    public String compareToFriendly(Version v2) {
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
