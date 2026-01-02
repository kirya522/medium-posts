package tech.kirya522.cpu;

public class CpuBurner {

    private boolean burning = true;

    public Long burn() {
        long count = 0;
        while (burning) {
            count += (long) Math.sqrt(System.nanoTime());

        }
        return count;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }
}
