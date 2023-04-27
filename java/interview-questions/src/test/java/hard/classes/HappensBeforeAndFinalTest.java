package hard.classes;

public class HappensBeforeAndFinalTest {
    public final int finalVar;
    public int nonFinalVar = -1;

    public HappensBeforeAndFinalTest(int finalVar, int nonFinalVar) {
        this.finalVar = finalVar;
        this.nonFinalVar = nonFinalVar;
    }
}
