import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class InterlockingImpl_Test
{
    @Test
    public void test_AddTrain()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 4);
        assertEquals(interlockingImpl.getSection(3), "A");
    }

//    @Test(expected = IllegalArgumentException.class)
//    public void whenExceptionThrown_thenExpectationSatisfied()
//    {
//        InterlockingImpl interlockingImpl = new InterlockingImpl();
//        interlockingImpl.initialize_PetriNet();
//        interlockingImpl.addTrain("A", 3, 10);
//    }









}
