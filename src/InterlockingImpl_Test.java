import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

public class InterlockingImpl_Test
{
    @Test
    public void check_init()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        assertEquals(interlockingImpl.sections_list.size(),11);
        assertEquals(interlockingImpl.petriNet.policiesList.size(),8);
        assertEquals(interlockingImpl.petriNet.pointMachineList.size(),6);
    }

    @Test
    public void check_addTrain()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 11);
        assertEquals(interlockingImpl.getSection(3),"A");
        assertEquals(interlockingImpl.getTrain("A"),3);
        assertEquals(interlockingImpl.present_train_list.get(0).getDest_SectionId(),11);
        assertEquals(interlockingImpl.present_train_list.get(0).getTrain_direction(),Direction.South);

//        String[] trainsToMove = {"A"};
//        interlockingImpl.moveTrains(trainsToMove);
//
//        assertEquals(interlockingImpl.getTrain("A"),7);
        //assertEquals(interlockingImpl.getSection(7),"A");
    }

    @Test
    public void check_moveTrainOnce()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 11);

        String[] trainsToMove1 = {"A"};
        interlockingImpl.moveTrains(trainsToMove1);

        assertEquals(interlockingImpl.getTrain("A"),7);
        assertEquals(interlockingImpl.getSection(7),"A");
        assertEquals(interlockingImpl.getSection(3),"");

        String[] trainsToMove2 = {"A"};
        interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(interlockingImpl.getTrain("A"),11);
        assertEquals(interlockingImpl.getSection(11),"A");
        assertEquals(interlockingImpl.getSection(7),"");
    }



//    @Test(expected = IllegalArgumentException.class)
//    public void whenExceptionThrown_thenExpectationSatisfied()
//    {
//        InterlockingImpl interlockingImpl = new InterlockingImpl();
//        interlockingImpl.initialize_PetriNet();
//        interlockingImpl.addTrain("A", 3, 10);
//    }









}
