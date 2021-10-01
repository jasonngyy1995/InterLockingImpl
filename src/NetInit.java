import java.util.ArrayList;

class FiringPolicy
{
    String policy_name;
    Direction direction;
    int section_id_1;
    int section_id_2;
    boolean enabled;

    FiringPolicy(String policy_name,int section_id_1, int section_id_2, Direction direction)
    {
        this.policy_name = policy_name;
        this.direction = direction;
        this.section_id_1 = section_id_1;
        this.section_id_2 = section_id_2;
    }

    String getPolicy_name()
    {
        return this.policy_name;
    }

    int getSection_id_1()
    {
        return this.section_id_1;
    }

    int getSection_id_2()
    {
        return this.section_id_2;
    }
}

class PointMachine
{
    int machine_id;
    ArrayList<FiringPolicy> controlled_policies = new ArrayList<FiringPolicy>();

    PointMachine(int assigned_id)
    {
        this.machine_id = assigned_id;
    }

    void add_policy(FiringPolicy policy)
    {
        this.controlled_policies.add(policy);
    }
}

public class NetInit
{
    // Store the railway system
    ArrayList<Section> sectionsList = new ArrayList<Section>();
    ArrayList<FiringPolicy> policiesList = new ArrayList<FiringPolicy>();

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

    void init_firingPolicies()
    {
        FiringPolicy S3S7 = new FiringPolicy("S3S7", 3,7, Direction.BiDirection);
        policiesList.add(S3S7);
        FiringPolicy S3S4 = new FiringPolicy("S3S4", 3,4, Direction.BiDirection);
        policiesList.add(S3S4);
        FiringPolicy S6S2 = new FiringPolicy("S6S2", 6,2, Direction.North);
        policiesList.add(S6S2);
        FiringPolicy S1S5 = new FiringPolicy("S1S5", 1,5, Direction.South);
        policiesList.add(S1S5);
        FiringPolicy S10S6 = new FiringPolicy("S10S6", 10,6, Direction.North);
        policiesList.add(S10S6);
        FiringPolicy S9S6 = new FiringPolicy("S9S6",9,6, Direction.North);
        policiesList.add(S9S6);
        FiringPolicy S5S9 = new FiringPolicy("S5S9",5,9, Direction.South);
        policiesList.add(S5S9);
        FiringPolicy S5S8 = new FiringPolicy("S5S8",5,8, Direction.South);
        policiesList.add(S5S8);
    }

    ArrayList<FiringPolicy> getPoliciesList()
    {
        return policiesList;
    }

    void create_pointMachine(int machine_id, int prioritized_section_1, int prioritized_section_2, int second_section_1, int second_section_2)
    {
        PointMachine pointMachine = new PointMachine(machine_id);
        String prioritized_policy_name = "S"+prioritized_section_1+"S"+prioritized_section_2;
        String second_policy_name = "S"+second_section_1+"S"+second_section_2;

        // index 0 for prioritized policy
        for (int i = 0; i < policiesList.size(); i++)
        {
            if (policiesList.get(i).getPolicy_name().equals(prioritized_policy_name))
            {
                pointMachine.add_policy(policiesList.get(i));
            }
        }

        // index 1 for second policy
        for (int i = 0; i < policiesList.size(); i++)
        {
            if (policiesList.get(i).getPolicy_name().equals(second_policy_name))
            {
                pointMachine.add_policy(policiesList.get(i));
            }
        }
    }

    void init_pointMachines()
    {
        create_pointMachine(1,3,7,3,4);
        create_pointMachine(2,6,2,3,4);
        create_pointMachine(3,1,5,3,4);
        create_pointMachine(4,9,6,10,6);
        create_pointMachine(5,9,6,5,9);
        create_pointMachine(5,9,6,5,8);
    }
}
