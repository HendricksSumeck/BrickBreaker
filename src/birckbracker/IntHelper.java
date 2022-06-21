package birckbracker;

import java.util.Random;

public class IntHelper {
    public int GetRandomNumberFor(int min)
    {
        var random = new Random();
        int max = -1;
        return min + random.nextInt(max - min + 1);
    }
}
