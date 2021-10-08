import org.junit.Test;
import static org.junit.Assert.*;

public class InterlockingImpl_Test
{
    // check throwing exceptions of addTrain()
    @Test(expected = IllegalArgumentException.class)
    public void test_invalidPath()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 3, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_repeatedAddTrain()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 3, 11);
        interlockingImpl.addTrain("A", 4, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_repeatedNameAsExitedTrain()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 4, 3);
        String[] trainsToMove1 = {"A"};
        interlockingImpl.moveTrains(trainsToMove1);

        interlockingImpl.addTrain("B", 11, 3);
        String[] trainsToMove2 = {"B"};
        interlockingImpl.moveTrains(trainsToMove2);

        assertEquals(interlockingImpl.getSection(3),null);
        assertEquals(interlockingImpl.getTrain("A"),-1);
        assertEquals(interlockingImpl.exited_train_list.get(0).getTrainName(),"A");

        interlockingImpl.addTrain("A", 3, 4);
    }

    @Test(expected = IllegalStateException.class)
    public void test_occupiedSection()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 3, 11);
        interlockingImpl.addTrain("B", 3, 4);
    }

    // check throw exception of getSection(), getTrain()
    @Test(expected = IllegalArgumentException.class)
    public void test_invalidSectionId()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        // section id can only be 1 - 11
        interlockingImpl.getSection(12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_invalidTrainName()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 3, 11);
        interlockingImpl.addTrain("B", 4, 3);
        interlockingImpl.getTrain("C");
    }

    // check the initialization of petri net
    @Test
    public void check_init()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        assertEquals(interlockingImpl.sections_list.size(),11);
        assertEquals(interlockingImpl.petriNet.policiesList.size(),8);
        assertEquals(interlockingImpl.petriNet.pointMachineList.size(),6);
    }

    // check if functions getSection(), getTrain(), and information of a train are correct after addTrain()
    @Test
    public void check_addTrain()
    {
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 3, 11);
        assertEquals(interlockingImpl.getSection(3),"A");
        assertEquals(interlockingImpl.getTrain("A"),3);
        assertEquals(interlockingImpl.present_train_list.get(0).getDest_SectionId(),11);
        assertEquals(interlockingImpl.present_train_list.get(0).getTrain_direction(),Direction.South);
    }

    @Test
    public void check_firingPolicy_S11ToS3() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        interlockingImpl.addTrain("A", 11, 3);
        String[] trainsToMove1 = {"A"};
        interlockingImpl.moveTrains(trainsToMove1);
        interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(interlockingImpl.getTrain("A"), 3);
        assertEquals(interlockingImpl.getSection(3), "A");
    }

    @Test
    public void check_basicTrainMove()
    {
        // move A from 3 to 11, confirm train can move to both North and South between S3 and S7 (see previous test)
        InterlockingImpl interlockingImpl = new InterlockingImpl();
        interlockingImpl.addTrain("A", 3, 11);

        String[] trainsToMove1 = {"A"};
        interlockingImpl.moveTrains(trainsToMove1);

        assertEquals(interlockingImpl.getTrain("A"),7);
        assertEquals(interlockingImpl.getSection(7),"A");
        assertEquals(interlockingImpl.getSection(3),null);

        String[] trainsToMove2 = {"A"};
        interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(interlockingImpl.getTrain("A"),11);
        assertEquals(interlockingImpl.getSection(11),"A");
        assertEquals(interlockingImpl.getSection(7),null);

        // move B from 10 to 2
        interlockingImpl.addTrain("B", 10,2);
        String[] trainsToMove3 = {"B"};
        interlockingImpl.moveTrains(trainsToMove3);
        assertEquals(interlockingImpl.exited_train_list.get(0).getTrainName(),"A");
        assertEquals(interlockingImpl.getTrain("A"),-1);
        assertEquals(interlockingImpl.getSection(11),null);
        assertEquals(interlockingImpl.getSection(6),"B");

        // add C into the same path as B
        // see if the same path works for multiple trains
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

        interlockingImpl.addTrain("A", 9, 2);
        interlockingImpl.addTrain("B", 10, 2);

        // train in S9 always has priority over the train in S10
        // when trains in both sections move simultaneously, only the train in S9 can move
        String[] trainsToMove1 = {"A", "B"};
        int moved_trains = interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(moved_trains,1);
        assertEquals(interlockingImpl.getTrain("A"),6);
        assertEquals(interlockingImpl.getSection(6),"A");
        assertEquals(interlockingImpl.getTrain("B"),10);
        assertEquals(interlockingImpl.getSection(10),"B");

        // check if trains move to next sections correctly
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

        interlockingImpl.addTrain("A", 9, 2);
        interlockingImpl.addTrain("B", 1, 9);

        String[] trainsToMove = {"B"};
        interlockingImpl.moveTrains(trainsToMove);

        interlockingImpl.addTrain("C", 1, 8);
        String[] trainsToMove1 = {"B","A","C"};
        int moved_trains1 = interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(interlockingImpl.getTrain("A"), 6);
        assertEquals(interlockingImpl.getSection(6), "A");
        assertEquals(interlockingImpl.getTrain("B"), 9);
        assertEquals(interlockingImpl.getSection(9), "B");
        assertEquals(moved_trains1,3);

        // check if train C can use the intersection after B moved to section 9
        String[] trainsToMove2 = {"C"};
        int moved_trains2 = interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(moved_trains2,1);
        assertEquals(interlockingImpl.getTrain("C"), 8);
        assertEquals(interlockingImpl.getSection(8), "C");
        assertEquals(interlockingImpl.getSection(9), null);
        assertEquals(interlockingImpl.getTrain("B"), -1);
    }

    @Test
    public void check_firingPolicy_S4ToS3() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        // check if a train is able to move from 4 to 3
        interlockingImpl.addTrain("A", 4, 3);
        String[] trainsToMove1 = {"A"};
        interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(interlockingImpl.getTrain("A"), 3);
        assertEquals(interlockingImpl.getSection(3), "A");
    }

    @Test
    public void check_firingPolicy_S3ToS4() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        // check if a train is able to move from 3 to 4
        interlockingImpl.addTrain("A", 3, 4);
        String[] trainsToMove1 = {"A"};
        interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(interlockingImpl.getTrain("A"), 4);
        assertEquals(interlockingImpl.getSection(4), "A");
    }

    @Test
    public void check_firingPolicy_S3S4_S3S7() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        interlockingImpl.addTrain("A", 3, 4);
        interlockingImpl.addTrain("B", 11, 3);
        String[] trainsToMove1 = {"B"};
        interlockingImpl.moveTrains(trainsToMove1);
        String[] trainsToMove2 = {"B","A"};
        interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(interlockingImpl.getTrain("A"), 4);
        assertEquals(interlockingImpl.getSection(4), "A");
        // even B is assigned to move first, check if a train in 7 is able takeover 3 after a train moved from 3 to 4
        assertEquals(interlockingImpl.getTrain("B"), 3);
        assertEquals(interlockingImpl.getSection(3), "B");

    }

    @Test
    public void check_firingPolicy_S3S4_S3S7_1() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        interlockingImpl.addTrain("A", 4, 3);
        interlockingImpl.addTrain("B", 11, 3);

        // B moves one section first
        String[] trainsToMove1 = {"B"};
        interlockingImpl.moveTrains(trainsToMove1);
        assertEquals(interlockingImpl.getTrain("B"), 7);
        assertEquals(interlockingImpl.getSection(7), "B");

        // B should move to 3 and A is still in B, since in the same round, the train in section 7 has a priority to move to section 3 over the train in section 4
        String[] trainsToMove2 = {"A","B"};
        interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(interlockingImpl.getTrain("B"), 3);
        assertEquals(interlockingImpl.getSection(3), "B");
        assertEquals(interlockingImpl.getTrain("A"), 4);
        assertEquals(interlockingImpl.getSection(4), "A");
    }

    @Test
    public void check_firingPolicy_S3S4_S3S7_2() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        interlockingImpl.addTrain("A", 4, 3);
        interlockingImpl.addTrain("B", 3, 11);

        // check if train can move from 4 to 3 after the train in 3 moved to 7
        String[] trainsToMove = {"B","A"};
        int first_round_moved = interlockingImpl.moveTrains(trainsToMove);
        assertEquals(interlockingImpl.getTrain("B"), 7);
        assertEquals(interlockingImpl.getSection(7), "B");
        assertEquals(interlockingImpl.getTrain("A"), 3);
        assertEquals(interlockingImpl.getSection(3), "A");
        assertEquals(first_round_moved, 2);

        String[] trainsToMove2 = {"B"};
        int second_round_moved = interlockingImpl.moveTrains(trainsToMove2);
        assertEquals(interlockingImpl.getTrain("B"), 11);
        assertEquals(interlockingImpl.getSection(11), "B");
        // test if getTrain() and getSection() return correctly
        assertEquals(interlockingImpl.getTrain("A"), -1);
        assertEquals(interlockingImpl.getSection(3), null);
        assertEquals(second_round_moved, 1);
    }

    @Test
    public void check_firingPolicy_S3S4_S6S2_S3S7() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        interlockingImpl.addTrain("A", 4, 3);
        interlockingImpl.addTrain("B", 10, 2);
        interlockingImpl.addTrain("C", 1, 8);
        interlockingImpl.addTrain("D", 11, 3);

        String[] trainsToMove = {"B"};
        interlockingImpl.moveTrains(trainsToMove);

        // random order
        String[] trainsToMove1 = {"B","C","D","A"};
        int num = interlockingImpl.moveTrains(trainsToMove1);

        // check if all trains moved except A, since in the same round, the train in section 7 has a priority to move to section 3 over the train in section 4
        assertEquals(interlockingImpl.getTrain("B"), 2);
        assertEquals(interlockingImpl.getSection(2), "B");
        assertEquals(interlockingImpl.getTrain("C"), 5);
        assertEquals(interlockingImpl.getSection(5), "C");
        assertEquals(interlockingImpl.getTrain("D"), 7);
        assertEquals(interlockingImpl.getSection(7), "D");
        assertEquals(interlockingImpl.getTrain("A"), 4);
        assertEquals(interlockingImpl.getSection(4), "A");
        assertEquals(num,3);
    }

    @Test
    public void moreThanOneTrain_exit_at_theSameTime() {
        InterlockingImpl interlockingImpl = new InterlockingImpl();

        interlockingImpl.addTrain("B", 10, 2);
        interlockingImpl.addTrain("C", 1, 8);
        interlockingImpl.addTrain("D", 11, 3);

        String[] trainsToMove1 = {"B", "C"};
        interlockingImpl.moveTrains(trainsToMove1);
        interlockingImpl.moveTrains(trainsToMove1);

        String[] trainsToMove2 = {"D"};
        interlockingImpl.moveTrains(trainsToMove2);

        // B and C should exit at the same time
        assertEquals(interlockingImpl.getTrain("B"), -1);
        assertEquals(interlockingImpl.getSection(2), null);
        assertEquals(interlockingImpl.getTrain("C"), -1);
        assertEquals(interlockingImpl.getSection(8), null);
        assertEquals(interlockingImpl.getTrain("D"), 7);
        assertEquals(interlockingImpl.getSection(7), "D");
    }

}
