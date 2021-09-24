import java.util.ArrayList;

public class NetInit
{
    // Store the railway system
    ArrayList<Section> sectionsList = new ArrayList<Section>();

    void init_railway()
    {
        // section 1 - 11 are track sections
        for (int i = 1; i < 12; i++)
        {
            Section section = new Section(i);
            sectionsList.add(section);
        }

        // ----------------------------------Initialize Passenger Line entered with section 1 or 9------------------------------------
        // initialize section 1
        Section section_1 = sectionsList.get(0);
        Transition sect_1_tran = new Transition(Direction.South, sectionsList.get(4));
        section_1.addTransition(sect_1_tran);
        section_1.addDestination(sectionsList.get(7));
        section_1.addDestination(sectionsList.get(8));

        // initialize section 5
        Section section_5 = sectionsList.get(4);
        Transition sect_5_tran_1 = new Transition(Direction.South, sectionsList.get(7));
        Transition sect_5_tran_2 = new Transition(Direction.South, sectionsList.get(8));
        section_5.addTransition(sect_5_tran_1);
        section_5.addTransition(sect_5_tran_2);

        // initialize section 9
        Section section_9 = sectionsList.get(8);
        Transition sect_9_tran = new Transition(Direction.North, sectionsList.get(5));
        section_9.addTransition(sect_9_tran);
        section_9.addDestination(sectionsList.get(1));

        // ----------------------------------Initialize Passenger Line Entered with section 10------------------------------------
        // initialize section 10
        Section section_10 = sectionsList.get(9);
        Transition sect_10_tran = new Transition(Direction.North, sectionsList.get(5));
        section_10.addTransition(sect_10_tran);
        section_10.addDestination(sectionsList.get(1));

        // initialize section 6
        Section section_6 = sectionsList.get(5);
        Transition sect_6_tran = new Transition(Direction.North, sectionsList.get(1));
        section_6.addTransition(sect_6_tran);

        // ----------------------------------Initialize Freight Line-----------------------------------
        // initialize section 3
        Section section_3 = sectionsList.get(2);
        Transition sect_3_tran_1 = new Transition(Direction.South, sectionsList.get(3));
        Transition sect_3_tran_2 = new Transition(Direction.South, sectionsList.get(6));
        section_3.addTransition(sect_3_tran_1);
        section_3.addTransition(sect_3_tran_2);
        section_3.addDestination(sectionsList.get(3));
        section_3.addDestination(sectionsList.get(10));

        // initialize section 4
        Section section_4 = sectionsList.get(3);
        Transition sect_4_tran = new Transition(Direction.North, sectionsList.get(2));
        section_4.addTransition(sect_4_tran);
        section_4.addDestination(sectionsList.get(2));

        // initialize section 7
        Section section_7 = sectionsList.get(6);
        Transition sect_7_tranToS = new Transition(Direction.South, sectionsList.get(10));
        Transition sect_7_tranToN = new Transition(Direction.North, sectionsList.get(2));
        section_7.addTransition(sect_7_tranToS);
        section_7.addTransition(sect_7_tranToN);

        // initialize section 11
        Section section_11 = sectionsList.get(10);
        Transition sect_11_tran = new Transition(Direction.North, sectionsList.get(6));
        section_11.addTransition(sect_11_tran);
        section_11.addDestination(sectionsList.get(2));
    }

    ArrayList<Section> getSectionsList()
    {
        return sectionsList;
    }
}
