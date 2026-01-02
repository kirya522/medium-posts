package tech.kirya522.cpu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.kirya522.mem.User;

public class CpuBurner {

    private boolean burning = true;

    public Long burn() {
        long count = 0;
        while (burning) {
            count += (long) Math.sqrt(System.nanoTime());

        }
        return count;
    }

    public void burnLog() {
        for (int i = 0; burning; i++) {
            var user = new User(i, "user", 1);
            ObjectMapper mapper = new ObjectMapper();
            String json = null;
            try {
                json = mapper.writeValueAsString(user);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            System.out.println(json);
        }
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }
}
