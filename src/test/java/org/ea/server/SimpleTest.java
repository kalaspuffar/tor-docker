import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleTest { 
  @Test 
  public void isTrue() { 
    assertTrue("Must be true", true);
    IrcSender.send();
  } 
} 