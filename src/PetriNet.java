import java.util.ArrayList;

class FiringPolicy
{
    String policy_name;
    boolean enabled;

    // constructor
    FiringPolicy(String policy_name)
    {
        this.policy_name = policy_name;
    }

    // getters
    String getPolicy_name()
    {
        return this.policy_name;
    }

    boolean getEnabled()
    {
        return this.enabled;
    }

    // setters
    void setEnabled(boolean permission)
    {
        this.enabled = permission;
    }
}

class PointMachine
{
    int machine_id;
    ArrayList<FiringPolicy> controlled_policies = new ArrayList<FiringPolicy>();

    // constructor
    PointMachine(int assigned_id)
    {
        this.machine_id = assigned_id;
    }

    // getter
    int getMachine_id()
    {
        return this.machine_id;
    }

    // function to add policies to point machine
    void add_policy(FiringPolicy policy)
    {
        this.controlled_policies.add(policy);
    }

    // getter
    ArrayList<FiringPolicy> getControlled_policies()
    {
        return this.controlled_policies;
    }

    // set the default first and second prioritized firing policies
    void default_enabled_policy(int index, boolean permission)
    {
        this.controlled_policies.get(index).setEnabled(permission);
    }
}

public class PetriNet
{
    // Store the railway system
    ArrayList<Section> sectionsList = new ArrayList<Section>();
    ArrayList<FiringPolicy> policiesList = new ArrayList<FiringPolicy>();
    ArrayList<PointMachine> pointMachineList = new ArrayList<PointMachine>();

    // function to initialize the railway system which is formed by sections and transitions
    void init_railway()
    {
        // section 1 - 11 are track sections
        for (int i = 1; i < 12; i++)
        {
            Section section = new Section(i,"");
            sectionsList.add(section);
        }

        // ----------------------------------Initialize Passenger Line entered with section 1 or 9------------------------------------
        // initialize section 1
        Section section_1 = sectionsList.get(0);
        Transition sect_1_tran = new Transition(Direction.South, sectionsList.get(4).getSectionId());
        section_1.addTransition(sect_1_tran);
        section_1.addDestination(sectionsList.get(7));
        section_1.addDestination(sectionsList.get(8));

        // initialize section 5
        Section section_5 = sectionsList.get(4);
        Transition sect_5_tran_1 = new Transition(Direction.South, sectionsList.get(7).getSectionId());
        Transition sect_5_tran_2 = new Transition(Direction.South, sectionsList.get(8).getSectionId());
        section_5.addTransition(sect_5_tran_1);
        section_5.addTransition(sect_5_tran_2);

        // initialize section 9
        Section section_9 = sectionsList.get(8);
        Transition sect_9_tran = new Transition(Direction.North, sectionsList.get(5).getSectionId());
        section_9.addTransition(sect_9_tran);
        section_9.addDestination(sectionsList.get(1));

        // ----------------------------------Initialize Passenger Line Entered with section 10------------------------------------
        // initialize section 10
        Section section_10 = sectionsList.get(9);
        Transition sect_10_tran = new Transition(Direction.North, sectionsList.get(5).getSectionId());
        section_10.addTransition(sect_10_tran);
        section_10.addDestination(sectionsList.get(1));

        // initialize section 6
        Section section_6 = sectionsList.get(5);
        Transition sect_6_tran = new Transition(Direction.North, sectionsList.get(1).getSectionId());
        section_6.addTransition(sect_6_tran);

        // ----------------------------------Initialize Freight Line-----------------------------------
        // initialize section 3
        Section section_3 = sectionsList.get(2);
        Transition sect_3_tran_1 = new Transition(Direction.South, sectionsList.get(3).getSectionId());
        Transition sect_3_tran_2 = new Transition(Direction.South, sectionsList.get(6).getSectionId());
        section_3.addTransition(sect_3_tran_1);
        section_3.addTransition(sect_3_tran_2);
        section_3.addDestination(sectionsList.get(3));
        section_3.addDestination(sectionsList.get(10));

        // initialize section 4
        Section section_4 = sectionsList.get(3);
        Transition sect_4_tran = new Transition(Direction.North, sectionsList.get(2).getSectionId());
        section_4.addTransition(sect_4_tran);
        section_4.addDestination(sectionsList.get(2));

        // initialize section 7
        Section section_7 = sectionsList.get(6);
        Transition sect_7_tranToS = new Transition(Direction.South, sectionsList.get(10).getSectionId());
        Transition sect_7_tranToN = new Transition(Direction.North, sectionsList.get(2).getSectionId());
        section_7.addTransition(sect_7_tranToS);
        section_7.addTransition(sect_7_tranToN);

        // initialize section 11
        Section section_11 = sectionsList.get(10);
        Transition sect_11_tran = new Transition(Direction.North, sectionsList.get(6).getSectionId());
        section_11.addTransition(sect_11_tran);
        section_11.addDestination(sectionsList.get(2));
    }

    // getters
    ArrayList<Section> getSectionsList()
    {
        return sectionsList;
    }

    // function to create all firing policies in accordance to the petri net design
    void init_firingPolicies()
    {
        FiringPolicy S3S7 = new FiringPolicy("S3S7");
        policiesList.add(S3S7);
        FiringPolicy S3S4 = new FiringPolicy("S3S4");
        policiesList.add(S3S4);
        FiringPolicy S6S2 = new FiringPolicy("S6S2");
        policiesList.add(S6S2);
        FiringPolicy S1S5 = new FiringPolicy("S1S5");
        policiesList.add(S1S5);
        FiringPolicy S10S6 = new FiringPolicy("S10S6");
        policiesList.add(S10S6);
        FiringPolicy S9S6 = new FiringPolicy("S9S6");
        policiesList.add(S9S6);
        FiringPolicy S5S9 = new FiringPolicy("S5S9");
        policiesList.add(S5S9);
        FiringPolicy S5S8 = new FiringPolicy("S5S8");
        policiesList.add(S5S8);
    }

    // getter
    ArrayList<FiringPolicy> getPoliciesList()
    {
        return policiesList;
    }

    // function to create a point machine
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

        pointMachine.default_enabled_policy(0,true);
        pointMachine.default_enabled_policy(1,false);

        // add it to the list of total point machines
        pointMachineList.add(pointMachine);
    }

    // function to call to set up 6 point machines according to the petri net design
    void init_pointMachines()
    {
        create_pointMachine(1,3,7,3,4);
        create_pointMachine(2,6,2,3,4);
        create_pointMachine(3,1,5,3,4);
        create_pointMachine(4,9,6,10,6);
        create_pointMachine(5,9,6,5,9);
        create_pointMachine(6,9,6,5,8);
    }

    // getter
    ArrayList<PointMachine> getPointMachineList()
    {
        return pointMachineList;
    }

    // function for changing the enabled policy of a point machine
    void pointMachine_changeEnabled(int machine_id)
    {
        PointMachine target_machine = pointMachineList.get(machine_id - 1);
        ArrayList<FiringPolicy> tmp_policyList = target_machine.getControlled_policies();

        for (int i = 0; i < tmp_policyList.size(); i++)
        {
            if (tmp_policyList.get(i).getEnabled() == true)
            {
                tmp_policyList.get(i).setEnabled(false);
            } else
            {
                tmp_policyList.get(i).setEnabled(true);
            }
        }
    }

    // function to find the index of a train in present_train_list
    int getTrainPos(String name, ArrayList<Train> trainList)
    {
        int pos;
        for (int i = 0; i < trainList.size(); i++)
        {
            if (trainList.get(i).getTrainName().equals(name))
            {
                pos = i;
                return pos;
            }
        }
        return -1;
    }

    // function to enable both policies
    void set_both_policy(int machine_id)
    {
        PointMachine target_machine = pointMachineList.get(machine_id - 1);
        ArrayList<FiringPolicy> tmp_policyList = target_machine.getControlled_policies();

        for (int i = 0; i < tmp_policyList.size(); i++)
        {
            tmp_policyList.get(i).setEnabled(true);
        }
    }

    // function to set the second policy as prioritized policy of a point machine
    void set_second_policy(int m_id)
    {
        PointMachine pointMachine = pointMachineList.get(m_id - 1);
        ArrayList<FiringPolicy> policies = pointMachine.getControlled_policies();

        if (policies.get(1).getEnabled() == false)
        {
            pointMachine_changeEnabled(m_id);
        }

    }

    // function to reset the default prioritized policy of a point machine
    void reset_to_default(int m_id)
    {
        PointMachine pointMachine = pointMachineList.get(m_id - 1);
        ArrayList<FiringPolicy> policies = pointMachine.getControlled_policies();

        if (policies.get(0).getEnabled() == false)
        {
            pointMachine_changeEnabled(m_id);
        }

    }

    // function to check if enabled policy of each point machine can be changed in current railway corridor
    void update_PointMachine(ArrayList<Section> sectionsList, ArrayList<Train> trainList)
    {
        // controller of disallowing any possible change to S3S4 later
        int S3S4_blocked_already = 0;

        for (PointMachine pointMachine: pointMachineList)
        {
            // update instruction for point machine 1
            if (pointMachine.getMachine_id() == 1)
            {
                // (For train from 4 to 3) if section 3 or 4 and 7 are empty
                if ((sectionsList.get(2).getOccupyingTrain_name().equals("") || sectionsList.get(3).getOccupyingTrain_name().equals("")) && sectionsList.get(6).getOccupyingTrain_name().equals(""))
                {
                    set_second_policy(1);
                }

                // (For train from 3 to 4 or 7) if section 4 is empty
                if (sectionsList.get(3).getOccupyingTrain_name().equals(""))
                {
                    set_both_policy(1);
                }

                // For train from 4 to 3, if section 3 is empty and section 7 is not empty
                if (sectionsList.get(2).getOccupyingTrain_name().equals("") && !sectionsList.get(6).getOccupyingTrain_name().equals(""))
                {
                    int sec7TrainPos = getTrainPos(sectionsList.get(6).getOccupyingTrain_name(), trainList);
                    Train sec7Train = trainList.get(sec7TrainPos);

                    // check if the section 7 train is to South, if yes, no collision and change is triggered
                    if (sec7Train.getTrain_direction() == Direction.South)
                    {
                        set_second_policy(1);

                        // if section 7 train is to North, change to default policy despite any situation
                    } else if (sec7Train.getTrain_direction() == Direction.North)
                    {
                        reset_to_default(1);
                        // inform other point machines S3S4 is blocked
                        S3S4_blocked_already += 1;
                    }
                }

            }

            // update instruction for point machine 2
            if (pointMachine.getMachine_id() == 2)
            {
                // if section 6 is empty and S3S4 is not blocked by point machine 1
                if (sectionsList.get(5).getOccupyingTrain_name().equals("") && S3S4_blocked_already == 0)
                {
                    set_second_policy(2);

                } else {
                    reset_to_default(2);
                }
            }

            // update instruction for point machine 3
            if (pointMachine.getMachine_id() == 3)
            {
                // if section 6 is empty and S3S4 is not blocked by point machine 1
                if (sectionsList.get(0).getOccupyingTrain_name().equals("") && S3S4_blocked_already == 0)
                {
                    set_second_policy(3);

                } else {
                    reset_to_default(3);
                }
            }

            // update instruction for point machine 4
            if (pointMachine.getMachine_id() == 4)
            {
                // if section 6 and 9 are empty
                if (sectionsList.get(5).getOccupyingTrain_name().equals("") && sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    set_second_policy(4);

                    // if section 6 is empty
                } else if (sectionsList.get(5).getOccupyingTrain_name().equals("") && !sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    int sec9TrainPos = getTrainPos(sectionsList.get(8).getOccupyingTrain_name(), trainList);
                    Train sec9Train = trainList.get(sec9TrainPos);

                    // check if section 9 train is to South, if yes, no collision and change is triggered
                    if (sec9Train.getTrain_direction() == Direction.South)
                    {
                        set_second_policy(4);
                    }

                    // if section 6 or 9 are not empty
                } else if (!sectionsList.get(5).getOccupyingTrain_name().equals("") || !sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    reset_to_default(4);
                }
            }

            // update instruction for point machine 5
            if (pointMachine.getMachine_id() == 5)
            {
                // if section 9 is empty
                if (sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    set_second_policy(5);

                } else {
                    reset_to_default(5);
                }
            }

            // update instruction for point machine 6
            if (pointMachine.getMachine_id() == 6)
            {
                // if section 9 is empty
                if (sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    set_second_policy(6);

                } else if (!sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    int sec9TrainPos2 = getTrainPos(sectionsList.get(8).getOccupyingTrain_name(), trainList);
                    Train sec9Train2 = trainList.get(sec9TrainPos2);

                    // check if section 9 train is to South, if yes, no collision and change is triggered
                    if (sec9Train2.getTrain_direction() == Direction.South)
                    {
                        set_second_policy(6);

                    } else if (sec9Train2.getTrain_direction() == Direction.North)
                    {
                        reset_to_default(6);
                    }
                }
            }
        }
    }
}
