import org.junit.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class InterlockingImpl_Test
{
    // check throwing exceptions of addTrain()
    @Test(expected = IllegalArgumentException.class)
    public void test_invalidPath()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_repeatedAddTrain()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 11);
        interlockingImpl.addTrain("A", 3, 4);
    }

    @Test(expected = IllegalStateException.class)
    public void test_occupiedSection()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 11);
        interlockingImpl.addTrain("B", 3, 4);
    }

    // check throw exception of getSection(), getTrain()
    @Test(expected = IllegalArgumentException.class)
    public void test_invalidSectionId()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        // section id can only be 1 - 11
        interlockingImpl.getSection(12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidTrainName()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        interlockingImpl.addTrain("A", 3, 11);
        interlockingImpl.addTrain("B", 4, 3);
        interlockingImpl.getTrain("C");
    }

    // check the initialization of petri net
    @Test
    public void check_init()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();
        assertEquals(interlockingImpl.sections_list.size(),11);
        assertEquals(interlockingImpl.petriNet.policiesList.size(),8);
        assertEquals(interlockingImpl.petriNet.pointMachineList.size(),6);
    }

    // check if functions getSection(), getTrain(), and information of a train are correct after addTrain()
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
    }

    //
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

        interlockingImpl.addTrain("B", 10,2);
        String[] trainsToMove3 = {"B"};
        interlockingImpl.moveTrains(trainsToMove3);
        assertEquals(interlockingImpl.exited_train_list.get(0).getTrainName(),"A");
        assertEquals(interlockingImpl.getSection(6),"B");

        interlockingImpl.addTrain("C", 10,2);
        String[] trainsToMove4 = {"B","C"};
        interlockingImpl.moveTrains(trainsToMove4);
        assertEquals(interlockingImpl.getSection(2),"B");
        assertEquals(interlockingImpl.getSection(6),"C");

        String[] trainsToMove5 = {"C"};
        interlockingImpl.moveTrains(trainsToMove5);
        assertEquals(interlockingImpl.getSection(2),"C");
    }

    @Test
    public void check_firingPolicy_S9S6_S10S6()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();

        interlockingImpl.addTrain("A", 9, 2);
        interlockingImpl.addTrain("B", 10, 2);

        String[] trainsToMove1 = {"A", "B"};
        int moved_trains = interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(moved_trains,1);
        assertEquals(interlockingImpl.getTrain("A"),6);
        assertEquals(interlockingImpl.getSection(6),"A");
        assertEquals(interlockingImpl.getTrain("B"),10);
        assertEquals(interlockingImpl.getSection(10),"B");

        String[] trainsToMove2 = {"A", "B"};
        int moved_trains1 = interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(moved_trains1,2);
        assertEquals(interlockingImpl.getTrain("A"),2);
        assertEquals(interlockingImpl.getSection(2),"A");
        assertEquals(interlockingImpl.getTrain("B"),6);
        assertEquals(interlockingImpl.getSection(6),"B");

        String[] trainsToMove3 = {"B"};
        int moved_trains2 = interlockingImpl.moveTrains(trainsToMove3);
        assertEquals(moved_trains2,1);
        assertEquals(interlockingImpl.getTrain("B"),2);
        assertEquals(interlockingImpl.getSection(2),"B");
    }

    @Test
    public void check_firingPolicy_S9S6_S5S9_S5S8() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.initialize_PetriNet();

        interlockingImpl.addTrain("A", 9, 2);
        interlockingImpl.addTrain("B", 1, 9);

        String[] trainsToMove1 = {"A","B"};
        interlockingImpl.moveTrains(trainsToMove1);
        interlockingImpl.addTrain("C", 1, 8);
        String[] trainsToMove2 = {"B", "C"};
        interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(interlockingImpl.getTrain("A"), 6);
        assertEquals(interlockingImpl.getSection(6), "A");
        assertEquals(interlockingImpl.getTrain("B"), 9);
        assertEquals(interlockingImpl.getSection(9), "B");
        assertEquals(interlockingImpl.getTrain("C"), 5);
        assertEquals(interlockingImpl.getSection(5), "C");

        String[] trainsToMove3 = {"C"};
        interlockingImpl.moveTrains(trainsToMove3);
        assertEquals(interlockingImpl.getSection(9), "");
        assertEquals(interlockingImpl.getTrain("C"), 8);
        assertEquals(interlockingImpl.getSection(8), "C");
    }

    @Test
    public void check_firingPolicy_S3S4_S6S2() {

    }

    @Test
    public void check_firingPolicy_S3S4_S3S7() {

    }

    @Test
    public void check_firingPolicy_S3S4_S6S2_S3S7() {

    }
}
